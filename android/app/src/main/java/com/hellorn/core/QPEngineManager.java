package com.hellorn.core;

import android.content.Context;
import android.os.Environment;

import com.hellorn.util.FileUtil;

import java.io.File;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class QPEngineManager {

    private static Executor executor = Executors.newCachedThreadPool();

    static String DB_PATH;

    static String DB_NAME = "/rn.db";
    static String CACHE_DOWNLOAD_DIR;

    static String UN_ZIP_DIR;

    static {
        System.loadLibrary("qpLib");
    }

    public static void init(Context context) {
        DB_PATH = context.getFilesDir() + DB_NAME;
        initDB(DB_PATH);
        CACHE_DOWNLOAD_DIR = context.getFilesDir().getAbsolutePath();//getSDCardPath();
        UN_ZIP_DIR = context.getFilesDir().getAbsolutePath();
    }

    public static String getSDCardPath() {
        String sdcardPath = null;
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            File sdcardDir = Environment.getExternalStorageDirectory();
            sdcardPath = sdcardDir.getPath();
        }
        return sdcardPath;
    }


    public static void download(Qp qp, DownloadCallback callback) {
        DownloadWorker worker = new DownloadWorker(qp, callback);
        //executor.execute(worker);
        worker.startTask();
    }

    public static String hasCache(Context context, String hybridId) {
        Qp qp = QPEngineManager.queryQp(DB_PATH, hybridId);
        if (qp != null) {
            return FileUtil.findJSBundleFiles(qp.updateUrl, ".bundle");
        }
        return null;
    }


    private native static boolean initDB(String db);

    public native static boolean insertRecord(String path, String hybridId, int version, String url);

    public native static Qp queryQp(String path, String hybrideId);

}
