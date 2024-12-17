//
// Created by wangqiang on 2024/12/17.
//

#ifndef HELLORN_DBWORK_H
#define HELLORN_DBWORK_H

#include <string>
#include "sqlite3.h"

class DBWork {
public:
    bool createDatabaseAndTable(std::string path);

    bool createTable(sqlite3* db);
};

#endif //HELLORN_DBWORK_H
