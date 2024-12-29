package com.hellorn.core;

import android.content.Context;

import com.hellorn.util.FileUtil;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class QPEngineManager {

    private static Executor executor = Executors.newCachedThreadPool();

    static String DB_PATH;

    static {
        System.loadLibrary("qpLib");
    }

    public static void init(Context context) {
        DB_PATH = context.getFilesDir() + "/rn.db";
        initDB(DB_PATH);
    }


    public static void download(String url, String dest, String unzipDir, String hybridId, int version, DownloadCallback callback) {
        DownloadWorker worker = new DownloadWorker(url, dest, unzipDir, hybridId, version, DB_PATH, callback);
        executor.execute(worker);
    }

    public static String hasCache(Context context, String hybridId) {

        Qp qp = QPEngineManager.queryQp(DB_PATH, hybridId);
        if (qp != null) {
            return FileUtil.findJSBundleFiles(qp.url, ".bundle");
        }
        return null;
    }


    private native static boolean initDB(String db);

    public native static boolean insertRecord(String path, String hybridId, int version, String url);

    public native static Qp queryQp(String path, String hybrideId);

}
