package cn.xdf.ucan.troy.lib.utils;

import android.app.Activity;
import android.content.Context;

import androidx.activity.ComponentActivity;

/**
 * @author hulijia
 * @createDate 2021/9/6
 * @description ActivityConverterUtils
 */
public class ActivityConverterUtils {

    public static ComponentActivity toComponentActivity(Context context) {
        ComponentActivity componentActivity = null;
        if (context instanceof ComponentActivity) {
            componentActivity = (ComponentActivity) context;
        }

        return componentActivity;
    }

    public static Activity toActivity(Context context) {
        Activity activity = null;
        if (context instanceof Activity) {
            activity = (Activity) context;
        }

        return activity;
    }

}