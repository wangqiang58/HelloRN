package cn.xdf.ucan.troy.lib.utils;

import android.content.Context;
import android.graphics.Point;
import android.os.Build;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.WindowManager;

/**
 * @author hulijia
 * @createDate 2021/8/30
 * @description DisplayUtils
 */
public class DisplayUtils {

    /*public int dp2px(Context context, float dpValue) {
        float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }*/

    public static int dp2px(Context context, float dpValue) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dpValue,
                context.getResources().getDisplayMetrics());
    }

    public static int px2dp(Context context, float pxValue) {
        float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    /*public static int sp2px(Context context, float spValue) {
        float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }*/

    public static int sp2px(Context context, float spValue) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, spValue,
                context.getResources().getDisplayMetrics());
    }

    public static int px2sp(Context context, float pxValue) {
        float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (pxValue / fontScale + 0.5f);
    }

    public static int getScreenW(Context context) {
        DisplayMetrics dm = new DisplayMetrics();
        dm = context.getResources().getDisplayMetrics();
        return dm.widthPixels;
    }

    public static int getFullScreenWidth(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Point point = new Point();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            //noinspection ConstantConditions
            wm.getDefaultDisplay().getRealSize(point);
        } else {
            //noinspection ConstantConditions
            wm.getDefaultDisplay().getSize(point);
        }
        return point.x;
    }

    public static int getScreenH(Context context) {
        DisplayMetrics dm = new DisplayMetrics();
        dm = context.getResources().getDisplayMetrics();
        return dm.heightPixels;
    }

    /**
     * 获取Bar高度
     *
     * @param context context
     * @param barName 名称
     * @return Bar高度
     */
    public static int getBarHeight(Context context, String barName) {
        // 获得状态栏高度
        int resourceId = context.getResources().getIdentifier(barName, "dimen", "android");
        return context.getResources().getDimensionPixelSize(resourceId);
    }

}