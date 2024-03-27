package cn.xdf.ucan.troy.lib.xdf.permission;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.method.LinkMovementMethod;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

/**
 * Created by kdd on 2019-12-10 11:02
 * <p>
 * Desc：提示二次确认框
 */
public class CommonConfirmDialog extends Dialog implements View.OnClickListener {

    private View mViewEmpty;
    private TextView tvContent, tvCancel, tvSure, mTvTitle;
    private int type = -1;


    public CommonConfirmDialog(@NonNull Context context) {
        super(context, R.style.widget_Dialog_Full_Screen);
        initView(context);
    }

    public CommonConfirmDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
        initView(context);
    }

    protected CommonConfirmDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        initView(context);
    }

    private void initView(Context context) {
        View mRootView = LayoutInflater.from(context).inflate(R.layout.widget_common_dialog_confirm, null);

        mTvTitle = mRootView.findViewById(R.id.tv_title);
        tvContent = mRootView.findViewById(R.id.tv_content);
        tvCancel = mRootView.findViewById(R.id.tv_cancel);
        tvSure = mRootView.findViewById(R.id.tv_sure);
        mViewEmpty = mRootView.findViewById(R.id.view_empty);
        tvCancel.setOnClickListener(this);
        tvSure.setOnClickListener(this);

        setContentView(mRootView);
    }

    /**
     * 设置是否可取消
     *
     * @param isCancel
     */
    public void setCanceled(boolean isCancel) {
        setCancelable(false);
        setCanceledOnTouchOutside(isCancel);
    }

    /**
     * 设置标题
     */
    public void setTitle(String title) {
        mTvTitle.setText(title);
        mTvTitle.setVisibility(View.VISIBLE);
    }

    /**
     * 隐藏取消按钮
     */
    public void hideCancelBottom() {
        tvCancel.setVisibility(View.GONE);
        mViewEmpty.setVisibility(View.GONE);
    }

    /**
     * 确认取消按钮
     */
    public void hideSureBottom() {
        tvSure.setVisibility(View.GONE);
        mViewEmpty.setVisibility(View.GONE);
    }

    /**
     * 设置提示语
     *
     * @param content
     */
    public void setContent(String content) {
        tvContent.setText(content);
    }

    /**
     * 设置提示语
     *
     * @param content
     */
    public void setSpannableContent(CharSequence content) {
        tvContent.setText(content);
        //设置了点击事件后请加上这句，不然点击事件不起作用
        tvContent.setMovementMethod(LinkMovementMethod.getInstance());
        //去除highlightClickString选中时的背景色
        tvContent.setHighlightColor(ContextCompat.getColor(getContext(),
                android.R.color.transparent));
    }

    public void setContentGravity(int centerType) {
        tvContent.setGravity(centerType);
    }

    /**
     * 确认按钮是否可点
     *
     * @param isClickable
     */
    public void setSureClickable(boolean isClickable) {
        tvSure.setClickable(isClickable);
    }

    /**
     * 取消按钮是否可点
     *
     * @param isClickable
     */
    public void setCancelClickable(boolean isClickable) {
        tvCancel.setClickable(isClickable);
    }

    /**
     * 设置取消按钮话术
     *
     * @param content
     */
    public void setCancelText(String content) {
        tvCancel.setText(content);
    }

    /**
     * 设置确认按钮话术
     *
     * @param content
     */
    public void setSureText(String content) {
        tvSure.setText(content);
    }

    /**
     * 设置确认文本文字颜色
     *
     * @param color
     */
    public void setSureTextColor(int color) {
        tvSure.setTextColor(color);
    }

    /**
     * 设置确定按钮的背景颜色
     * @param background
     */
    public void setSureBackground(@DrawableRes int background) {
        tvSure.setBackgroundResource(background);
    }

    public void setSureBackground(Drawable drawable) {
        tvSure.setBackground(drawable);
    }

    /**
     * 设置取消文本文字颜色
     *
     * @param color
     */
    public void setCancelTextColor(int color) {
        tvCancel.setTextColor(color);
    }

    /**
     * 设置取消按钮的背景颜色
     * @param background
     */
    public void setCancelBackground(@DrawableRes int background) {
        tvCancel.setBackgroundResource(background);
    }
    /**
     * 隐藏取消按钮
     */
    public void hideCancel() {
        tvCancel.setVisibility(View.GONE);
        mViewEmpty.setVisibility(View.GONE);
    }

    /**
     * 设置内容文字大小
     *
     * @param size
     */
    public void setContextSize(int size) {
        tvContent.setTextSize(TypedValue.COMPLEX_UNIT_SP, size);
    }


    public void setType(int type) {
        this.type = type;
    }

    private OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener {
        /**
         * 点击确认
         *
         * @param type type
         */
        void onSureClick(int type);

        /**
         * 点击取消
         *
         * @param type type
         */
        void onCancelClick(int type);
    }

    @Override
    public void onClick(View view) {
        int vid = view.getId();
        if (vid == R.id.tv_sure) {
            if (onItemClickListener != null) {
                onItemClickListener.onSureClick(this.type);
            }
            dismiss();
        }
        if (vid == R.id.tv_cancel) {
            if (onItemClickListener != null) {
                onItemClickListener.onCancelClick(this.type);
            }
            dismiss();
        }
    }
}
