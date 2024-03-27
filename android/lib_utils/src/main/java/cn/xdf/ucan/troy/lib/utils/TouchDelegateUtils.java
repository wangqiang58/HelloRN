package cn.xdf.ucan.troy.lib.utils;

import android.graphics.Rect;
import android.view.TouchDelegate;
import android.view.View;

/**
 * @author hulijia
 * @createDate 2021/8/30
 * @description TouchDelegateUtils
 */
public class TouchDelegateUtils {

    /**
     * 扩展区域不会超过父控件的区域
     *
     * @param view  view
     * @param value 四个边距，单位为px
     */
    public static void expandViewTouchDelegate(View view, int value) {
        expandViewTouchDelegate(view, value, value, value, value);
    }

    /**
     * 扩展区域不会超过父控件的区域
     *
     * @param view   view
     * @param left   左边距，单位为px
     * @param right  右边距，单位为px
     * @param top    上边距，单位为px
     * @param bottom 下边距，单位为px
     */
    public static void expandViewTouchDelegate(View view, int left, int right,
                                               int top, int bottom) {
        if (view == null) {
            return;
        }

        view.post(() -> {
            if (view.getParent() instanceof View) {
                Rect bounds = new Rect();
                view.getHitRect(bounds);
                bounds.top -= top;
                bounds.bottom += bottom;
                bounds.left -= left;
                bounds.right += right;
                TouchDelegate touchDelegate = new TouchDelegate(bounds, view);
                ((View) view.getParent()).setTouchDelegate(touchDelegate);
            }
        });
    }

    public static void resetViewTouchDelegate(View view) {
        if (view == null) {
            return;
        }

        view.post(() -> {
            if (view.getParent() instanceof View) {
                Rect bounds = new Rect();
                bounds.setEmpty();
                TouchDelegate touchDelegate = new TouchDelegate(bounds, view);
                ((View) view.getParent()).setTouchDelegate(touchDelegate);
            }
        });
    }

}