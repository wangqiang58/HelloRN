package com.hellorn.core;

public class DownloadWorker implements Runnable {
    public long mNativePtr = -1;

    /**
     * 下载地址
     */
    private String url;
    /**
     * 缓存地址
     */
    private String outputPath;
    /**
     * 解压地址
     */
    private String unzipDest;

    /**
     * qp 版本
     */
    private int version;

    /**
     * qp id
     */
    private String hybridId;

    private String dbName;


    private DownloadCallback callback;

    public DownloadWorker(String url, String outputPath, String unzipDest,
                          String hybridId, int version,
                          String dbName,
                          DownloadCallback callback) {
        this.unzipDest = unzipDest;
        this.url = url;
        this.outputPath = outputPath;
        this.hybridId = hybridId;
        this.version = version;
        this.dbName = dbName;
        this.callback = callback;
    }


    @Override
    public void run() {
        boolean result = downloadNative(url, outputPath, unzipDest,dbName, hybridId, version);
        callback.onResult(result);
    }

    public native static boolean downloadNative(String url, String dest, String unzip,String dbName, String hybridId, int version);

}
