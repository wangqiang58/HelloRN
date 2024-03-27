package cn.xdf.ucan.troy.lib.xdf.permission;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;

import java.util.List;

import cn.xdf.ucan.troy.lib.permission.IPermissionResultCallBack2;
import cn.xdf.ucan.troy.lib.permission.Permission;
import cn.xdf.ucan.troy.lib.permission.XDFPermission;
import cn.xdf.ucan.troy.lib.utils.PermissionPageManagement;

/**
 * @Description: 录音权限申请
 * @Author: ZhangJie
 * @CreateDate: 2021/11/26 2:32 下午
 */
public class PermissionRequestHelper {

    /**
     * 提示框类型 1通用 2测评
     */
    public static final int PERMISSION_DIALOG_NORMAL = 1;
    public static final int PERMISSION_DIALOG_EVA = 2;

    public static final int PERMISSION_DIALOG_NONE = 3;

    /**
     * 申请权限
     *
     * @param context                 上下文
     * @param permissions             要申请的权限
     * @param dialogType              弹窗类型
     * @param enableSkip              能否跳过（只有在dialogType为PERMISSION_DIALOG_EVA有效）
     * @param permissionRejectTitle   权限拒绝时的弹窗标题
     * @param permissionRejectContent 权限拒绝时的弹窗内容
     * @param listener                回调
     */
    public static void requestPermission(Context context, String[] permissions, int dialogType, boolean enableSkip, String permissionRejectTitle, String permissionRejectContent, PermissionRequestListener listener) {
        requestPermission(context, permissions, dialogType, enableSkip, permissionRejectTitle, permissionRejectContent, "", "", listener);
    }


    public static void requestPermission(Context context, String[] permissions, int dialogType, boolean enableSkip, String permissionRejectTitle, String permissionRejectContent, String leftBtnText, String rightBtnText, PermissionRequestListener listener) {
        int code = 10000;
        XDFPermission.requestPermissions(context, code, permissions, new IPermissionResultCallBack2() {
            @Override
            public void onCancel() {
                if (listener != null) {
                    listener.requestFailed();
                }
            }

            @Override
            public void onResult(int requestCode, boolean isAllGranted, List<Permission> permissions) {
                if (code == requestCode && isAllGranted) {
                    //授权成功
                    if (listener != null) {
                        listener.requestSuccess();
                    }
                } else {
                    //授权失败
                    if (listener != null) {
                        listener.requestFailed();
                    }
                    if (dialogType != PERMISSION_DIALOG_NONE) {
                        showPermissionDialog(context, dialogType, enableSkip, permissionRejectTitle, permissionRejectContent, leftBtnText, rightBtnText, listener);
                    }
                }
            }
        });
    }

    public static void showPermissionDialog(Context context, int dialogType, boolean enableSkip, String permissionRejectTitle, String permissionRejectContent, String leftBtnText, String rightBtnText, PermissionRequestListener listener) {
        if (dialogType == PERMISSION_DIALOG_EVA) {
            EvaRecordPermissionConfirmDialog dialog = new EvaRecordPermissionConfirmDialog(context, enableSkip);
            dialog.setTitle(permissionRejectTitle);
            dialog.setLeftBtnText(TextUtils.isEmpty(leftBtnText) ? context.getApplicationContext()
                    .getString(R.string.widget_dialog_eva_permission_confirm_cancel) : leftBtnText);
            dialog.setRightBtnText(TextUtils.isEmpty(rightBtnText) ? context.getApplicationContext()
                    .getString(R.string.widget_dialog_eva_permission_confirm_setting) : rightBtnText);
            dialog.setOnItemClickListener(new EvaRecordPermissionConfirmDialog.OnItemClickListener() {
                @Override
                public void onLeftBtnClicked() {
                    dialog.dismiss();
                    if (listener != null) {
                        listener.onCancel();
                    }
                }

                @Override
                public void onRightBtnClicked() {
                    PermissionPageManagement.goIntentSetting((Activity) context);
                    dialog.dismiss();
                }

                @Override
                public void onSkipClicked() {
                    dialog.dismiss();
                    if (listener != null) {
                        listener.onSkip();
                    }
                }
            });
            dialog.show();
        }

//        else {
//            CommonConfirmDialog confirmDialog = new CommonConfirmDialog(context);
//            confirmDialog.setTitle(permissionRejectTitle);
//            confirmDialog.setContent(permissionRejectContent);
//            confirmDialog.setCancelClickable(true);
//            confirmDialog.setSureText("去开启");
//            confirmDialog.setCancelText("暂不开启");
//            confirmDialog.setOnItemClickListener(new CommonConfirmDialog.OnItemClickListener() {
//                @Override
//                public void onSureClick(int type) {
//                    PermissionPageManagement.goIntentSetting((Activity) context);
//                    confirmDialog.dismiss();
//                }
//
//                @Override
//                public void onCancelClick(int type) {
//                    confirmDialog.dismiss();
//                    if (listener != null) {
//                        listener.onCancel();
//                    }
//                }
//            });
//            confirmDialog.show();
//        }
    }

}
