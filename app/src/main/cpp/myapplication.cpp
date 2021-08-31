#include<jni.h>
#include<string>
#include"base64.h"
#include <iostream>

extern "C"
JNIEXPORT jstring JNICALL
Java_com_test_myapplication_services_SendToDBService_stringFromJNI(JNIEnv *env, jobject thiz,
                                                                   jstring test) {
    const char  *hello = env->GetStringUTFChars(test, NULL);

    std::string encoded = base64_encode(hello ,false);
    return env->NewStringUTF(encoded.c_str());
}
extern "C"
JNIEXPORT jstring JNICALL
Java_com_test_myapplication_MainActivity_stringFromJNI(JNIEnv *env, jobject thiz, jstring test) {
    const char  *hello = env->GetStringUTFChars(test, NULL);

    return env->NewStringUTF(hello);
}