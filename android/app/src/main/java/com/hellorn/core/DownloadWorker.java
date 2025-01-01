package com.hellorn.core;

public class DownloadWorker implements Runnable {
    public long mNativePtr = -1;

    private Qp qp;


    private DownloadCallback callback;

    public DownloadWorker(Qp qp, DownloadCallback callback) {
        this.qp = qp;
        this.callback = callback;
    }


    @Override
    public void run() {
        boolean result = downloadNative(qp.updateUrl, QPEngineManager.CACHE_DOWNLOAD_DIR, QPEngineManager.UN_ZIP_DIR, QPEngineManager.DB_PATH,
                qp.hybrideId, qp.version, qp.md5);
        callback.onResult(result);
    }

    public native static boolean downloadNative(String updateUrl, String downloadDir, String unzipDir, String dbName, String hybridId, int version, String md5);

}
