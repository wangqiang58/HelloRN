package cn.xdf.ucan.troy.lib.utils;

import android.text.TextUtils;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * @author hulijia
 * @createDate 2021/9/17
 * @description SignQueryParamsUtils
 */
public class SignQueryParamsUtils {

    public static String sortFormatQueryParams(String jsonString) {
        JSONObject jsonObject = JsonUtils.toJsonObject(jsonString);
        return sortFormatQueryParams(jsonObject);
    }

    public static String sortFormatQueryParams(JSONObject jsonObject) {
        List<Map.Entry<String, String>> list = sortByDictionary(jsonObject);
        return formatQueryParams(list);
    }

    public static String sortFormatQueryParams(Map<String, String> map) {
        List<Map.Entry<String, String>> list = sortByDictionary(map);
        return formatQueryParams(list);
    }

    private static List<Map.Entry<String, String>> sortByDictionary(JSONObject jsonObject) {
        if (jsonObject == null) {
            return null;
        }

        Map<String, String> treeMap = new TreeMap<>(new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                return o1.compareTo(o2);
            }
        });

        for (Iterator<String> iterator = jsonObject.keys(); iterator.hasNext(); ) {
            String key = iterator.next();
            if (TextUtils.isEmpty(key)) {
                continue;
            }

            treeMap.put(key, jsonObject.optString(key));
        }

        return new ArrayList<>(treeMap.entrySet());
    }

    private static List<Map.Entry<String, String>> sortByDictionary(Map<String, String> map) {
        if (map == null || map.isEmpty()) {
            return null;
        }

        List<Map.Entry<String, String>> list = new ArrayList<>(map.entrySet());
        //暂时不考虑o1和o1.getKey()为null的场景
        Collections.sort(list, (o1, o2) -> o1.getKey().compareTo(o2.getKey()));

        return list;
    }

    private static String formatQueryParams(List<Map.Entry<String, String>> list) {
        if (list == null || list.isEmpty()) {
            return null;
        }

        StringBuilder stringBuilder = new StringBuilder();
        int length = list.size();
        for (int i = 0; i < length; i++) {
            String key;
            Map.Entry<String, String> entry = list.get(i);
            if (entry == null || TextUtils.isEmpty((key = entry.getKey()))) {
                continue;
            }

            stringBuilder
                    .append(key)
                    .append("=")
                    .append(entry.getValue());

            if (i != length - 1) {
                stringBuilder.append("=");
            }
        }

        return stringBuilder.toString();
    }

}