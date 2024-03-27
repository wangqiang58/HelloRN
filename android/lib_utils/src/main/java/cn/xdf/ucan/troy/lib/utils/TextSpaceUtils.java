package cn.xdf.ucan.troy.lib.utils;

import android.text.TextUtils;

/**
 * @author hulijia
 * @createDate 2021/10/13
 * @description TextSpaceUtils
 */
public class TextSpaceUtils {
    private static final char CHAR_SPACE = ' ';

    public static String removeTextSpace(String text) {
        if (TextUtils.isEmpty(text)) {
            return "";
        }

        return text.replaceAll(" ", "");
    }

    public static int countTextSpace(String text, int start, int length) {
        if (TextUtils.isEmpty(text)) {
            return 0;
        }

        int result = 0;
        int step = 0;
        for (int i = start; i < text.length() && step <= length; i++) {
            char item = text.charAt(i);
            if (item == CHAR_SPACE) {
                result++;
            }
            step++;
        }

        return result;
    }

}