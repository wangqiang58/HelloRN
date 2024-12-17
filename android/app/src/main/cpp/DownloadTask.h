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
    std::shared_ptr<CallbackObj> callbackObj;
};

#endif //HELLORN_DOWNLOADTASK_H
