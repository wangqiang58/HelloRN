package cn.xdf.ucan.troy.lib.permission;

import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @Description:
 * @author: jingzhenglun@xdf.cn
 * @date: 4/29/21
 */
public class PermissionFragment extends Fragment {

    private int mRequestCode;
    private IPermissionResultCallback mCallback;
    private String[] mPendingPermissions;
    private boolean mHasCurrentPermissionsRequest;

    /**
     * 申请权限
     */
    public static void requestPermission(FragmentActivity activity, int requestCode, String[] permissions, IPermissionResultCallback callback) {
        PermissionFragment permissionFragment = new PermissionFragment();
        permissionFragment.mRequestCode = requestCode;
        permissionFragment.mPendingPermissions = permissions;
        permissionFragment.mCallback = callback;

        activity.getSupportFragmentManager().beginTransaction().add(permissionFragment, "permission_" + requestCode).commit();
        permissionFragment.mHasCurrentPermissionsRequest = true;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestPermissions(mPendingPermissions, mRequestCode);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        mHasCurrentPermissionsRequest = false;
        if (requestCode != mRequestCode) return;

        if (mCallback != null) {
            boolean[] shouldShowRequestPermissionRationales = getShouldShowRequestPermissionRationales(permissions);
            mCallback.onResult(requestCode, isAllGranted(grantResults), assembleResult(permissions, grantResults, shouldShowRequestPermissionRationales));
        }

        release();
    }

    /**
     * 是否全部授权
     *
     * @param grantResults
     * @return
     */
    private boolean isAllGranted(int[] grantResults) {
        int length = grantResults.length;
        //如果权限结果数组长度为0，默认没有全部授权，否则默认全部授权
        boolean isAllGranted = length != 0;
        //与每个权限的申请结果，最终为tur视为全部授权
        for (int i = 0; i < length; ++i) {
            isAllGranted = isAllGranted & (PackageManager.PERMISSION_GRANTED == grantResults[i]);
        }
        return isAllGranted;
    }

    /**
     * 获取权限是否不允许再次申请
     *
     * @param permissions
     * @return
     */
    private boolean[] getShouldShowRequestPermissionRationales(@NonNull String[] permissions) {
        boolean[] shouldShowRequestPermissionRationales = new boolean[permissions.length];
        for (int i = 0; i < permissions.length; i++) {
            shouldShowRequestPermissionRationales[i] = shouldShowRequestPermissionRationale(permissions[i]);
        }
        return shouldShowRequestPermissionRationales;
    }

    /**
     * 组装权限申请结果
     *
     * @param permissions
     * @param grantResults
     * @param shouldShowRequestPermissionRationales
     * @return
     */
    private List<Permission> assembleResult(String[] permissions, int[] grantResults, boolean[] shouldShowRequestPermissionRationales) {
        int size = permissions.length;
        if (size == 0) {
            return Collections.emptyList();
        }

        List<Permission> permissionList = new ArrayList<>(size);
        for (int i = 0; i < size; ++i) {
            boolean granted = grantResults[i] == PackageManager.PERMISSION_GRANTED;
            Permission permission = new Permission(permissions[i], granted, shouldShowRequestPermissionRationales[i]);
            permissionList.add(permission);
        }
        return permissionList;
    }

    /**
     * 权限申请结束后移除使用的Fragment
     */
    private void release() {
        FragmentManager fragmentManager = getFragmentManager();
        if (fragmentManager != null) {
            fragmentManager.beginTransaction().remove(PermissionFragment.this).commit();
        }
    }

    public boolean isHasCurrentPermissionsRequest() {
        return mHasCurrentPermissionsRequest;
    }

}
