package cn.xdf.ucan.troy.lib.utils

import android.app.Activity
import android.content.pm.ActivityInfo
import android.provider.Settings
import android.view.OrientationEventListener
import android.view.Surface

/**
 *
 * @Description: 屏幕旋转工具
 * @Author: ZhangJie
 * @CreateDate: 2022/2/8 5:03 下午
 */
class OrientationUtils {

    private var mOrientationEventListener: OrientationEventListener? = null

    fun initOrientation(activity: Activity) {

        //当前屏幕方向
        var rotation = activity.windowManager.defaultDisplay.rotation
        //当前系统旋转状态 1开启自动旋转 0禁用系统旋转
        var rotationStatus = Settings.System.getInt(
            activity.contentResolver,
            Settings.System.ACCELEROMETER_ROTATION,
            0
        )

        /*---初始屏幕方向设置---*/
        //所有Activity必须默认是sensorLandscape(根据重力感应传感器去切换正反横屏)
        // 当系统自动旋转禁用时，判断当前屏幕方向
        //如果是Surface.ROTATION_90，表示是正向横屏，设置为SCREEN_ORIENTATION_LANDSCAPE
        //如果是Surface.ROTATION_270，表示是反向横屏，设置为SCREEN_ORIENTATION_REVERSE_LANDSCAPE
        if (rotationStatus == 0) {
            when (rotation) {
                Surface.ROTATION_90 -> activity.requestedOrientation =
                    ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
                Surface.ROTATION_270 -> activity.requestedOrientation =
                    ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE
            }
        }

        /*---监听重力传感器设置屏幕方向---*/
        mOrientationEventListener = object : OrientationEventListener(activity) {
            override fun onOrientationChanged(orientation: Int) {

                rotation = activity.windowManager.defaultDisplay.rotation
                rotationStatus = Settings.System.getInt(
                    activity.contentResolver,
                    Settings.System.ACCELEROMETER_ROTATION,
                    0
                )

                //系统自动旋转禁用时，使用之前的设置，不再判断重力感应
                if (rotationStatus == 0) {
                    return
                }

                //设备躺平，使用之前的设置，不再判断重力感应
                if (orientation == ORIENTATION_UNKNOWN) {
                    return
                }

                //将所有旋转角度归结为四个方向
                val newOrientation: Int = if (orientation > 350 || orientation < 10) {
                    0
                } else if (orientation in 81..99) {
                    90
                } else if (orientation in 171..189) {
                    180
                } else if (orientation in 261..279) {
                    270
                } else {
                    return
                }

                //90度：设置为正向横屏 180度：设置为反=反向横屏，0和180为正反纵向，不改变屏幕方向
                when (newOrientation) {
                    90 -> {
                        activity.requestedOrientation =
                            ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE
                    }
                    270 -> {
                        activity.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
                    }
                }
            }
        }

        mOrientationEventListener?.enable()
    }

    fun onDestroy() {
        if (mOrientationEventListener != null) {
            mOrientationEventListener?.disable()
        }
    }
}