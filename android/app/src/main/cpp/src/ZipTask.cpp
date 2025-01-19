//
// Created by wangqiang on 2024/12/26.
//
#include "ZipTask.h"
#include <string>
#include <vector>
#include "zlib/zip.h"
#include <iostream>
#include "fstream"
#include <unistd.h>
#include <sys/stat.h>
#include <stdio.h>

#define DBG_SEQ 0
#define LOGI printf
#define LOGD printf
#define LOGE printf

bool ZipTask::unzip(std::string zipFilePath, std::string outputDirectory) {
    int err = 0;
    zip *z = zip_open(zipFilePath.c_str(), 0, &err);
    if (z == nullptr) {
        std::cerr << "Failed to open zip file: " << zip_strerror(z) << std::endl;
        return false;
    }

    int numEntries = zip_get_num_entries(z, 0);
    for (int i = 0; i < numEntries; ++i) {
        zip_stat_t entry_stat;
        if (zip_stat_index(z, i, 0, &entry_stat) == 0) {
            std::string entryName(entry_stat.name);
            std::string fullPath = outputDirectory + "/" + entryName;

            // 对于目录，创建目录
            if (entry_stat.name[strlen(entry_stat.name) - 1] == '/') {
                if (mkdir(fullPath.c_str(), 0755) == -1) {
                    std::cerr << "Failed to create directory " << fullPath << ": " << strerror(errno) << std::endl;
                    zip_close(z);
                    return false;
                }
            } else {
                // 对于文件，创建目录（如果需要）并打开文件
                std::string directory = fullPath.substr(0, fullPath.find_last_of('/'));
                if (mkdir(directory.c_str(), 0755) == -1 && errno!= EEXIST) {
                    std::cerr << "Failed to create directory " << directory << ": " << strerror(errno) << std::endl;
                    zip_close(z);
                    return false;
                }

                zip_file *f = zip_fopen_index(z, i, 0);
                if (f == nullptr) {
                    std::cerr << "Failed to open entry " << entryName << ": " << zip_strerror(z) << std::endl;
                    zip_close(z);
                    return false;
                }

                FILE *out = fopen(fullPath.c_str(), "wb");
                if (out == nullptr) {
                    std::cerr << "Failed to create file " << fullPath << ": " << strerror(errno) << std::endl;
                    zip_fclose(f);
                    zip_close(z);
                    return false;
                }

                char buffer[1024];
                ssize_t len;
                while ((len = zip_fread(f, buffer, sizeof(buffer))) > 0) {
                    if (fwrite(buffer, 1, len, out)!= len) {
                        std::cerr << "Failed to write data to file " << fullPath << std::endl;
                        fclose(out);
                        zip_fclose(f);
                        zip_close(z);
                        return false;
                    }
                }

                fclose(out);
                zip_fclose(f);
            }
        } else {
            std::cerr << "Failed to get entry statistics for entry " << i << std::endl;
            zip_close(z);
            return false;
        }
    }

    zip_close(z);
    return true;
}