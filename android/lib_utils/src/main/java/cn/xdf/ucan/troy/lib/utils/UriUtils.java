package cn.xdf.ucan.troy.lib.utils;

import android.net.Uri;
import android.text.TextUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * @author hulijia
 * @createDate 2021/8/30
 * @description UriUtils
 */
public class UriUtils {

    public static Uri toUri(String scheme, String authority, String path,
                            Map<String, String> queryParams, String fragment) {
        if (TextUtils.isEmpty(scheme) || TextUtils.isEmpty(authority) || TextUtils.isEmpty(path)) {
            return null;
        }

        Uri.Builder builder = new Uri.Builder()
                .scheme(scheme)
                .authority(authority)
                .path(path);

        addQueryParams(builder, queryParams);

        if (!TextUtils.isEmpty(fragment)) {
            builder.fragment(fragment);
        }

        return builder.build();
    }

    /**
     * 将queryParams解析成map
     *
     * @param uriString uri
     * @return Map<String, String>
     */
    public static Map<String, String> parseQueryParams(String uriString) {
        Uri uri = null;
        if (!TextUtils.isEmpty(uriString)) {
            uri = Uri.parse(uriString);
        }

        return parseQueryParams(uri);
    }

    /**
     * 将queryParams解析成map
     *
     * @param uri uri
     * @return Map<String, String>
     */
    public static Map<String, String> parseQueryParams(Uri uri) {
        HashMap<String, String> result = new HashMap<>(8);
        if (uri == null) {
            return result;
        }

        Set<String> keySet = uri.getQueryParameterNames();
        if (keySet == null) {
            return result;
        }

        for (String key : keySet) {
            if (TextUtils.isEmpty(key)) {
                continue;
            }

            String value = uri.getQueryParameter(key);
            if (TextUtils.isEmpty(value)) {
                continue;
            }

            result.put(key, value);
        }

        return result;
    }

    public static String addQueryParams(String uriString, Map<String, String> queryParams) {
        Uri uri = null;
        if (!TextUtils.isEmpty(uriString)) {
            uri = Uri.parse(uriString);
        }

        return addQueryParams(uri, queryParams);
    }

    public static String addQueryParams(Uri uri, Map<String, String> queryParams) {
        if (uri == null) {
            return "";
        }

        Uri.Builder builder = uri.buildUpon();
        addQueryParams(builder, queryParams);
        return builder.build().toString();
    }

    public static void addQueryParams(Uri.Builder builder, Map<String, String> queryParams) {
        if (builder == null || queryParams == null) {
            return;
        }

        for (Map.Entry<String, String> entry : queryParams.entrySet()) {
            String key, value;
            if (entry == null || TextUtils.isEmpty(key = entry.getKey())
                    || TextUtils.isEmpty(value = entry.getValue())) {
                continue;
            }

            builder.appendQueryParameter(key, value);
        }
    }

}