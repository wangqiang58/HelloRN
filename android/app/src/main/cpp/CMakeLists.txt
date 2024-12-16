cmake_minimum_required(VERSION 3.11.0)
project("downloadLib" VERSION 1.0)
# 设置源文件和头文件目录
set(CMAKE_CXX_FLAGS "${CMAKE_CXX_FLAGS} -std=c++11 -fPIC")

message("cmake地址" + ${CMAKE_SOURCE_DIR})

# 创建共享库，指定.so后缀
add_library(downloadLib SHARED DownloadWorker.cpp DownloadWorker.h Student.cpp jni_tools.cpp)


include_directories(${CMAKE_CURRENT_SOURCE_DIR}/include)
target_include_directories(downloadLib PRIVATE ${CMAKE_CURRENT_SOURCE_DIR}/include)

find_library(log-lib log)

target_link_libraries(
        downloadLib
        ${CMAKE_CURRENT_SOURCE_DIR}/../jniLibs/arm64-v8a/libcurl.so
        ${log-lib}
)
