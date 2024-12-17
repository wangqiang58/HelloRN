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

// 辅助函数，用于获取CallbackInterface接口的Class对象
static jclass getCallbackClass(JNIEnv *env) {
    return env->FindClass("com/hellorn/core/DownloadCallback");
}

extern "C"
JNIEXPORT void JNICALL
Java_com_hellorn_core_DownloadManager_download(JNIEnv *env, jclass clazz, jstring url, jstring dest,
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
Java_com_hellorn_core_DownloadManager_initDB(JNIEnv *env, jclass clazz, jstring path) {
    //std::shared_ptr<DBWork> work = std::make_shared<DBWork>();
    DBWork *work = new DBWork();
    const char *pathStr = env->GetStringUTFChars(path, nullptr);
    std::string str(pathStr);
    bool result = work->createDatabaseAndTable(str);
    return (jboolean) result;
}