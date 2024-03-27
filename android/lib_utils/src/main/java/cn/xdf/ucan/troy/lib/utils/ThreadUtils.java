package cn.xdf.ucan.troy.lib.utils;

import android.os.Looper;

/**
 * @author hulijia
 * @createDate 2021/8/30
 * @description ThreadUtils
 */
public class ThreadUtils {

    /**
     * 比右边方式效率要高 Looper.getMainLooper().getThread() == Thread.currentThread()
     *
     * @return 当前线程是否是主线程
     */
    public static boolean isMainThread() {
        return Looper.myLooper() == Looper.getMainLooper();
    }

}