//
// Created by wangqiangt on 2024/12/8.
//

#ifndef HELLORN_DOWNLOADTASK_H
#define HELLORN_DOWNLOADTASK_H
#include <string>
#include "CallbackObj.h"

// 使用 std::function 作为回调类型
typedef std::function<void(bool result)> CallbackFunction;

struct DownloadTask {
    std::string url;
    std::string outputPath;
    CallbackFunction callback;
};

#endif //HELLORN_DOWNLOADTASK_H
