package cn.xdf.ucan.troy.lib.utils;

import android.content.Context;
import android.content.pm.ApplicationInfo;

/**
 * @author hulijia
 * @createDate 2021/8/30
 * @description DebugUtils
 */
public class DebugUtils {

    public static boolean isDebug(Context context) {
        try {
            ApplicationInfo applicationInfo = context.getApplicationInfo();

            return (applicationInfo.flags & ApplicationInfo.FLAG_DEBUGGABLE) != 0;
        } catch (Exception e) {
            return false;
        }
    }


    public static String BaseUrl;

    public static String InitBaseUrl(String env) {

        switch (env) {
            case "test":
                BaseUrl = "https://uneng.staff.xdf.cn/owl/thursday/";
                break;
            case "prod":
                BaseUrl = "https://talkapi.sikuyunshu.com/aitalk/";
                break;
        }
        return BaseUrl;
    }

}