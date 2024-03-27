package cn.xdf.ucan.troy.lib.utils

import android.app.Activity
import android.os.Environment
import java.io.File
import java.math.BigDecimal

/**
 *
 * @Description: 缓存工具
 * @Author: ZhangJie
 * @CreateDate: 2021/11/8 2:22 下午
 */
class CacheUtils {

    companion object {
        val util by lazy(LazyThreadSafetyMode.SYNCHRONIZED) {
            CacheUtils()
        }
    }

    /**
     * 获取缓存大小（MB）
     */
    fun getCacheSize(activity: Activity): String {
        val finalSize: String
        var cacheSize = activity.cacheDir.size()
        if (Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED) {
            cacheSize += activity.externalCacheDir?.size()!!
        }
        finalSize = getFormatSize(cacheSize)
        return finalSize
    }

    /**
     * 清除缓存
     */
    fun clearCache(activity: Activity) {
        activity.cacheDir.clearFile()
        if (Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED) {
            activity.externalCacheDir?.clearFile()
        }
    }

    /**
     * 转换格式
     */
    private fun getFormatSize(size: Long): String {
        if (size <= 0) {
            return "0.00 KB"
        }
        val kSize = size / 1024f
        if (kSize < 1) {
            val result1 = BigDecimal(kSize.toString())
            return result1.setScale(2, BigDecimal.ROUND_HALF_UP)
                .toPlainString() + "KB"
        }
        val mSize = kSize / 1024
        val result1 = BigDecimal(mSize.toString())
        return result1.setScale(2, BigDecimal.ROUND_HALF_UP)
            .toPlainString() + "MB"
    }


    /**
     * 获取文件或者文件夹大小
     */
    fun File.size(): Long {
        var size = 0L
        try {
            if (isFile) {
                size += length()
            }
            if (isDirectory) {
                listFiles()?.forEach {
                    size += it.size()
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return size
    }

    /**
     * 删除文件夹或文件
     */
    private fun File.clearFile() {
        if (isFile) delete()
        if (isDirectory) listFiles()?.forEach { it.clearFile() }
    }
}