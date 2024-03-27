package cn.xdf.ucan.troy.lib.permission;

import java.io.Serializable;
import java.util.Objects;

/**
 * author:fumm
 * Date : 2021/ 03/ 31 3:54 PM
 * Dec : 权限描述类
 **/
public class PermissionConfigBean implements Serializable {


    /**
     * 权限名字
     */
    private String name;

    /**
     * 权限描述
     */
    private String description;

    public PermissionConfigBean(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PermissionConfigBean that = (PermissionConfigBean) o;
        return name.equals(that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
