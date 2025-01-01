//
// Created by wangqiang on 2024/12/17.
//

#ifndef HELLORN_QP_H
#define HELLORN_QP_H

#include "string"

struct Qp {
    /**
     * qp 主键
     */
    std::string hybrideId;
    /**
     * qp 版本号
     */
    int version;
    /**
     * 解压以后 资源文件
     */
    std::string fileName;
};

#endif //HELLORN_QP_H
