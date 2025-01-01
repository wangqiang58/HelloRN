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
#include "QpInfo.h"
#include <fstream>
#include "curl/curl.h"


class DownloadWorker {
public:
    DownloadWorker();

    ~DownloadWorker();

    bool addTask(const QpInfo task);

    bool download(const QpInfo task,const std::string fileName);

    std::string getFileNameFromURL(const std::string &url);

    std::string getFileNameWithSuffix(const std::string &url);

};

#endif //HELLORN_DOWNLOADWORKER_H
