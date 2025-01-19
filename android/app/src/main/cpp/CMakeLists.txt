cmake_minimum_required(VERSION 3.11.0)
project("qpLib" VERSION 1.0)
# 设置源文件和头文件目录
set(CMAKE_CXX_FLAGS "${CMAKE_CXX_FLAGS} -std=c++11 -fPIC")

message("cmake地址" + ${CMAKE_SOURCE_DIR} + ${CMAKE_ANDROID_ARCH_ABI})

set(LIBRARY_OUTPUT_PATH ${CMAKE_CURRENT_SOURCE_DIR}/build)
set(DIR "${CMAKE_CURRENT_SOURCE_DIR}/../jniLibs/${CMAKE_ANDROID_ARCH_ABI}")

# 创建共享库，指定.so后缀
add_library(qpLib SHARED src/DownloadWorker.cpp
        src/jni_tools.cpp
        src/DBWork.cpp
        src/ZipTask.cpp
        src/MD5.cpp
        src/sqlite3.c)

find_library(log-lib log)


include_directories(${CMAKE_CURRENT_SOURCE_DIR}/include)

# 开始添加3rd的相关库的依赖
# 添加curl库
add_library(curl
        SHARED
        IMPORTED)

set_target_properties(
        curl
        PROPERTIES IMPORTED_LOCATION
        ${DIR}/libcurl.so
)

add_library(zip
        SHARED
        IMPORTED)

set_target_properties(
        zip
        PROPERTIES IMPORTED_LOCATION
        ${DIR}/libzip.so
)

add_library(ssl
        SHARED
        IMPORTED)

set_target_properties(
        ssl
        PROPERTIES IMPORTED_LOCATION
        ${DIR}/libssl.so
)

add_library(crypto
        SHARED
        IMPORTED)

set_target_properties(
        crypto
        PROPERTIES IMPORTED_LOCATION
        ${DIR}/libcrypto.so
)

target_link_libraries(
        qpLib
        curl
        zip
        ssl
        crypto
        ${log-lib}
)
