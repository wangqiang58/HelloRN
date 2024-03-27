package cn.xdf.ucan.troy.lib.permission;

import java.util.Objects;

/**
 * @Description:权限类，封装权限名称和授权状态，以及是否允许申请
 * @author: jingzhenglun@xdf.cn
 * @date: 5/10/21
 */
public class Permission {


    public final String mPermission;
    public final boolean mGranted;
    public final boolean mShouldShowRequestPermissionRationale;

    /**
     * @param permission                           权限
     * @param granted                              是否授权
     * @param shouldShowRequestPermissionRationale 是否允许提示申请权限弹窗 false为勾选不再提示
     */
    public Permission(String permission, boolean granted, boolean shouldShowRequestPermissionRationale) {
        this.mPermission = permission;
        this.mGranted = granted;
        this.mShouldShowRequestPermissionRationale = shouldShowRequestPermissionRationale;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Permission that = (Permission) o;
        return mGranted == that.mGranted &&
                mShouldShowRequestPermissionRationale == that.mShouldShowRequestPermissionRationale &&
                Objects.equals(mPermission, that.mPermission);
    }

    @Override
    public int hashCode() {
        return Objects.hash(mPermission, mGranted, mShouldShowRequestPermissionRationale);
    }

    @Override
    public String toString() {
        return "Permission{" +
                "mPermission='" + mPermission + '\'' +
                ", mGranted=" + mGranted +
                ", mShouldShowRequestPermissionRationale=" + mShouldShowRequestPermissionRationale +
                '}';
    }
}
