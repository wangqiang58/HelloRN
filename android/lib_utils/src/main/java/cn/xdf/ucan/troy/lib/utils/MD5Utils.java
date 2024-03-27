package cn.xdf.ucan.troy.lib.utils;

import android.text.TextUtils;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * @author hulijia
 * @createDate 2021/8/30
 * @description MD5Utils
 */
public class MD5Utils {

    // 全局数组
    private final static String[] strDigits = {"0", "1", "2", "3", "4", "5",
            "6", "7", "8", "9", "a", "b", "c", "d", "e", "f"};

    public MD5Utils() {
    }

    // 返回形式为数字跟字符串
    private static String byteToArrayString(byte bByte) {
        int iRet = bByte;
        // System.out.println("iRet="+iRet);
        if (iRet < 0) {
            iRet += 256;
        }
        int iD1 = iRet / 16;
        int iD2 = iRet % 16;
        return strDigits[iD1] + strDigits[iD2];
    }

    // 返回形式只为数字
    private static String byteToNum(byte bByte) {
        int iRet = bByte;
        System.out.println("iRet1=" + iRet);
        if (iRet < 0) {
            iRet += 256;
        }
        return String.valueOf(iRet);
    }

    // 转换字节数组为16进制字串
    private static String byteToString(byte[] bByte) {
        StringBuffer sBuffer = new StringBuffer();
        for (int i = 0; i < bByte.length; i++) {
            sBuffer.append(byteToArrayString(bByte[i]));
        }
        return sBuffer.toString();
    }

    public static String md5LowerCase(String text) {
        if (TextUtils.isEmpty(text)) {
            return null;
        }

        text = md5(text);
        if (!TextUtils.isEmpty(text)) {
            text = text.toLowerCase();
        }

        return text;
    }

    public static String md5UpperCase(String text) {
        if (TextUtils.isEmpty(text)) {
            return null;
        }

        text = md5(text);
        if (!TextUtils.isEmpty(text)) {
            text = text.toUpperCase();
        }

        return text;
    }

    private static String md5(String s) {
        byte[] hash;

        try {

            hash = MessageDigest.getInstance("MD5").digest(s.getBytes("UTF-8"));

        } catch (NoSuchAlgorithmException e) {

            throw new RuntimeException("Huh, MD5 should be supported?", e);

        } catch (UnsupportedEncodingException e) {

            throw new RuntimeException("Huh, UTF-8 should be supported?", e);

        }

        StringBuilder hex = new StringBuilder(hash.length * 2);

        for (byte b : hash) {

            if ((b & 0xFF) < 0x10)
                hex.append("0");

            hex.append(Integer.toHexString(b & 0xFF));

        }

        return hex.toString();
    }

}