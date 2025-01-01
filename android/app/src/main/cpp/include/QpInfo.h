//
// Created by wangqiangt on 2024/12/8.
//

#ifndef HELLORN_QPINFO_H
#define HELLORN_QPINFO_H

#include <string>
#include "CallbackObj.h"

struct QpInfo {
    std::string update_url;
    std::string outputPath;
    std::string unzipDir;
    std::string dbName;
    std::string hybridId;
    int version;
    std::string md5;
};

#endif //HELLORN_QPINFO_H
