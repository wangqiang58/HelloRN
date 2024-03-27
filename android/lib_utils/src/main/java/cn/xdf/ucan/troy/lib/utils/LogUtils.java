package cn.xdf.ucan.troy.lib.utils;

import android.util.Log;

/**
 * @author hulijia
 * @createDate 2021/8/30
 * @description LogUtils
 */
public class LogUtils {
    private static final boolean IS_LOG = BuildConfig.DEBUG;

    public static void v(String tag, String msg) {
        if (IS_LOG) {
            Log.v(tag, msg);
        }
    }

    public static void d(String tag, String msg) {
        if (IS_LOG) {
            Log.d(tag, msg);
        }
    }

    public static void i(String tag, String msg) {
        if (IS_LOG) {
            Log.i(tag, msg);
        }
    }

    public static void w(String tag, String msg) {
        if (IS_LOG) {
            Log.w(tag, msg);
        }
    }

    public static void e(String tag, String msg) {
        e(tag, msg, null);
    }

    public static void e(String tag, String msg, Throwable throwable) {
        if (IS_LOG) {
            Log.e(tag, msg, throwable);
        }
    }

    public static void wtf(String tag, String msg) {
        wtf(tag, msg, null);
    }

    public static void wtf(String tag, Throwable throwable) {
        wtf(tag, null, throwable);
    }

    public static void wtf(String tag, String msg, Throwable throwable) {
        if (IS_LOG) {
            Log.wtf(tag, msg, throwable);
        }
    }

    public static void wtf(int priority, String tag, String msg) {
        if (IS_LOG) {
            Log.println(priority, tag, msg);
        }
    }

    public static String getStackTraceString() {
        return getStackTraceString(new RuntimeException("LogUtils"));
    }

    public static String getStackTraceString(Throwable throwable) {
        if (!IS_LOG) {
            return null;
        }

        return Log.getStackTraceString(throwable);
    }
}