//
// Created by wangqiang on 2024/12/17.
//

#ifndef HELLORN_DBWORK_H
#define HELLORN_DBWORK_H

#include <string>
#include "sqlite3.h"
#include "Qp.h"

class DBWork {
public:
    bool createDatabaseAndTable(std::string path);

    bool createTable(sqlite3 *db);

    bool insertData(std::string path, std::string hybrideId, int version, std::string url);

    Qp queryQp(std::string path, std::string hybrideId);

};

#endif //HELLORN_DBWORK_H
