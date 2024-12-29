package com.hellorn.core;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class QPEngineManager {

    static Executor executor = Executors.newCachedThreadPool();

    static {
        System.loadLibrary("qpLib");
    }


    public static void download(String url, String dest, String unzip, DownloadCallback callback) {
//        downloadNative(url, dest, unzip, callback);
        DownloadWorker worker = new DownloadWorker(url,dest,unzip,callback);
        executor.execute(worker);
    }


    public native static boolean initDB(String path);

    public native static boolean insertRecord(String path, String hybridId, int version, String url);

    public native static Qp queryQp(String path, String hybrideId);

}
