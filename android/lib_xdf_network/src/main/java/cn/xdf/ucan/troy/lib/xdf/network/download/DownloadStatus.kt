package cn.xdf.ucan.troy.lib.xdf.network.download

import java.io.File

sealed class DownloadStatus {

    data class whenDownloading(val progress: Long?) : DownloadStatus()

    data class whenDownloadSuccess(val file: File?) : DownloadStatus()
    data class whenDownloadFailure(val error: Throwable?) : DownloadStatus()

    // 网络异常
    class whenNetError() : DownloadStatus()

    // 下载目标地址异常
    class whenTargetUrlError() : DownloadStatus()

    data class whenDownloadError(val error: Throwable?) : DownloadStatus()
}