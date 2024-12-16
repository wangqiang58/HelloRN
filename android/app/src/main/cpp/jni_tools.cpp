//
// Created by wangqiangt on 2024/12/8.
//
#include <jni.h>
#include <string>
#include "DownloadWorker.h"
#include <android/log.h>

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
    // 定义日志标签和日志内容
    const char *tag = "Test";
    std::string logMessage = "This is a log message from C++";
    // 使用__android_log_write函数打印日志，日志级别为INFO
    __android_log_write(ANDROID_LOG_INFO, tag, logMessage.c_str());

    jclass callbackClass = getCallbackClass(env);
    std::string data = "我成功拉";
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
    worker->addTask(task);
}