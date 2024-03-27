package cn.xdf.ucan.troy.lib.permission;


/**
 * @Description:
 * @author: jingzhenglun@xdf.cn
 * @date: 5/12/21
 */
public interface IPermissionResultCallBack2 extends IPermissionResultCallback {
    /**
     * 查看申请原因并选择取消
     */
    void onCancel();
}
