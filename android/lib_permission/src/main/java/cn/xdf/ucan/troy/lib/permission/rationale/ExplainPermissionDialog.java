package cn.xdf.ucan.troy.lib.permission.rationale;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import java.util.HashSet;
import java.util.Iterator;

import cn.xdf.ucan.troy.lib.permission.PermissionConfigBean;
import cn.xdf.ucan.troy.lib.permission.R;

/**
 * author:fumm
 * Date : 2021/ 03/ 30 4:22 PM
 * Dec : 解释申请权限原因弹框
 **/
public class ExplainPermissionDialog extends DialogFragment implements View.OnClickListener {
    public static final String TAG = ExplainPermissionDialog.class.getSimpleName();
    public static final String KEY_DES = "des";
    private View rootView, viewLine;
    private TextView tvCancel, tvSure, mTvTitle;
    private LinearLayout llDesRoot;

    /**
     * 创申请原因弹窗
     *
     * @param des
     * @return
     */
    public static ExplainPermissionDialog newInstance(HashSet<PermissionConfigBean> des) {
        ExplainPermissionDialog explainPermissionDialog = new ExplainPermissionDialog();
        Bundle bundle = new Bundle();
        bundle.putSerializable("des", des);
        explainPermissionDialog.setArguments(bundle);
        return explainPermissionDialog;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        getDialog().setCanceledOnTouchOutside(false);

        rootView = inflater.inflate(R.layout.permission_common_dialog_confirm, container, false);
        mTvTitle = rootView.findViewById(R.id.tv_title);
        llDesRoot = rootView.findViewById(R.id.ll_des_root);
        tvCancel = rootView.findViewById(R.id.tv_cancel);
        tvSure = rootView.findViewById(R.id.tv_sure);
        viewLine = rootView.findViewById(R.id.view_line);
        tvCancel.setOnClickListener(this);
        tvSure.setOnClickListener(this);

        HashSet<PermissionConfigBean> des = (HashSet<PermissionConfigBean>) getArguments().getSerializable(KEY_DES);
        setContent(des);
        return rootView;
    }

    @Override
    public void onResume() {
        Window window = getDialog().getWindow();
        if (window != null) {
            WindowManager.LayoutParams layoutParams = window.getAttributes();
            layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
            window.setAttributes(layoutParams);
        }
        super.onResume();

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
        viewLine.setVisibility(View.GONE);
    }

    /**
     * 确认取消按钮
     */
    public void hideSureBottom() {
        tvSure.setVisibility(View.GONE);
        viewLine.setVisibility(View.GONE);
    }


    /**
     * 设置提示内容
     *
     * @param content 文案
     */
    public void setContent(HashSet<PermissionConfigBean> content) {
        if (content == null || content.isEmpty()) {
            return;
        }
        Iterator<PermissionConfigBean> iterator = content.iterator();

        while (iterator.hasNext()) {
            PermissionConfigBean des = iterator.next();
            addContent(des);
        }
    }

    /**
     * 添加内容描述
     *
     * @param desBean desBean
     */
    private void addContent(PermissionConfigBean desBean) {
        View view = LayoutInflater.from(getContext())
                .inflate(R.layout.include_permission_description, llDesRoot, false);
        TextView title = view.findViewById(R.id.tv_title);
        TextView des = view.findViewById(R.id.tv_des);
        title.setText(desBean.getName());
        des.setText(desBean.getDescription());
        llDesRoot.addView(view);
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
     * 设置取消文本文字颜色
     *
     * @param color
     */
    public void setCancelTextColor(int color) {
        tvCancel.setTextColor(color);
    }

    /**
     * 隐藏取消按钮
     */
    public void hideCancel() {
        tvCancel.setVisibility(View.GONE);
        viewLine.setVisibility(View.GONE);
    }


    private OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener {
        void OnSureClick();

        void OnCancelClick();
    }


    @Override
    public void onClick(View view) {
        int vid = view.getId();
        if (vid == R.id.tv_sure) {
            if (onItemClickListener != null) {
                onItemClickListener.OnSureClick();
            }
            dismiss();
        }
        if (vid == R.id.tv_cancel) {
            if (onItemClickListener != null) {
                onItemClickListener.OnCancelClick();
            }
            dismiss();
        }
    }
}
