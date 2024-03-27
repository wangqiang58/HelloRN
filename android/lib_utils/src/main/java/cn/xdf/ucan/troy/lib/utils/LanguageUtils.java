package cn.xdf.ucan.troy.lib.utils;

import java.util.Locale;

/**
 * @author hulijia
 * @createDate 2021/8/30
 * @description LanguageUtils
 */
public class LanguageUtils {
    private static final String LANGUAGE_ZH = "zh";

    public static boolean isLanguageZh() {
        return Locale.getDefault().getLanguage().contains(LANGUAGE_ZH);
    }
}