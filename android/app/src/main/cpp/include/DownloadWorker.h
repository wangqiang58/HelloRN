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

    bool addTask(const DownloadTask task);

    bool download(const DownloadTask task);

};

#endif //HELLORN_DOWNLOADWORKER_H
