package cn.xdf.ucan.troy.lib.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import androidx.core.content.ContextCompat;

/**
 * @author hulijia
 * @createDate 2021/8/30
 * @description StatusBarUtils
 */
public class StatusBarUtils {
    private static Boolean mIsTranslucentStatus;
    private static Integer mStatusHeight;

    //由于6.0之后，才可以修改状态栏文字和图标颜色，默认的白色在浅色状态栏背景下会看不清
    //因此，从6.0起才支持沉浸式状态栏
    //都是主线程调用，不需要考虑线程安全
    public static boolean isTranslucentStatus() {
        if (mIsTranslucentStatus == null) {
            mIsTranslucentStatus = Build.VERSION.SDK_INT >= Build.VERSION_CODES.M;
        }
        return mIsTranslucentStatus;
    }

    //修改的只是该Activity对应的状态栏，对其它Activity的状态栏没有影响
    //虽然状态栏是所有应用共用的，但会针对每个Activity设置的属性(也可以采用默认值)分别进行绘制
    public static void setTranslucentStatus(Activity activity, boolean isLightStatus) {
        if (activity == null) {
            return;
        }
        int sdkInt = Build.VERSION.SDK_INT;
        if (sdkInt >= Build.VERSION_CODES.LOLLIPOP) {
            //方法1：利用全屏模式
            Window window = activity.getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN;
            //6.0(API 23)之后，支持修改状态栏文字和图标颜色，默认是白色
            //6.0之前，小米、魅族有专门的修改方法，此处不兼容
            option |= isLightStatus && sdkInt >= Build.VERSION_CODES.M ?
                    View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR : View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
            //option |= View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
            window.getDecorView().setSystemUiVisibility(option);

            //需要把颜色设置透明，否则会呈现系统默认的浅灰色
            window.setStatusBarColor(Color.TRANSPARENT);
            //window.setNavigationBarColor(Color.TRANSPARENT);
        } else if (sdkInt >= Build.VERSION_CODES.KITKAT) {
            //方法2：利用FLAG_TRANSLUCENT_STATUS
            Window window = activity.getWindow();
            //WindowManager.LayoutParams attributes = window.getAttributes();
            //attributes.flags |= WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
            ////attributes.flags |= WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION;
            //window.setAttributes(attributes);

            //直接addFlags即可，无需通过Attributes
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
    }

    public static void setLightStatus(Activity activity, boolean isLightStatus) {
        if (activity == null) {
            return;
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Window window = activity.getWindow();
            //window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            //window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            //必须要有该属性，否则背景色颜色不对
            int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN;
            //6.0(API 23)之后，支持修改状态栏文字和图标颜色，默认是白色
            //6.0之前，小米、魅族有专门的修改方法，此处不兼容
            option |= isLightStatus ?
                    View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR : View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
            //option |= View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
            window.getDecorView().setSystemUiVisibility(option);

            //需要把颜色设置透明，否则会呈现系统默认的浅灰色
            //window.setStatusBarColor(Color.TRANSPARENT);
            //window.setNavigationBarColor(Color.TRANSPARENT);
        } /*else if (sdkInt >= Build.VERSION_CODES.KITKAT) {
            //方法2：利用FLAG_TRANSLUCENT_STATUS
            Window window = activity.getWindow();
            //WindowManager.LayoutParams attributes = window.getAttributes();
            //attributes.flags |= WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
            ////attributes.flags |= WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION;
            //window.setAttributes(attributes);

            //直接addFlags即可，无需通过Attributes
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }*/
    }

    public static void setStatusColor(Activity activity, int colorResId) {
        if (activity == null) {
            return;
        }
        int sdkInt = Build.VERSION.SDK_INT;
        if (sdkInt >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = activity.getWindow();
            window.setStatusBarColor(ContextCompat.getColor(activity, colorResId));
            //window.setNavigationBarColor(ContextCompat.getColor(activity, colorResId));
        }/* else if (sdkInt >= Build.VERSION_CODES.KITKAT) {
            //使用SystemBarTint库使4.4版本状态栏变色，需要先将状态栏设置为透明
            //SystemBarTintManager来自于开源库https://github.com/jgilfelt/SystemBarTint
            setTranslucentStatus(activity, false);
            SystemBarTintManager systemBarTintManager = new SystemBarTintManager(activity);
            //显示状态栏
            systemBarTintManager.setStatusBarTintEnabled(true);
            systemBarTintManager.setStatusBarTintColor(ContextCompat.getColor(activity, colorResId));
        }*/
    }

    public static void setFitsSystemWindows(Activity activity, boolean fitSystemWindows) {
        if (activity == null) {
            return;
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            ViewGroup contentLayout = (ViewGroup) activity.findViewById(android.R.id.content);
            View parentView = contentLayout.getChildAt(0);
            if (parentView != null) {
                parentView.setFitsSystemWindows(fitSystemWindows);
            }
        }
    }

    public static void setStatusViewHeight(Activity activity, int statusViewId) {
        if (activity == null) {
            return;
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            View statusView = activity.findViewById(statusViewId);
            ViewGroup.LayoutParams layoutParams = statusView.getLayoutParams();
            layoutParams.height = getStatusHeight(activity);
        }
    }

    public static void addStatusView(Activity activity, int colorResId) {
        if (activity == null) {
            return;
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            //ViewGroup contentLayout = (ViewGroup) activity.getWindow().getDecorView();
            ViewGroup contentLayout = (ViewGroup) activity.findViewById(android.R.id.content);
            View statusView = new View(activity);
            ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, getStatusHeight(activity));
            statusView.setBackgroundResource(colorResId);
            //默认为addView(child, -1, params);
            //界面是乱的，第一个view和statusView有重叠
            contentLayout.addView(statusView, layoutParams);
        }
    }

    public static void setStatusViewPaddingTop(Activity activity) {
        if (activity == null) {
            return;
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            View contentLayout = activity.getWindow().getDecorView();
            //View contentLayout = activity.findViewById(android.R.id.content);
            contentLayout.setPadding(0, getStatusHeight(activity), 0, 0);
        }
    }

    public static void setStatusViewMarginTop(Context context,
                                              ViewGroup.MarginLayoutParams marginLayoutParams,
                                              int otherMarginResId) {
        if (context == null || marginLayoutParams == null) {
            return;
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            marginLayoutParams.setMargins(0, getStatusHeight(context)
                            + (int) context.getResources().getDimension(otherMarginResId),
                    0, 0);
        }
    }

    //都是主线程调用，不需要考虑线程安全
    private static int getStatusHeight(Context context) {
        if (mStatusHeight == null) {
            int resourceId = context.getResources().getIdentifier(
                    "status_bar_height", "dimen", "android");
            if (resourceId > 0) {
                mStatusHeight = context.getResources().getDimensionPixelSize(resourceId);
            }
            if (mStatusHeight <= 0) {
                mStatusHeight = DisplayUtils.dp2px(context, 24);
            }
        }
        return mStatusHeight;
    }

}