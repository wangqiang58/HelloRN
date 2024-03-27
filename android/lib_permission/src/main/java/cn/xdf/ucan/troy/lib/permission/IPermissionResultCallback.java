package cn.xdf.ucan.troy.lib.permission;

import java.util.List;

public interface IPermissionResultCallback {

    /**
     * 权限申请回调透传
     *
     * @param requestCode
     * @param isAllGranted
     * @param permissions
     */
    void onResult(int requestCode, boolean isAllGranted, List<Permission> permissions);
}