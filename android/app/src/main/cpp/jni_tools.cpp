//
// Created by wangqiangt on 2024/12/8.
//
#include <jni.h>
#include <string>
#include "DownloadWorker.h"
#include <android/log.h>
#include "DBWork.h"

// 定义一个全局变量来保存回调接口的引用
static jobject g_callback = nullptr;

extern "C"
JNIEXPORT void JNICALL
Java_com_hellorn_core_QPEngineManager_download(JNIEnv *env, jclass clazz, jstring url, jstring dest,
                                               jobject callback) {
    g_callback = env->NewGlobalRef(callback);

    DownloadWorker *worker = new DownloadWorker();

    const char *urlstr = env->GetStringUTFChars(url, nullptr);
    std::string str(urlstr);

    const char *trgetstr = env->GetStringUTFChars(dest, nullptr);
    std::string outputstr(trgetstr);

    worker->start();

    DownloadTask task{
            url:urlstr,
            outputPath:trgetstr
    };
    if (g_callback != nullptr) {
        env->DeleteGlobalRef(g_callback);
    }
    g_callback = env->NewGlobalRef(callback);

    if (env != nullptr && g_callback != nullptr) {
        jclass callbackClass = env->GetObjectClass(g_callback);
        jmethodID callbackMethod = env->GetMethodID(callbackClass, "onResult", "(Z)V");
        if (callbackMethod != nullptr) {
            bool result = false;
            jboolean jresult = (jboolean) result;
            env->CallVoidMethod(g_callback, callbackMethod, jresult);
        }
    }

    worker->addTask(task);
}
extern "C"
JNIEXPORT jboolean JNICALL
Java_com_hellorn_core_QPEngineManager_initDB(JNIEnv *env, jclass clazz, jstring path) {
    std::shared_ptr<DBWork> work = std::make_shared<DBWork>();
//    DBWork *work = new DBWork();
    const char *pathStr = env->GetStringUTFChars(path, nullptr);
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
                                                   jint jVersion, jstring jUrl) {
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
                                             "(Ljava/lang/String;ILjava/lang/String;)V");
    if (constructor == nullptr) {
        std::cerr << "Could not find Qp constructor." << std::endl;
        return nullptr;
    }
    jstring jUrl = env->NewStringUTF(qp.url.c_str());
    jstring jHybrideId = env->NewStringUTF(qp.hybrideId.c_str());
    jobject result = env->NewObject(qpClass, constructor, jHybrideId, qp.version, jUrl);
    env->DeleteLocalRef(jUrl);
    return result;
}