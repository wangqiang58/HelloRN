//
// Created by wangqiangt on 2024/12/8.
//

#ifndef HELLORN_DOWNLOADTASK_H
#define HELLORN_DOWNLOADTASK_H

#include <string>
#include "CallbackObj.h"

struct DownloadTask {
    std::string url;
    std::string outputPath;
    std::string unzipDir;
    std::string dbName;
    std::string hybridId;
    int version;
};

#endif //HELLORN_DOWNLOADTASK_H
