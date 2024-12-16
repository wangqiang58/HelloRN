//
// Created by wangqiangt on 2024/12/9.
//

#ifndef HELLORN_DOWNLOADWORKER_H
#define HELLORN_DOWNLOADWORKER_H

#include <iostream>
#include <queue>
#include <string>
#include <thread>
#include <mutex>
#include <condition_variable>
#include "DownloadTask.h"
#include <fstream>
#include "curl/curl.h"


class DownloadWorker {
public:
    DownloadWorker();

    ~DownloadWorker();

    void addTask(const DownloadTask &task);

    void start();

    void stop();

protected:
    std::queue<DownloadTask> m_taskQueue;
    std::mutex m_mutex;
    std::condition_variable m_cv;
    std::thread m_workerThread;
    bool m_stop = false;

    void run();

    bool download(const DownloadTask &task);
};

#endif //HELLORN_DOWNLOADWORKER_H
