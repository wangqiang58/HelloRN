package cn.xdf.ucan.troy.lib.xdf.permission;

import android.app.Dialog;
import android.content.Context;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.NonNull;

import cn.xdf.ucan.troy.lib.utils.LogUtils;

/**
 * @author hulijia
 * @createDate 2021/10/13
 * @description BaseDialog
 */
public abstract class BaseDialog extends Dialog {
    private static final String TAG = "BaseDialog-";

    public BaseDialog(@NonNull Context context, int gravity) {
        this(context, gravity, true);
    }

    public BaseDialog(@NonNull Context context, int gravity, boolean isCancelable) {
        super(context, R.style.widget_Dialog_Full_Screen);
        init(gravity, isCancelable);
    }

    public BaseDialog(@NonNull Context context, int gravity, boolean isCancelable, int themeResId) {
        super(context, themeResId);
        init(gravity, isCancelable);
    }

    private void init(int gravity, boolean isCancelable) {
        setCancelable(isCancelable);
        setCanceledOnTouchOutside(isCancelable);

        initAttributes(gravity);
    }

    private void initAttributes(int gravity) {
        Window window = getWindow();
        if (window == null) {
            LogUtils.e(TAG, "window null");
            return;
        }

        WindowManager.LayoutParams layoutParams = window.getAttributes();
        layoutParams.width = WindowManager.LayoutParams.WRAP_CONTENT;
        layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
        layoutParams.gravity = gravity;
        window.setAttributes(layoutParams);
    }

}