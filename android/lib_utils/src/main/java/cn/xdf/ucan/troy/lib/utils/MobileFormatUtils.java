package cn.xdf.ucan.troy.lib.utils;

import android.text.TextUtils;

import androidx.annotation.NonNull;

import cn.xdf.ucan.troy.lib.utils.LogUtils;

/**
 * @author hulijia
 * @createDate 2021/7/20
 * @description MobileFormatUtils
 */
public class MobileFormatUtils {
    private static final String TAG = "MobileFormatUtils-";
    private static final char CHAR_SPACE = ' ';
    private static final int MOBILE_LENGTH = 11;
    private static final int SMS_LENGTH = 6;
    //private static final String REGEX_PWD = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z]).{8,24}$";
    private static final String REGEX_PWD = "^(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z]{6,16}$";

    /**
     * 11位手机号转换为344格式
     *
     * @param mobile 11位手机号
     * @return 344格式的手机号
     */
    public static String addGapForFinalMobile(String mobile) {
        if (TextUtils.isEmpty(mobile) || mobile.length() != MOBILE_LENGTH) {
            LogUtils.e(TAG, "mobile is invalid " + mobile);
            return mobile;
        }

        String result = null;
        try {
            String regex = "(1\\w{2})(\\w{4})(\\w{4})";
            result = mobile.replaceAll(regex, "$1 $2 $3");
        } catch (Exception e) {
            LogUtils.e(TAG, "addGapForFinalMobile exception " + e.getMessage());
        }

        return result;
    }

    /**
     * 输入中的手机号转换为344格式，不允许输入空格，通过digits限制了输入空格
     *
     * @param mobile 输入的手机号
     * @return 344格式的手机号
     */
    @NonNull
    public static String addGapForEditMobile(CharSequence mobile) {
        if (TextUtils.isEmpty(mobile)) {
            return "";
        }

        StringBuilder resultBuilder = new StringBuilder();
        int textLength = 0;
        for (int i = 0; i < mobile.length(); i++) {
            char item = mobile.charAt(i);

            //如果首位不为1，不允许再输入
            /*if (i == 0 && item != '1') {
                break;
            }*/

            if (item == CHAR_SPACE) {
                continue;
            }

            resultBuilder.append(item);
            textLength++;

            //第3个和第7个位置后面添加空格
            if (textLength == 3 || textLength == 7) {
                resultBuilder.append(CHAR_SPACE);
            }
        }

        //去掉最后的空格(输入只有3位或者7位时)
        return resultBuilder.toString().trim();
    }

    public static boolean checkEditMobileNum(String mobile) {
        //字符个数校验还需要加上两个空格
        return !TextUtils.isEmpty(mobile) && mobile.length() == MOBILE_LENGTH
                && mobile.startsWith("1");
    }

    public static boolean checkEditSmsNum(String sms) {
        //6位验证码
        return !TextUtils.isEmpty(sms) && sms.length() == SMS_LENGTH;
    }

    public static boolean checkEditPwd(String pwd) {
        return !TextUtils.isEmpty(pwd) && pwd.matches(REGEX_PWD);
    }

}