//
// Created by wangqiang on 2024/12/17.
//
#include "DBWork.h"
#include <jni.h>
#include "sqlite3.h"
#include <string>
#include <iostream>

bool DBWork::createDatabaseAndTable(std::string path) {
    sqlite3 *db;
    int rc;
    char *errMsg = nullptr;

    rc = sqlite3_open(path.c_str(), &db);
    if (rc) {
        std::cerr << "Can't open database: " << sqlite3_errmsg(db) << std::endl;
        sqlite3_close(db);
        return false;
    }

    // 创建表
    return createTable(db);

    // 关闭数据库
    //sqlite3_close(db);
}

// 创建 qp 表的函数
bool DBWork::createTable(sqlite3 *db) {
    int rc;
    char *errMsg = nullptr;
    std::string createTableSQL = "CREATE TABLE IF NOT EXISTS qp ("
                                 "hybrideId TEXT PRIMARY KEY, "
                                 "url TEXT, "
                                 "version TEXT);";

    // 执行 SQL 语句创建表
    rc = sqlite3_exec(db, createTableSQL.c_str(), nullptr, nullptr, &errMsg);
    if (rc != SQLITE_OK) {
        std::cerr << "SQL error: " << errMsg << std::endl;
        sqlite3_free(errMsg);
        sqlite3_close(db);
        return false;
    }

    std::cout << "Table qp created successfully." << std::endl;
    return true;
}