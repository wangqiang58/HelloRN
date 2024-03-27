package cn.xdf.ucan.troy.lib.utils;

import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.lang.reflect.Type;
import java.util.Collection;
import java.util.Map;

/**
 * @author hulijia
 * @createDate 2021/8/30
 * @description JsonUtils
 */
public class JsonUtils {

    public static <T> T fromJson(String jsonString, Class<T> clazz) {
        T result = null;
        try {
            result = new Gson().fromJson(jsonString, clazz);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }

    public static <T> T fromJson(String jsonString, Type type) {
        T result = null;
        try {
            result = new Gson().fromJson(jsonString, type);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }

    public static String toJsonString(Object obj) {
        String result = null;
        try {
            result = new Gson().toJson(obj);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }

    public static JSONObject toJsonObject(String jsonString) {
        JSONObject result = null;
        try {
            result = new JSONObject(jsonString);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }

    public static Map<String, String> toMap(Object obj) {
        Map<String, String> result = null;
        try {
            String jsonString = toJsonString(obj);
            result = fromJson(jsonString, new TypeToken<Map<String, String>>() {
            }.getType());
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }

    public static JSONArray removeEscape(Collection<String> jsonCollection) {
        if (jsonCollection == null || jsonCollection.isEmpty()) {
            return null;
        }

        JSONArray jsonArray = new JSONArray();
        for (String jsonString : jsonCollection) {
            JSONObject jsonObject = removeEscape(jsonString);
            if (jsonObject != null) {
                jsonArray.put(jsonObject);
            }
        }

        return jsonArray;
    }

    public static JSONObject removeEscape(String jsonString) {
        if (TextUtils.isEmpty(jsonString)) {
            return null;
        }

        JSONObject jsonObject = null;
        try {
            jsonObject = (JSONObject) new JSONTokener(jsonString).nextValue();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return jsonObject;
    }

}