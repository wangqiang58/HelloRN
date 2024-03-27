package cn.xdf.ucan.troy.lib.utils;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.drawable.Drawable;
import android.os.Process;
import android.text.TextUtils;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * @author hulijia
 * @createDate 2021/8/30
 * @description PackageUtils
 */
public class PackageUtils {
    private static final String TAG = "PackageUtils-";

    /**
     * 该方法只会获取自己应用相关的RunningAppProcessInfo，效率很高
     *
     * @param context 上下文
     * @return 进程名称
     */
    public static String getCurrentProcessName(Context context) {
        if (context == null) {
            return null;
        }

        String result = null;
        ActivityManager activityManager = (ActivityManager) context.getSystemService(
                Context.ACTIVITY_SERVICE);
        if (activityManager != null) {
            List<ActivityManager.RunningAppProcessInfo> appProcessInfoList = null;
            try {
                appProcessInfoList = activityManager.getRunningAppProcesses();
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (appProcessInfoList != null) {
                int currentPid = Process.myPid();
                for (ActivityManager.RunningAppProcessInfo appProcessInfo : appProcessInfoList) {
                    if (appProcessInfo.pid == currentPid) {
                        result = appProcessInfo.processName;
                        break;
                    }
                }
            }
        }

        return result;
    }

    public static boolean isCurrentMainProcess(Context context) {
        String processName = getCurrentProcessName(context);
        Log.d(TAG, "processName: " + processName);
        return TextUtils.equals(processName, getPackageName(context));
    }

    public static boolean isAppInstalled(Context context, String pkgName) {
        boolean isAppExist = false;
        if (!TextUtils.isEmpty(pkgName)) {
            try {
                context.getPackageManager().getApplicationInfo(pkgName, 0);
                isAppExist = true;
            } catch (PackageManager.NameNotFoundException e) {
            }
        }
        return isAppExist;
    }

   /* public static boolean isAppInstalled(Context context, String pkgName) {
        boolean app_installed = false;
        try {
            PackageManager pm = context.getPackageManager();
            pm.getPackageInfo(pkgName, PackageManager.GET_ACTIVITIES);
            app_installed = true;
        } catch (PackageManager.NameNotFoundException e) {
            app_installed = false;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return app_installed;
    }*/

    public static void jumpApp(Context context, String pkgName) {
        try {
            Intent intent = context.getPackageManager().getLaunchIntentForPackage(pkgName);
            if (intent == null) {
                return;
            }
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                    | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED
                    | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            context.startActivity(intent);
        } catch (Exception e) {
            Log.e(TAG, "jumpApp Exception: " + e.getMessage());
        }
    }

    public static String getVersionName(Context context) {
        String result = null;
        PackageInfo packageInfo = getPackageInfo(context, context.getPackageName(),
                PackageManager.GET_CONFIGURATIONS);
        if (packageInfo != null) {
            result = packageInfo.versionName;
        }
        return result == null ? "" : result;
    }

    public static int getVersionCode(Context context) {
        int result = 0;
        PackageInfo packageInfo = getPackageInfo(context, context.getPackageName(),
                PackageManager.GET_CONFIGURATIONS);
        if (packageInfo != null) {
            result = packageInfo.versionCode;
        }
        return result;
    }

    public static int getVersionCode(Context context, String pkgName) {
        int result = -1;
        PackageInfo packageInfo = getPackageInfo(context, pkgName,
                PackageManager.GET_CONFIGURATIONS);
        if (packageInfo != null) {
            result = packageInfo.versionCode;
        }
        return result;
    }

    public static boolean isLatestVersion(Context context, String pkgName, int newVersionCode) {
        int versionCode = getVersionCode(context, pkgName);
        return versionCode == -1 || versionCode >= newVersionCode;
    }

    public static PackageInfo getPackageInfo(Context context, String pkgName, int flag) {
        PackageInfo packageInfo = null;
        try {
            packageInfo = context.getPackageManager().getPackageInfo(pkgName, flag);
        } catch (Exception e) {
            Log.e(TAG, "getPackageInfo Exception: " + e.getMessage());
        }
        return packageInfo;
    }

    public static String getPkgNameByApkFile(Context context, String apkFilePath) {
        String result = null;
        try {
            PackageInfo packageInfo = context.getPackageManager().getPackageArchiveInfo(apkFilePath,
                    PackageManager.GET_ACTIVITIES);
            result = packageInfo.packageName;
        } catch (Exception e) {
            Log.e(TAG, "getPkgNameByApkFile Exception: " + e.getMessage());
        }
        return result;
    }

    /*public static List<String> getInstalledApp(Context context) {
        List<String> resultList = new ArrayList<>();
        try {
            List<ApplicationInfo> applicationInfoList = context.getPackageManager().getInstalledApplications(0);
            for (ApplicationInfo applicationInfo : applicationInfoList) {
                if (!isSystemApp(applicationInfo)) {
                    resultList.add(applicationInfo.packageName);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultList;
    }*/

    public static List<String> getInstalledApp(Context context) {
        List<String> resultList = new ArrayList<>();
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);
        try {
            @SuppressLint("QueryPermissionsNeeded")
            List<ResolveInfo> launcherResolveInfoList = context.getPackageManager()
                    .queryIntentActivities(intent, 0);
            for (ResolveInfo resolveInfo : launcherResolveInfoList) {
                //if (!isSystemApp(resolveInfo.activityInfo.applicationInfo)) {
                resultList.add(resolveInfo.activityInfo.packageName);
                //}
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultList;
    }

    public static String getPackageName(Context context) {
        if (context == null) {
            return null;
        }

        return context.getPackageName();
    }

    public static String getCurrentAppName(Context context) {
        if (context == null) {
            return null;
        }

        ApplicationInfo applicationInfo = context.getApplicationInfo();
        if (applicationInfo == null) {
            return null;
        }

        return context.getString(applicationInfo.labelRes);
    }

    public static String getAppName(Context context, String pkgName) {
        String result = null;
        try {
            PackageManager packageManager = context.getPackageManager();
            ApplicationInfo applicationInfo = packageManager
                    .getApplicationInfo(pkgName, 0);
            result = (String) applicationInfo.loadLabel(packageManager);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public static Drawable getAppIcon(Context context, String pkgName) {
        Drawable result = null;
        try {
            PackageManager packageManager = context.getPackageManager();
            ApplicationInfo applicationInfo = packageManager
                    .getApplicationInfo(pkgName, 0);
            result = applicationInfo.loadIcon(packageManager);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public static String getMetaData(Context context, String key, String defaultValue) {
        String result = defaultValue;
        try {
            ApplicationInfo applicationInfo = context.getPackageManager()
                    .getApplicationInfo(context.getPackageName(),
                            PackageManager.GET_META_DATA);
            result = applicationInfo.metaData.getString(key, defaultValue);
        } catch (Exception e) {
            Log.e(TAG, "getMetaData: " + e.getMessage());
        }
        return result;
    }

    private static boolean isSystemApp(ApplicationInfo applicationInfo) {
        return (applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) != 0
                || (applicationInfo.flags & ApplicationInfo.FLAG_UPDATED_SYSTEM_APP) != 0;
    }

}