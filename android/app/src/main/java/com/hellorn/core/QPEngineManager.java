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
    static String CACHE_DOWNLOAD_DIR;

    static String UN_ZIP_DIR;

    static {
        System.loadLibrary("qpLib");
    }

    public static void init(Context context) {
        DB_PATH = context.getFilesDir() + "/rn.db";
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


    public static void download(String updateUrl, String hybridId, int version, String md5, DownloadCallback callback) {
        DownloadWorker worker = new DownloadWorker(updateUrl, CACHE_DOWNLOAD_DIR, UN_ZIP_DIR, hybridId, version, DB_PATH, md5, callback);
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
