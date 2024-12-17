//
// Created by wangqiang on 2024/12/17.
//
#include "DBWork.h"
#include <jni.h>
#include "sqlite3.h"
#include <string>
#include <iostream>
#include "Qp.h"

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

bool DBWork::insertData(std::string path, std::string hybrideId, int version, std::string url) {
    sqlite3 *db;
    int rc;
    char *errMsg = 0;
    std::string sql = "INSERT INTO qp (hybrideId, version, url) VALUES (?,?,?);";

    // 打开数据库
    rc = sqlite3_open(path.c_str(), &db);
    if (rc) {
        std::cerr << "Can't open database: " << sqlite3_errmsg(db) << std::endl;
        sqlite3_close(db);
        return (jboolean) false;
    }

    sqlite3_stmt *stmt;
    // 准备 SQL 语句
    rc = sqlite3_prepare_v2(db, sql.c_str(), -1, &stmt, nullptr);
    if (rc != SQLITE_OK) {
        std::cerr << "Failed to prepare SQL statement: " << sqlite3_errmsg(db) << std::endl;
        sqlite3_close(db);
        return (jboolean) false;
    }

    // 绑定参数
    sqlite3_bind_text(stmt, 1, hybrideId.c_str(), -1, SQLITE_STATIC);
    sqlite3_bind_int(stmt, 2, version);
    sqlite3_bind_text(stmt, 3, url.c_str(), -1, SQLITE_STATIC);

    // 执行 SQL 语句
    rc = sqlite3_step(stmt);
    if (rc != SQLITE_DONE) {
        std::cerr << "Failed to execute SQL statement: " << sqlite3_errmsg(db) << std::endl;
    } else {
        std::cout << "Record inserted successfully." << std::endl;
    }
    // 清理资源
    sqlite3_finalize(stmt);
    sqlite3_close(db);
    return (jboolean) true;
}

Qp DBWork::queryQp(std::string path, std::string hybridId){
    sqlite3 *db;
    int rc;
    char *errMsg = 0;
    std::string sql = "SELECT hybrideId, version, url FROM qp WHERE hybrideId =?;";

    Qp qp;

    // 打开数据库
    rc = sqlite3_open(path.c_str(), &db);
    if (rc) {
        std::cerr << "Can't open database: " << sqlite3_errmsg(db) << std::endl;
        sqlite3_close(db);
        return qp;
    }

    sqlite3_stmt *stmt;
    // 准备 SQL 语句
    rc = sqlite3_prepare_v2(db, sql.c_str(), -1, &stmt, nullptr);
    if (rc!= SQLITE_OK) {
        std::cerr << "Failed to prepare SQL statement: " << sqlite3_errmsg(db) << std::endl;
        sqlite3_close(db);
        return qp;
    }

    // 绑定参数
    sqlite3_bind_text(stmt, 1, hybridId.c_str(), -1, SQLITE_STATIC);

    // 执行查询
    rc = sqlite3_step(stmt);
    if (rc == SQLITE_ROW) {
        qp.version = sqlite3_column_int(stmt, 1);
        const char *url = (const char *)sqlite3_column_text(stmt, 2);
        qp.url = url? url : "";
        qp.hybrideId = hybridId;
    } else if (rc == SQLITE_DONE) {
        std::cerr << "No record found." << std::endl;
        sqlite3_close(db);
        return qp;
    } else {
        std::cerr << "Failed to execute SQL statement: " << sqlite3_errmsg(db) << std::endl;
        sqlite3_close(db);
        return qp;
    }

    // 清理资源
    sqlite3_finalize(stmt);
    sqlite3_close(db);

    return qp;


}