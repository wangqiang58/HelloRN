package cn.xdf.ucan.troy.lib.utils;

/**
 * @author hulijia
 * @createDate 2021/8/30
 * @description StringParseUtils
 */
public class StringParseUtils {

    public static int parseInt(String str, int def) {
        int result = def;

        try {
            result = Integer.parseInt(str);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }

    public static long parseLong(String str, long def) {
        long result = def;

        try {
            result = Long.parseLong(str);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }

    public static float parseFloat(String str, float def) {
        float result = def;

        try {
            result = Float.parseFloat(str);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }

    public static double parseDouble(String str, double def) {
        double result = def;

        try {
            result = Double.parseDouble(str);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }

    public static boolean parseBoolean(String str, boolean def) {
        boolean result = def;

        try {
            result = Boolean.parseBoolean(str);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }

}