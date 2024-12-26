////
//// Created by wangqiangt on 2024/12/8.
////

#include "DownloadWorker.h"
#include "curl/curl.h"
#include <android/log.h>
#include <jni.h>


DownloadWorker::DownloadWorker() {
    curl_global_init(CURL_GLOBAL_ALL);
}

DownloadWorker::~DownloadWorker() {
    curl_global_cleanup();
}

void DownloadWorker::addTask(const DownloadTask task) {
    m_task = task;
}

void DownloadWorker::start() {
    m_workerThread = std::thread(&DownloadWorker::run, this);
}

void DownloadWorker::stop() {
    if (m_workerThread.joinable()) {
        m_workerThread.join();
    }
}

void DownloadWorker::run() {
    bool result = download(m_task);
    m_task.callback(result);
}

static size_t write_data(void *ptr, size_t size, size_t nmemb, void *stream) {
    std::ofstream *file = (std::ofstream *) stream;
    file->write((char *) ptr, size * nmemb);
    return size * nmemb;
}

bool DownloadWorker::download(const DownloadTask task) {
    std::string logMessage = "开始下载任务....";
    __android_log_write(ANDROID_LOG_INFO, "Test", logMessage.c_str());

    CURL *curl = curl_easy_init();
    if (curl) {
        std::ofstream file(task.outputPath, std::ios::binary);
        if (file.is_open()) {
            curl_easy_setopt(curl, CURLOPT_URL, task.url.c_str());
            curl_easy_setopt(curl, CURLOPT_WRITEFUNCTION, write_data);
            curl_easy_setopt(curl, CURLOPT_WRITEDATA, &file);

            CURLcode res = curl_easy_perform(curl);
            if (res != CURLE_OK) {
                std::cerr << "curl_easy_perform() failed for task: " << task.url << " with error: "
                          << curl_easy_strerror(res) << std::endl;
                file.close();
                curl_easy_cleanup(curl);
                std::string logMessage = "下载任务异常....";
                __android_log_write(ANDROID_LOG_INFO, "Test", logMessage.c_str());
                return false;
            }
            file.close();
            std::string logMessage = "下载任务OK....";
            __android_log_write(ANDROID_LOG_INFO, "Test", logMessage.c_str());
            curl_easy_cleanup(curl);
            return true;
        } else {
            std::cerr << "Unable to open file for task: " << task.url << " at path: "
                      << task.outputPath << std::endl;
            curl_easy_cleanup(curl);
            return false;
        }
    }
    std::cerr << "Failed to initialize libcurl for task: " << task.url << std::endl;
    return false;
}