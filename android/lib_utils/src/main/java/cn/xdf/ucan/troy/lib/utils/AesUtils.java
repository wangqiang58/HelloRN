package cn.xdf.ucan.troy.lib.utils;

import android.text.TextUtils;
import android.util.Log;

import java.nio.charset.StandardCharsets;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

/**
 * @author hulijia
 * @createDate 2021/9/22
 * @description AesUtils
 */
public class AesUtils {
    private static final String TAG = "AESUtils-";

    public static byte[] encrypt(String key, String algorithm, String inputString) {
        if (TextUtils.isEmpty(inputString)) {
            Log.e(TAG, "inputString null");
            return null;
        }

        return encrypt(key, algorithm, inputString.getBytes(StandardCharsets.UTF_8));
    }

    public static byte[] encrypt(String key, String algorithm, byte[] inputBytes) {
        return execute(key, algorithm, true, inputBytes);
    }

    public static byte[] decrypt(String key, String algorithm, String inputString) {
        if (TextUtils.isEmpty(inputString)) {
            Log.e(TAG, "inputString null");
            return null;
        }

        return decrypt(key, algorithm, inputString.getBytes(StandardCharsets.UTF_8));
    }

    public static byte[] decrypt(String key, String algorithm, byte[] inputBytes) {
        return execute(key, algorithm, false, inputBytes);
    }

    private static byte[] execute(String key, String algorithm, boolean isEncrypt,
                                  byte[] inputBytes) {
        if (TextUtils.isEmpty(key) || TextUtils.isEmpty(algorithm)
                || inputBytes == null || inputBytes.length == 0) {
            Log.e(TAG, "params null");
            return null;
        }

        byte[] result = null;
        try {
            SecretKeySpec secretKeySpec = new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8),
                    algorithm);
            Cipher cipher = Cipher.getInstance(algorithm);
            cipher.init(isEncrypt ? Cipher.ENCRYPT_MODE : Cipher.DECRYPT_MODE, secretKeySpec);
            result = cipher.doFinal(inputBytes);
        } catch (Exception e) {
            Log.e(TAG, "execute Exception: " + e.getMessage());
        }

        return result;
    }

}