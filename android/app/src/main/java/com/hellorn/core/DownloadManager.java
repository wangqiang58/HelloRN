package com.hellorn.core;

public class DownloadManager {

    static {
        System.loadLibrary("downloadLib");
    }

    public native static boolean initDB(String path);

    public native static boolean insertRecord(String path,String hybridId, int version, String url);

    public native static Qp queryQp(String path,String hybrideId);
    public native static void download(String url, String dest, DownloadCallback callback);
}
