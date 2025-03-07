//
// Created by wangqiangt on 2024/12/8.
//
#include <jni.h>
#include <string>
#include "DownloadWorker.h"
#include <android/log.h>
#include "DBWork.h"
#include "zlib.h"
#include "ZipTask.h"
#include "android/log.h"


#define LOG_TAG "qpLib"
#define LOGD(...) __android_log_print(ANDROID_LOG_DEBUG, LOG_TAG, __VA_ARGS__)


// 全局 JavaVM 指针，通常在 JNI_OnLoad 中初始化
JavaVM *g_jvm;



// JNI_OnLoad 函数，在 JNI 库加载时调用
JNIEXPORT jint JNICALL JNI_OnLoad(JavaVM *vm, void *reserved) {
    g_jvm = vm;
    return JNI_VERSION_1_6;
}

extern "C"
JNIEXPORT void JNICALL
Java_com_hellorn_core_DownloadWorker_downloadNative(JNIEnv *env, jclass clazz, jstring update_url,
                                                    jstring download_dir,
                                                    jstring unzip_dir,
                                                    jstring dbName,
                                                    jstring hybridId,
                                                    int version,
                                                    jstring md5,
                                                    jobject callback) {

    std::shared_ptr<DownloadWorker> worker = std::make_shared<DownloadWorker>();

    const char *urlstr = env->GetStringUTFChars(update_url, nullptr);
    std::string str(urlstr);


    const char *str1 = env->GetStringUTFChars(unzip_dir, nullptr);
    std::string unZipstr(str1);

    const char *trgetstr = env->GetStringUTFChars(download_dir, nullptr);
    std::string download_dir_str(trgetstr);

    const char *hybridIdstr = env->GetStringUTFChars(hybridId, nullptr);
    std::string hybridIdstr2(hybridIdstr);

    const char *dbNamestr = env->GetStringUTFChars(dbName, nullptr);
    std::string dbNamestr2(dbNamestr);

    const char *md5Str = env->GetStringUTFChars(md5, nullptr);
    std::string md5str(md5Str);

    QpInfo task{
            update_url:urlstr,
            outputPath:download_dir_str,
            unzipDir:unZipstr,
            dbName:dbNamestr2,
            version:1,
            hybridId:hybridIdstr2,
            md5:md5str
    };

    //调用 work->addTask 方法
    // 创建全局引用以在其他线程中使用
    jobject globalCallback = env->NewGlobalRef(callback);
    // 获取回调方法
    jclass callbackClass = env->GetObjectClass(callback);
    jmethodID callbackMethodId = env->GetMethodID(callbackClass, "onResult", "(Z)V");

     // 调用 addTask 并传入 lambda 回调
    worker->addTask(task, [worker,globalCallback, callbackMethodId](bool result) {
        JNIEnv *threadEnv;
        if (g_jvm->AttachCurrentThread(&threadEnv, nullptr) != JNI_OK) {
            LOGD("Failed to attach thread");
            return;
        }

        // 调用 Java 回调方法
        threadEnv->CallVoidMethod(globalCallback, callbackMethodId, result);

        // 清理全局引用
        threadEnv->DeleteGlobalRef(globalCallback);

        // 分离线程
        g_jvm->DetachCurrentThread();
    });


    // 释放字符串资源
    env->ReleaseStringUTFChars(update_url, urlstr);
    env->ReleaseStringUTFChars(unzip_dir, str1);
    env->ReleaseStringUTFChars(download_dir, trgetstr);
    env->ReleaseStringUTFChars(hybridId, hybridIdstr);
    env->ReleaseStringUTFChars(dbName, dbNamestr);
    env->ReleaseStringUTFChars(md5, md5Str);
    return;
}

extern "C"
JNIEXPORT jboolean JNICALL
Java_com_hellorn_core_QPEngineManager_initDB(JNIEnv *env, jclass clazz, jstring dbPath) {
    std::shared_ptr<DBWork> work = std::make_shared<DBWork>();
//    DBWork *work = new DBWork();
    const char *pathStr = env->GetStringUTFChars(dbPath, nullptr);
    std::string str(pathStr);
    bool result = work->createDatabaseAndTable(str);
    return (jboolean) result;
}

std::string jstring2string(JNIEnv *env, jstring jStr) {
    const char *cStr = env->GetStringUTFChars(jStr, nullptr);
    std::string str(cStr);
    env->ReleaseStringUTFChars(jStr, cStr);
    return str;
}

extern "C"
JNIEXPORT jboolean JNICALL
Java_com_hellorn_core_QPEngineManager_insertRecord(JNIEnv *env, jclass clazz, jstring jPath,
                                                   jstring jHybridId,
                                                   jint
                                                   jVersion, jstring jUrl) {
    std::string hybridId = jstring2string(env, jHybridId);
    int version = jVersion;
    std::string path = jstring2string(env, jPath);
    std::string url = jstring2string(env, jUrl);

    std::shared_ptr<DBWork> work = std::make_shared<DBWork>();
    work->insertData(path, hybridId, version, url);
}
extern "C"
JNIEXPORT jobject JNICALL
Java_com_hellorn_core_QPEngineManager_queryQp(JNIEnv *env, jclass clazz, jstring jPath,
                                              jstring hybride_id) {
    std::string hybridId = jstring2string(env, hybride_id);

    std::shared_ptr<DBWork> work = std::make_shared<DBWork>();
    std::string path = jstring2string(env, jPath);

    Qp qp = work->queryQp(path, hybridId);

    // 创建 Java 对象
    jclass qpClass = env->FindClass("com/hellorn/core/Qp");
    if (qpClass == nullptr) {
        std::cerr << "Could not find Qp class." << std::endl;
        return nullptr;
    }
    jmethodID constructor = env->GetMethodID(qpClass, "<init>",
                                             "(Ljava/lang/String;ILjava/lang/String;Ljava/lang/String;)V");
    if (constructor == nullptr) {
        std::cerr << "Could not find Qp constructor." << std::endl;
        return nullptr;
    }
    jstring jUrl = env->NewStringUTF(qp.fileName.c_str());
    jstring jHybrideId = env->NewStringUTF(qp.hybrideId.c_str());
    jobject result = env->NewObject(qpClass, constructor, jHybrideId, qp.version, jUrl,
                                    env->NewStringUTF(""));
    env->DeleteLocalRef(jUrl);
    return result;
}