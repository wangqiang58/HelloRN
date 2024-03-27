package cn.xdf.ucan.troy.lib.permission;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.util.Log;

import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

import cn.xdf.ucan.troy.lib.permission.rationale.ExplainPermissionDialog;
import cn.xdf.ucan.troy.lib.permission.rationale.RationaleDialogFactory;
import cn.xdf.ucan.troy.lib.permission.utils.ContextUtil;

/**
 * author:fumm
 * Date : 2021/ 03/ 29 1:57 PM
 * Dec : android 权限申请 弹框描述
 * 该工具类 提供权限申请前，对所申请的权限目的 进行描述
 * <p>
 * 功能接口
 * <p>
 * 权限描述弹框
 * <p>
 * 去设置弹框
 **/
class PermissionRequestUtils implements IPermissionRequestUtils {
    public static final String RATIONALE_FRAGMENT_DIALOG = "RationaleFragmentDialog";
    private static final String TAG = PermissionRequestUtils.class.getSimpleName();

    private static PermissionRequestUtils mInstance;

    private PermissionRequestUtils() {
    }

    public static PermissionRequestUtils getInstance() {
        if (mInstance == null) {
            mInstance = new PermissionRequestUtils();
        }
        return mInstance;
    }

    /**
     * 申请权限
     *
     * @param context
     * @param requestCode
     * @param permissions 权限列表
     */
    @Override
    public void requestPermission(final Context context, final int requestCode, final String... permissions) {
        if (context == null) {
            Log.e(TAG, "request permission, but context is null!!!");
            return;
        }

        final FragmentActivity activity = ContextUtil.getFragmentActivity(context);
        if (activity == null) {
            Log.e(TAG, "request permission without FragmentActivity context!!!");
            return;
        }

        List<String> needRequestPermission = getNeedRequestPermission(activity, permissions);
        if (needRequestPermission == null || needRequestPermission.size() == 0) {
            Log.e(TAG, "has none permission to request!!!");
            //直接申请权限
            ActivityCompat.requestPermissions(activity, permissions, requestCode);
            return;
        }
        HashSet<PermissionConfigBean> des = getPermissionDes(needRequestPermission);

        if (des == null || des.isEmpty()) {
            Log.e(TAG, "empty rationale!!!");
            //直接申请权限
            ActivityCompat.requestPermissions(activity, permissions, requestCode);
            return;
        }

        RationaleDialogFactory.createDialog(des, new ExplainPermissionDialog.OnItemClickListener() {
            @Override
            public void OnSureClick() {
                // 同意开始申请权限
                ActivityCompat.requestPermissions(activity, permissions, requestCode);
            }

            @Override
            public void OnCancelClick() {
                // 拒绝开始申请权限
            }
        }).show((activity).getSupportFragmentManager(), RATIONALE_FRAGMENT_DIALOG);
    }


    /**
     * 申请权限
     *
     * @param fragment
     * @param requestCode
     * @param permissions
     */
    @Override
    public void requestPermission(final Fragment fragment, final int requestCode, final String... permissions) {
        if (fragment == null) {
            Log.e(TAG, "fragment is null");
            return;
        }
        List<String> needRequestPermission = getNeedRequestPermission(fragment.getActivity(), permissions);
        if (needRequestPermission == null || needRequestPermission.size() == 0) {
            Log.e(TAG, "has none permission to request");
            //直接申请权限
            fragment.requestPermissions(permissions, requestCode);
            return;
        }
        HashSet<PermissionConfigBean> des = getPermissionDes(needRequestPermission);

        if (des == null || des.isEmpty()) {
            Log.e(TAG, "empty rationale!!!");
            //直接申请权限
            fragment.requestPermissions(permissions, requestCode);
            return;
        }

        RationaleDialogFactory.createDialog(des, new ExplainPermissionDialog.OnItemClickListener() {
            @Override
            public void OnSureClick() {
                // 同意开始申请权限
                fragment.requestPermissions(permissions, requestCode);
            }

            @Override
            public void OnCancelClick() {
                // 拒绝开始申请权限
            }
        }).show(fragment.getActivity().getSupportFragmentManager(), RATIONALE_FRAGMENT_DIALOG);
    }

    /**
     * 申请权限
     *
     * @param context
     * @param requestCode
     * @param permissions
     * @param callback
     */
    @Override
    public void requestPermission(final Context context, final int requestCode, final String[] permissions, final IPermissionResultCallback callback) {

        if (context == null) {
            Log.e(TAG, "context is null!!!");
            return;
        }

        final FragmentActivity activity = ContextUtil.getFragmentActivity(context);
        if (activity == null) {
            Log.e(TAG, "request permission without FragmentActivity context!!!");
            return;
        }

        List<String> needRequestPermission = getNeedRequestPermission(activity, permissions);
        if (needRequestPermission == null || needRequestPermission.size() == 0) {
            Log.e(TAG, "has none permission to request");
            // 直接申请权限
            PermissionFragment.requestPermission(activity, requestCode, permissions, callback);
            return;
        }
        HashSet<PermissionConfigBean> des = getPermissionDes(needRequestPermission);

        if (des == null || des.isEmpty()) {
            Log.e(TAG, "empty rationale!!!");
            // 直接申请权限
            PermissionFragment.requestPermission(activity, requestCode, permissions, callback);
            return;
        }

        RationaleDialogFactory.createDialog(des, new ExplainPermissionDialog.OnItemClickListener() {
            @Override
            public void OnSureClick() {
                // 同意开始申请权限
                PermissionFragment.requestPermission(activity, requestCode, permissions, callback);
            }

            @Override
            public void OnCancelClick() {
                // 拒绝开始申请权限
                if(callback instanceof IPermissionResultCallBack2){
                    ((IPermissionResultCallBack2) callback).onCancel();
                }
            }
        }).show(activity.getSupportFragmentManager(), RATIONALE_FRAGMENT_DIALOG);
    }


    /**
     * 获取去重的权限申请解释文案
     *
     * @param permissions permissions
     * @return 权限提示文案
     */
    private HashSet<PermissionConfigBean> getPermissionDes(List<String> permissions) {
        HashSet<PermissionConfigBean> set = new HashSet<>();
        if (permissions == null || permissions.size() == 0) {
            return set;
        }

        // set 去除重复描述
        for (String permission : permissions) {
            PermissionConfigBean des = PermissionConfigManager.getConfig().get(permission);
            if (des == null) {
                Log.e(TAG, permission + " describe  is not find !!! please init or add your permission config.");
                continue;
            }
            set.add(des);
        }
        if (set.isEmpty()) {
            Log.e(TAG, "all permission describe  is not find !!! please init or add your permission config.");
        }
        return set;
    }

    /**
     * 过滤已授权的权限,已拒绝不再询问的不需要过滤
     *
     * @param activity
     * @param permissions
     * @return
     */
    private List<String> getNeedRequestPermission(Activity activity, String[] permissions) {
        if (permissions == null || permissions.length == 0) {
            return Collections.emptyList();
        }

        List<String> needRequestPermission = new ArrayList<>();
        for (String permission : permissions) {
            int granted = ActivityCompat.checkSelfPermission(activity, permission);
            if (granted != PackageManager.PERMISSION_GRANTED) {
                needRequestPermission.add(permission);
            }
        }
        return needRequestPermission;
    }
}
