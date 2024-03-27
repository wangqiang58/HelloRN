package cn.xdf.ucan.troy.lib.utils;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.text.TextUtils;
import android.util.Log;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

/**
 * @author hulijia
 * @createDate 2021/8/30
 * @description PermissionUtils
 */
public class PermissionUtils {
    private static final String TAG = "PermissionUtils-";

    public static boolean checkPermission(Context activity, String permission) {
        if (activity == null || TextUtils.isEmpty(permission)) {
            Log.e(TAG, "checkPermission params null");
            return false;
        }

        return ContextCompat.checkSelfPermission(activity, permission)
                == PackageManager.PERMISSION_GRANTED;
    }


    public static void requestPermission(Activity activity, String permission, int requestCode) {
        if (activity == null || TextUtils.isEmpty(permission)) {
            Log.e(TAG, "requestPermission params null");
            return;
        }

        ActivityCompat.requestPermissions(activity,new String[]{permission}, requestCode);
    }

    /**
     * shouldShowRequestPermissionRationale
     * 无法区分拒绝(Don't ask again)和询问，因此，无法匹配出区分拒绝(Don't ask again)状态
     *
     * @param activity   activity
     * @param permission permission
     * @return 见下
     * 允许                                     false
     * 拒绝(Don't ask again)                    false
     * 询问
     * 第一次询问                                false
     * 拒绝过，但不是Don't ask again              true
     */
    public static boolean shouldRequestPermission(Activity activity, String permission) {
        if (activity == null || TextUtils.isEmpty(permission)) {
            Log.e(TAG, "shouldRequestPermission params null");
            return false;
        }

        return ActivityCompat.shouldShowRequestPermissionRationale(activity, permission);
    }

    public static boolean checkAndRequestPermission(Activity activity, String permission,
                                                    int requestCode) {
        if (activity == null || TextUtils.isEmpty(permission)) {
            Log.e(TAG, "checkAndRequestPermission params null");
            return false;
        }

        boolean hasPermission = checkPermission(activity, permission);
        if (!hasPermission) {
            requestPermission(activity, permission, requestCode);
        }
        return hasPermission;
    }

}
