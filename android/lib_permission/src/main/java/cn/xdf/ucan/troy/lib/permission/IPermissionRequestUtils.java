package cn.xdf.ucan.troy.lib.permission;

import android.content.Context;

import androidx.fragment.app.Fragment;

/**
 * author:fumm
 * Date : 2021/ 03/ 30 4:11 PM
 * Dec : 功能接口
 **/
public interface IPermissionRequestUtils {

    /**
     * 请求权限
     * 回调在Activity
     *
     * @param context    context
     * @param requestCode requestCode
     * @param permissions permissions
     */
    void requestPermission(Context context, int requestCode, String... permissions);

    /**
     * 请求权限
     * 回调在Fragment
     *
     * @param requestCode
     * @param permissions
     */
    void requestPermission(Fragment fragment, int requestCode, String... permissions);

    /**
     * 申请权限
     * 封装回调
     *
     * @param context
     * @param permissions
     */
    void requestPermission(Context context, int requestCode, String[] permissions,
                           IPermissionResultCallback callback);

}
