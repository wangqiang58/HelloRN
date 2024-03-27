package cn.xdf.ucan.troy.lib.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * @author hulijia
 * @createDate 2021/8/30
 * @description DateUtils
 */
public class DateUtils {
    public static final String FD_Y_M_D_T_H_M_S = "yyyy-MM-dd'T'HH:mm:ss";
    public static final String FD_Y_M_D_H_M_S = "yyyy-MM-dd HH:mm:ss";
    public static final String FD_Y_M_D_H_M = "yyyy-MM-dd HH:mm";
    public static final String FD_Y_M_D = "yyyy-MM-dd";
    public static final String FD_SEPARATOR_Y_M_D = "yyyy/MM/dd";
    public static final String FD_CN_Y_M_D_H_M = "yyyy年MM月dd日  HH:mm";
    public static final String FD_CN_Y_M_D = "yyyy年MM月dd日";
    public static final String FD_CN_M_D = "MM月dd日";
    public static final String FD_H_M_S = "HH:mm:ss";
    public static final String FD_H_M = "HH:mm";
    public static final String FD_M_D = "MM.dd";
    public static final String FD_M_SPOT_D_H_M = "MM.dd HH:mm";
    public static final String FD_YMD = "yyyyMMdd";

    public static Date parseToDate(String dateStr, String pattern) {
        Date result = null;
        try {
            result = new SimpleDateFormat(pattern, Locale.getDefault()).parse(dateStr);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public static long parseToTime(String dateStr, String pattern) {
        long result = -1L;
        try {
            result = new SimpleDateFormat(pattern, Locale.getDefault()).parse(dateStr).getTime();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public static String format(long milSecond, String pattern) {
        String result = null;
        try {
            //result = (String) DateFormat.format(pattern, milSecond);
            result = new SimpleDateFormat(pattern, Locale.getDefault()).format(new Date(milSecond));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result == null ? "" : result;
    }

    public static String convertFormat(String dateStr, String patternFrom, String patternTo) {
        String result = null;
        try {
            Date date = new SimpleDateFormat(patternFrom, Locale.getDefault()).parse(dateStr);
            result = new SimpleDateFormat(patternTo, Locale.getDefault()).format(date);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result == null ? "" : result;
    }

}