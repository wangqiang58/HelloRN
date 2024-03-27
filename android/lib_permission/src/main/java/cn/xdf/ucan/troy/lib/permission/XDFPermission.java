package cn.xdf.ucan.troy.lib.permission;

import android.content.Context;

import androidx.fragment.app.Fragment;

/**
 * @Description: 业务方发起权限请求
 * @author: jingzhenglun@xdf.cn
 * @date: 5/11/21
 */
public class XDFPermission {

    /**
     * 在Activity中发起权限请求
     *
     * @param context
     * @param requestCode
     * @param permissions
     */
    public static void requestPermissions(Context context, int requestCode, String... permissions) {
        PermissionRequestUtils.getInstance().requestPermission(context, requestCode, permissions);
    }

    /**
     * 在Fragment中发起权限请求
     *
     * @param fragment
     * @param requestCode
     * @param permissionsrequestPermissions
     */
    public static void requestPermissions(Fragment fragment, int requestCode, String... permissions) {
        PermissionRequestUtils.getInstance().requestPermission(fragment, requestCode, permissions);
    }

    /**
     * 在任何可以获取到FragmentActivity的地方发起请求，并直接处理回调
     *
     * @param context
     * @param requestCode
     * @param permissions
     * @param callback
     */
    public static void requestPermissions(Context context, int requestCode, String[] permissions,
                                          IPermissionResultCallBack2 callback) {
        PermissionRequestUtils.getInstance().requestPermission(context, requestCode, permissions, callback);
    }
}
