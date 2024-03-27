package cn.xdf.ucan.troy.lib.utils;

import android.text.TextUtils;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

/**
 * @author hulijia
 * @createDate 2021/8/30
 * @description RC4Utils
 */
public class RC4Utils {
    private static final int S_KEY_LENGTH = 256;
    //StandardCharsets.UTF_8可以直接写，没必要在定义成变量
    private static final Charset CHARSET = StandardCharsets.UTF_8;

    public static byte[] encrypt(String data, String key) {
        if (TextUtils.isEmpty(data) || TextUtils.isEmpty(key)) {
            return null;
        }
        return execute(data.getBytes(CHARSET), key.getBytes(CHARSET));
    }

    public static byte[] encrypt(String data, byte[] key) {
        if (TextUtils.isEmpty(data) || key == null || key.length == 0) {
            return null;
        }
        return execute(data.getBytes(CHARSET), key);
    }

    public static byte[] encrypt(byte[] data, byte[] key) {
        if (data == null || data.length == 0 || key == null || key.length == 0) {
            return null;
        }
        return execute(data, key);
    }

    public static String decrypt2String(byte[] data, String key) {
        if (data == null || data.length == 0 || TextUtils.isEmpty(key)) {
            return null;
        }
        return new String(execute(data, key.getBytes(CHARSET)), CHARSET);
    }

    public static String decrypt2String(byte[] data, byte[] key) {
        if (data == null || data.length == 0 || key == null || key.length == 0) {
            return null;
        }
        return new String(execute(data, key), CHARSET);
    }

    public static byte[] decrypt(byte[] data, byte[] key) {
        if (data == null || data.length == 0 || key == null || key.length == 0) {
            return null;
        }
        return execute(data, key);
    }

    private static byte[] execute(byte[] data, byte[] key) {
        key = initKey(key);
        int x = 0;
        int y = 0;
        int dataLength = data.length;
        byte[] result = new byte[dataLength];
        for (int i = 0; i < dataLength; i++) {
            x = (x + 1) & 0xff;
            y = ((key[x] & 0xff) + y) & 0xff;
            byte tmp = key[x];
            key[x] = key[y];
            key[y] = tmp;
            int xorIndex = ((key[x] & 0xff) + (key[y] & 0xff)) & 0xff;
            result[i] = (byte) (data[i] ^ key[xorIndex]);
        }
        return result;
    }

    private static byte[] initKey(byte[] key) {
        byte[] result = new byte[S_KEY_LENGTH];
        for (int i = 0; i < S_KEY_LENGTH; i++) {
            result[i] = (byte) i;
        }
        int index1 = 0;
        int index2 = 0;
        int keyLength = key.length;
        for (int i = 0; i < S_KEY_LENGTH; i++) {
            index2 = ((key[index1] & 0xff) + (result[i] & 0xff) + index2) & 0xff;
            byte temp = result[i];
            result[i] = result[index2];
            result[index2] = temp;
            index1 = (index1 + 1) % keyLength;
        }
        return result;
    }

}