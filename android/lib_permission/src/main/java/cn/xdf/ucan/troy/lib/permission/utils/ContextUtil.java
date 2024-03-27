package cn.xdf.ucan.troy.lib.permission.utils;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;

import androidx.fragment.app.FragmentActivity;

/**
 * @Description:
 * @author: jingzhenglun@xdf.cn
 * @date: 5/11/21
 */
public class ContextUtil {

    public static Activity getActivity(Context context) {
        while (context instanceof ContextWrapper) {
            if (context instanceof Activity) {
                return (Activity) context;
            }
            context = ((ContextWrapper) context).getBaseContext();
        }
        return null;
    }

    public static FragmentActivity getFragmentActivity(Context context) {
        while (context instanceof ContextWrapper) {
            if (context instanceof FragmentActivity) {
                return (FragmentActivity) context;
            }
            context = ((ContextWrapper) context).getBaseContext();
        }
        return null;
    }

}
