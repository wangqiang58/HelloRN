package cn.xdf.ucan.troy.lib.xdf.permission;

/**
 * @author zhaoli
 * @date 2021/11/26
 * @description
 */
public abstract class PermissionRequestListener {
    /**
     * 授权成功
     */
    public void requestSuccess(){};

    /**
     * 授权失败
     */
    public void requestFailed(){};


    /**
     * 跳过
     */
    public void onSkip(){};

    /**
     * 取消
     */
    public void onCancel(){};
}
