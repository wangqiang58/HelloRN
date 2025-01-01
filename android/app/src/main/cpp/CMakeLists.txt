cmake_minimum_required(VERSION 3.11.0)
project("qpLib" VERSION 1.0)
# 设置源文件和头文件目录
set(CMAKE_CXX_FLAGS "${CMAKE_CXX_FLAGS} -std=c++11 -fPIC")

message("cmake地址" + ${CMAKE_SOURCE_DIR})

set(LIBRARY_OUTPUT_PATH ${CMAKE_CURRENT_SOURCE_DIR}/build)


#file(GLOB SRC src/*.cpp)
#message("src:" +${SRC})

# 创建共享库，指定.so后缀
add_library(qpLib SHARED src/DownloadWorker.cpp
        src/jni_tools.cpp
        src/DBWork.cpp
        src/ZipTask.cpp
        src/MD5.cpp
        src/sqlite3.c)

include_directories(${CMAKE_CURRENT_SOURCE_DIR}/include)

#add_library(qpLib SHARED ${SRC})

target_include_directories(qpLib PRIVATE ${CMAKE_CURRENT_SOURCE_DIR}/include)

find_library(log-lib log)

target_link_libraries(
        qpLib
        ${CMAKE_CURRENT_SOURCE_DIR}/../jniLibs/arm64-v8a/libcurl.so
        ${CMAKE_CURRENT_SOURCE_DIR}/../jniLibs/arm64-v8a/libzip.so
        ${CMAKE_CURRENT_SOURCE_DIR}/../jniLibs/arm64-v8a/libssl.so
        ${CMAKE_CURRENT_SOURCE_DIR}/../jniLibs/arm64-v8a/libcrypto.so
        ${log-lib}
)
