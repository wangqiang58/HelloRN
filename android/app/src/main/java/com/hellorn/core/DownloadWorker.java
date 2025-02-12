package com.hellorn.core;

public class DownloadWorker {
    private Qp qp;


    private DownloadCallback callback;

    public DownloadWorker(Qp qp, DownloadCallback callback) {
        this.qp = qp;
        this.callback = callback;
    }


    public void startTask() {
        downloadNative(qp.updateUrl, QPEngineManager.CACHE_DOWNLOAD_DIR, QPEngineManager.UN_ZIP_DIR, QPEngineManager.DB_PATH,
                qp.hybrideId, qp.version, qp.md5, callback);

    }

    public native static void downloadNative(String updateUrl, String downloadDir, String unzipDir, String dbName, String hybridId, int version, String md5, DownloadCallback callback);

}
