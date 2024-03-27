package cn.xdf.ucan.troy.lib.utils;

import android.content.SharedPreferences;

import com.tencent.mmkv.MMKV;

import java.util.Set;

/**
 * @ClassName: MMKVStorage
 * @Description: 暂行替代方案
 * @Author: mauto
 * @Date: 2021/9/28 10:07 上午
 */
public class MMKVStorageUtils {
   private MMKV mMMKV;

   public MMKVStorageUtils(String id) {
      mMMKV = MMKV.mmkvWithID(id);
   }

   public MMKVStorageUtils(String id, int mode) {
      mMMKV = MMKV.mmkvWithID(id, mode);
   }

   /**
    * 存储数据到本地
    *
    * @param key   key
    * @param value value
    */
   public void saveString(String key, String value) {
      mMMKV.encode(key, value);
   }

   /**
    * 从本地获取数据
    *
    * @param key key
    * @return value
    */
   public String getString(String key, String defaultValue) {
      return mMMKV.decodeString(key, defaultValue);
   }

   public void saveInt(String key, int value) {
      mMMKV.encode(key, value);
   }

   public int getInt(String key, int defaultValue) {
      return mMMKV.decodeInt(key, defaultValue);
   }

   public boolean saveSet(String key, Set<String> values) {
       return mMMKV.encode(key, values);
   }

   public Set<String> getSet(String key, Set<String> defaultValue) {
      return mMMKV.getStringSet(key, defaultValue);
   }

   public void saveBoolean(String key, boolean value) {
      mMMKV.encode(key, value);
   }

   public boolean getBoolean(String key, boolean defaultValue) {
      return mMMKV.decodeBool(key, defaultValue);
   }

   /**
    * 从本地删除数据
    *
    * @param key key
    */
   public void deleteString(String key) {
      mMMKV.remove(key).commit();
   }

   /**
    * 情况全部本地数据
    */
   public void clearAll() {
      mMMKV.clearAll();
   }

   public SharedPreferences.Editor edit() {
      return mMMKV;
   }
}
