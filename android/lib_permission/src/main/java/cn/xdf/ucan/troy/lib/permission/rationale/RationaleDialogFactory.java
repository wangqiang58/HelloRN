package cn.xdf.ucan.troy.lib.permission.rationale;

import androidx.fragment.app.DialogFragment;

import java.util.HashSet;

import cn.xdf.ucan.troy.lib.permission.PermissionConfigBean;

/**
 * @Description:构造申请原因弹窗
 * @author: jingzhenglun@xdf.cn
 * @date: 4/29/21
 */
public class RationaleDialogFactory {

    public static DialogFragment createDialog(HashSet<PermissionConfigBean> des,
                                              ExplainPermissionDialog.OnItemClickListener onItemClickListener) {
        ExplainPermissionDialog explainPermissionDialog = ExplainPermissionDialog.newInstance(des);
        explainPermissionDialog.setOnItemClickListener(onItemClickListener);
        return explainPermissionDialog;
    }
}
