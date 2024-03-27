package cn.xdf.ucan.troy.lib.utils;

import android.app.ActivityManager;
import android.content.Context;

/**
 * @author hulijia
 * @createDate 2021/8/30
 * @description MemoryUtils
 */
public class MemoryUtils {

    public static long getAvailMemory(Context context) {
        ActivityManager activityManager = (ActivityManager) context.getSystemService(
                Context.ACTIVITY_SERVICE);
        ActivityManager.MemoryInfo memoryInfo = new ActivityManager.MemoryInfo();
        activityManager.getMemoryInfo(memoryInfo);
        return memoryInfo.availMem;
    }

}