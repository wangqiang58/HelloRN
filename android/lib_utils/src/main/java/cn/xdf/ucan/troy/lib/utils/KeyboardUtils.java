package cn.xdf.ucan.troy.lib.utils;

import android.app.Activity;
import android.content.Context;
import android.os.IBinder;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import androidx.annotation.NonNull;

/**
 * @author hulijia
 * @createDate 2021/10/15
 * @description KeyboardUtils
 */
public class KeyboardUtils {

    /**
     * view必须是EditText或者其子类
     * view必须是可以获取焦点的(即view.isFocusable()返回true)，view.setFocusable(true);
     * view当前必须已经获取到焦点(即view.isFocused()返回true)，view.requestFocus();
     * view必须是可见
     * 当前界面必须已经加载完成，不能直接在Activity的onCreate()，onResume()，onAttachedToWindow()中使用，
     * 可以在这些方法中通过post的方式来延迟执行
     *
     * @param editText editText
     */
    public static void showSoftInput(@NonNull EditText editText) {
        InputMethodManager inputMethodManager = (InputMethodManager) editText.getContext()
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        if (inputMethodManager == null) {
            return;
        }

        editText.setFocusable(true);
        editText.setFocusableInTouchMode(true);
        editText.requestFocus();

        //按home按键退出时，软键盘不会消失，体验很差
        //inputMethodManager.showSoftInput(editText, InputMethodManager.SHOW_FORCED);
        inputMethodManager.showSoftInput(editText, InputMethodManager.SHOW_IMPLICIT);
    }

    /**
     * clearFocus/disable就可以了，不需要额外hideSoftInput
     * <p>
     * view可以当前布局中已经存在的任何一个View，如果找不到可以用getWindow().getDecorView()
     *
     * @param activity activity
     */
    public static void hideSoftInput(@NonNull Activity activity) {
        InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(
                Context.INPUT_METHOD_SERVICE);
        if (inputMethodManager == null) {
            return;
        }

        hideSoftInput(inputMethodManager, activity.getWindow().getDecorView().getWindowToken());
    }

    /**
     * clearFocus/disable就可以了，不需要额外hideSoftInput
     * <p>
     * view可以当前布局中已经存在的任何一个View，如果找不到可以用getWindow().getDecorView()
     *
     * @param view view
     */
    public static void hideSoftInput(@NonNull View view) {
        InputMethodManager inputMethodManager = (InputMethodManager) view.getContext()
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        if (inputMethodManager == null) {
            return;
        }

        hideSoftInput(inputMethodManager, view.getWindowToken());
    }

    /**
     * 使用0作为flags总是能够隐藏软键盘
     *
     * @param inputMethodManager inputMethodManager
     * @param windowToken        windowToken
     */
    private static void hideSoftInput(InputMethodManager inputMethodManager, IBinder windowToken) {
        //inputMethodManager.hideSoftInputFromWindow(windowToken, InputMethodManager.HIDE_NOT_ALWAYS);
        inputMethodManager.hideSoftInputFromWindow(windowToken, 0);
    }

}
