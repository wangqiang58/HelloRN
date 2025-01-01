package com.hellorn.core;

public class DownloadWorker implements Runnable {
    public long mNativePtr = -1;

    /**
     * 下载地址
     */
    private String updateUrl;
    /**
     * 缓存地址
     */
    private String downloadDir;
    /**
     * 解压地址
     */
    private String unZipDir;

    /**
     * qp 版本
     */
    private int version;

    /**
     * qp id
     */
    private String hybridId;

    private String dbName;

    private String md5;


    private DownloadCallback callback;

    public DownloadWorker(String updateUrl, String downloadDir, String unZipDir,
                          String hybridId, int version,
                          String dbName,
                          String md5,
                          DownloadCallback callback) {
        this.unZipDir = unZipDir;
        this.updateUrl = updateUrl;
        this.downloadDir = downloadDir;
        this.hybridId = hybridId;
        this.version = version;
        this.dbName = dbName;
        this.md5 = md5;
        this.callback = callback;
    }


    @Override
    public void run() {
        boolean result = downloadNative(updateUrl, downloadDir, unZipDir,dbName, hybridId, version,md5);
        callback.onResult(result);
    }

    public native static boolean downloadNative(String updateUrl, String downloadDir, String unzipDir,String dbName, String hybridId, int version,String md5);

}
