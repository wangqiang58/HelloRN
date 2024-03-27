package cn.xdf.ucan.troy.lib.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * @author hulijia
 * @createDate 2021/8/30
 * @description NetWorkUtils
 */
public class NetWorkUtils {

    public static boolean isNetworkConnected(Context context) {
        boolean isNetworkConnected = false;
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getApplicationContext().getSystemService(
                Context.CONNECTIVITY_SERVICE);
        if (connectivityManager != null) {
            @SuppressLint("MissingPermission")
            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
            isNetworkConnected = networkInfo != null && networkInfo.isAvailable();
        }
        return isNetworkConnected;
    }

    public static int getNetworkType(Context context) {
        int networkType = -1;
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(
                Context.CONNECTIVITY_SERVICE);
        if (connectivityManager != null) {
            @SuppressLint("MissingPermission")
            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
            if (networkInfo != null && networkInfo.isAvailable()) {
                networkType = networkInfo.getType();
            }
        }
        return networkType;
    }

    public static boolean isWifiNetworkType(Context context) {
        int networkType = getNetworkType(context);
        return networkType == ConnectivityManager.TYPE_WIFI
                || networkType == ConnectivityManager.TYPE_ETHERNET;
    }

}