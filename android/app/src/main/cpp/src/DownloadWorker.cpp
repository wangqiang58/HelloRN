////
//// Created by wangqiangt on 2024/12/8.
////

#include "DownloadWorker.h"
#include "curl/curl.h"
#include <android/log.h>
#include <jni.h>
#include "ZipTask.h"
#include "DBWork.h"
#include "MD5.h"

DownloadWorker::DownloadWorker() {
    curl_global_init(CURL_GLOBAL_ALL);
}

DownloadWorker::~DownloadWorker() {
    curl_global_cleanup();
}

bool DownloadWorker::addTask(const QpInfo downloadTask) {
    //1、下载
    std::string tempFile =
            downloadTask.outputPath + "/" + getFileNameWithSuffix(downloadTask.update_url);
    bool result = download(downloadTask, tempFile);
    if (result) {
        MD5 md5;
        md5.reset();
        std::ifstream stream = std::ifstream(tempFile);
        md5.update(stream);
        __android_log_write(ANDROID_LOG_INFO, "Test", ("md5:" + md5.toString()).c_str());
        if (md5.toString().c_str() != downloadTask.md5) {
            return false;
        }
        //2、解压
        std::shared_ptr<ZipTask> unzipTask = std::make_shared<ZipTask>();
        unzipTask->unzip(tempFile, downloadTask.unzipDir);
        //3、插入db
        std::shared_ptr<DBWork> dbWork = std::make_shared<DBWork>();
        std::string fileName =
                downloadTask.unzipDir + "/" + getFileNameFromURL(downloadTask.update_url);
        dbWork->insertData(downloadTask.dbName, downloadTask.hybridId, downloadTask.version,
                           fileName);
        stream.close();
        std::remove(tempFile.c_str());
        return true;
    }
    return false;
}


std::string DownloadWorker::getFileNameFromURL(const std::string &url) {
    size_t found = url.find_last_of("/");
    if (found == std::string::npos) {
        return "";
    }
    std::string fileName = url.substr(found + 1);

    // 查找文件的后缀名
    size_t dotIndex = fileName.find_last_of(".");
    if (dotIndex != std::string::npos) {
        fileName = fileName.substr(0, dotIndex);
    }
    return fileName;
}

std::string DownloadWorker::getFileNameWithSuffix(const std::string &url) {
    size_t found = url.find_last_of("/");
    if (found == std::string::npos) {
        return "";
    }
    std::string fileName = url.substr(found + 1);
    return fileName;
}


static size_t write_data(void *ptr, size_t size, size_t nmemb, void *stream) {
    std::ofstream *file = (std::ofstream *) stream;
    file->write((char *) ptr, size * nmemb);
    return size * nmemb;
}

bool DownloadWorker::download(const QpInfo task, const std::string fileName) {
    std::string logMessage = "开始下载任务...." + fileName;
    __android_log_write(ANDROID_LOG_INFO, "Test", logMessage.c_str());

    CURL *curl = curl_easy_init();
    if (curl) {
        std::ofstream file(fileName, std::ios::binary);
        if (file.is_open()) {
            curl_easy_setopt(curl, CURLOPT_URL, task.update_url.c_str());
            curl_easy_setopt(curl, CURLOPT_WRITEFUNCTION, write_data);
            curl_easy_setopt(curl, CURLOPT_WRITEDATA, &file);

            CURLcode res = curl_easy_perform(curl);
            if (res != CURLE_OK) {
                std::cerr << "curl_easy_perform() failed for task: " << task.update_url
                          << " with error: "
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
            std::cerr << "Unable to open file for task: " << task.update_url << " at path: "
                      << task.outputPath << std::endl;
            curl_easy_cleanup(curl);
            return false;
        }
    }
    std::cerr << "Failed to initialize libcurl for task: " << task.update_url << std::endl;
    return false;
}