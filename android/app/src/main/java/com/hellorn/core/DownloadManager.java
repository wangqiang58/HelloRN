package com.hellorn.core;

public class DownloadManager {

    static {
        System.loadLibrary("downloadLib");
    }

    public native static boolean initDB(String path);
    public native static void download(String url, String dest, DownloadCallback callback);
}
