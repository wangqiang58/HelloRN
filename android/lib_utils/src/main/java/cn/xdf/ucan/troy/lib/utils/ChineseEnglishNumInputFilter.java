package cn.xdf.ucan.troy.lib.utils;

import android.text.InputFilter;
import android.text.Spanned;

/**
 * @author hulijia
 * @createDate 2021/10/19
 * @description ChineseEnglishNumInputFilter
 */
public class ChineseEnglishNumInputFilter implements InputFilter {
    //private static final int
    private final int mMaxLength;

    public ChineseEnglishNumInputFilter(int maxLength) {
        mMaxLength = maxLength;
    }

    @Override
    public CharSequence filter(CharSequence source, int start, int end,
                               Spanned dest, int dStart, int dEnd) {
        int dIndex = 0;
        int count = 0;

        while (count <= mMaxLength && dIndex < dest.length()) {
            char c = dest.charAt(dIndex++);
            if (c < 128) {
                count = count + 1;
            } else {
                count = count + 2;
            }
        }

        if (count > mMaxLength) {
            return dest.subSequence(0, dIndex - 1);
        }

        int sindex = 0;
        while (count <= mMaxLength && sindex < source.length()) {
            char c = source.charAt(sindex++);
            if (c < 128) {
                count = count + 1;
            } else {
                count = count + 2;
            }
        }

        if (count > mMaxLength) {
            sindex--;
        }

        return source.subSequence(0, sindex);
    }

}