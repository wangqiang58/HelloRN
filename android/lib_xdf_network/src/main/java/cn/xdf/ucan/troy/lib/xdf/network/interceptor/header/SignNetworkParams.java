package cn.xdf.ucan.troy.lib.xdf.network.interceptor.header;

import android.text.TextUtils;

import org.json.JSONException;
import org.json.JSONObject;

import cn.xdf.ucan.troy.lib.utils.DeviceUtils;
import cn.xdf.ucan.troy.lib.utils.LogUtils;
import cn.xdf.ucan.troy.lib.utils.MD5Utils;
import cn.xdf.ucan.troy.lib.utils.SignQueryParamsUtils;

/**
 * @author hulijia
 * @createDate 2021/9/17
 * @description SignNetworkParams
 */
public class SignNetworkParams {
    private static final String TAG = "SignNetworkParams-";
    private static final String SALT = "82eb0caabd6c4ed9afdea43f03dea9a5";
    public static final String KEY_ONCE = "once";
    public static final String KEY_TS = "ts";
    public static final String KEY_SIGN = "sign";
    public static final String KEY_APP = "yak";

    public static String sign(String params, boolean isGetType) {
        String once = DeviceUtils.getUuid();
        String ts = String.valueOf(System.currentTimeMillis() / 1000L);

        if (isGetType) {
            params = SignQueryParamsUtils.sortFormatQueryParams(params);
        }

        //params有可能本来就没有，不校验
        String sign = sign(once, ts, params, isGetType);

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.putOpt(KEY_ONCE, once);
            jsonObject.putOpt(KEY_TS, ts);
            jsonObject.putOpt(KEY_SIGN, sign);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return jsonObject.toString();
    }

    public static String sign(String once, String ts, String params, boolean isGetType) {
        //params有可能本来就没有，不校验
        if (TextUtils.isEmpty(once) || TextUtils.isEmpty(ts)) {
            return null;
        }

        StringBuilder stringBuilder = new StringBuilder()
                .append(KEY_ONCE).append("=").append(once).append("=")
                .append(KEY_TS).append("=").append(ts).append("=");

        if (!isGetType) {
            stringBuilder.append("body=");
        }

        if (!TextUtils.isEmpty(params)) {
            stringBuilder.append(params);
        }

        stringBuilder.append(SALT);

        String result = stringBuilder.toString();
        LogUtils.d(TAG, "result: " + result);

        return MD5Utils.md5UpperCase(result);
    }

}