package cn.xdf.ucan.troy.lib.permission;

import java.util.HashMap;

/**
 * @Description:权限描述配置
 * @author: jingzhenglun@xdf.cn
 * @date: 5/11/21
 */
public class PermissionConfigManager {

    /**
     * 权限描述配置Map
     */
    private static final HashMap<String, PermissionConfigBean> mPermissionDesMap = new HashMap<>();

    /**
     * 业务方初始化权限描述配置
     *
     * @param configBeanHashMap
     */
    public static void initConfig(HashMap<String, PermissionConfigBean> configBeanHashMap) {
        mPermissionDesMap.clear();
        mPermissionDesMap.putAll(configBeanHashMap);
    }

    /**
     * 业务方添加单个权限描述配置
     *
     * @param permission
     * @param permissionConfigBean
     */
    public static void addConfig(String permission, PermissionConfigBean permissionConfigBean) {
        mPermissionDesMap.put(permission, permissionConfigBean);
    }

    /**
     * 返回当前的权限描述配置项
     *
     * @return
     */
    public static HashMap<String, PermissionConfigBean> getConfig() {
        return mPermissionDesMap;
    }

    /**
     * 清空权限配置
     */
    public static void clearConfig() {
        mPermissionDesMap.clear();
    }

}
