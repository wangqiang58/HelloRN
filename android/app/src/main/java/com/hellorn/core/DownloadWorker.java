package com.hellorn.core;

public class DownloadWorker implements Runnable {
    public long mNativePtr = -1;
    private String url;
    private String outputPath;
    private String unzipDest;

    private DownloadCallback callback;

    public DownloadWorker(String url, String outputPath, String unzipDest, DownloadCallback callback) {
        this.unzipDest = unzipDest;
        this.url = url;
        this.outputPath = outputPath;
        this.callback = callback;
    }


    @Override
    public void run() {
        boolean result = downloadNative(url, outputPath, unzipDest);
        callback.onResult(result);
    }

    public native static boolean downloadNative(String url, String dest, String unzip);

}
