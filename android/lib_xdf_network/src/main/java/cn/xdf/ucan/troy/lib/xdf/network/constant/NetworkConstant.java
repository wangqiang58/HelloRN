package cn.xdf.ucan.troy.lib.xdf.network.constant;

import androidx.annotation.StringDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import okhttp3.MediaType;

/**
 * @author hulijia
 * @createDate 2021/9/6
 * @description NetworkConstant
 */
public class NetworkConstant {

    public static class Common {
        public static final MediaType MEDIA_TYPE = MediaType.parse("application/json; charset=utf-8");

        /**
         * 字符串类型参数解析为数字类型时的默认值
         * 与服务端返回的-1值区分开
         */
        public static final int INVALID_DATA = -2;
        public static final String INVALID_DATA_STR = "-2";
    }

    /**
     * status type
     */
    @Retention(RetentionPolicy.SOURCE)
    @StringDef({STATUS_TYPE.ERROR_CODE, STATUS_TYPE.RESPONSE_NULL, STATUS_TYPE.ERROR_CODE,
            STATUS_TYPE.OK, STATUS_TYPE.ERROR_CHECK, STATUS_TYPE.ERROR_LOGIN,
            STATUS_TYPE.ERROR_PERMISSION, STATUS_TYPE.ERROR_SIGN, STATUS_TYPE.ERROR_SYSTEM_MAINTENANCE})
    public @interface STATUS_TYPE {
        /**
         * 网络不可用，该值不是服务端返回的
         */
        String NETWORK_DISABLE = "-22";

        /**
         * response为null，无法获取status参数，该值不是服务端返回的
         */
        String RESPONSE_NULL = "-23";

        /**
         * 代码异常
         */
        String ERROR_CODE = "0";

        /**
         * 成功
         */
        String OK = "1";

        /**
         * 业务拦截，业务验证不通过
         */
        String ERROR_CHECK = "2";

        /**
         * 登陆异常：token过期等
         */
        String ERROR_LOGIN = "3";

        /**
         * 未授权，接口无权限，数据无权限
         */
        String ERROR_PERMISSION = "4";

        /**
         * 验签不通过
         */
        String ERROR_SIGN = "5";

        /**
         * 登陆异常：被踢
         */
        String ERROR_LOGIN_KICKED = "6";

        /**
         * 系统维护
         */
        String ERROR_SYSTEM_MAINTENANCE = "8";
    }

}
