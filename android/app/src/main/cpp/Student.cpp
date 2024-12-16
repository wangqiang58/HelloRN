//
// Created by wangqiangt on 2024/12/7.
//
#include "Student.h"
#include <jni.h>
#include <string>

int Student::add(int a, int b)
{
    return a + b;
}

extern "C"
JNIEXPORT jint JNICALL
Java_com_hellorn_Student_add(JNIEnv *env, jobject thiz, jint a, jint b) {
    Student* student = new Student();
    return student->add(a,b);
}