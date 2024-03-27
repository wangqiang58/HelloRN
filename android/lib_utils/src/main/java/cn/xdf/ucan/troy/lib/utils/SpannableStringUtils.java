package cn.xdf.ucan.troy.lib.utils;

import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

/**
 * @author hulijia
 * @createDate 2021/8/30
 * @description SpannableStringUtils
 */
public class SpannableStringUtils {
    private static final int INVALID_PARAM = -1;

    public static SpannableStringBuilder getSpannableStringBuilder(CharSequence text,
                                                                   SpannableStringEntry entry) {
        if (TextUtils.isEmpty(text) || entry == null) {
            return null;
        }

        List<SpannableStringEntry> list = new ArrayList<>();
        list.add(entry);

        return getSpannableStringBuilder(text, list);
    }

    public static SpannableStringBuilder getSpannableStringBuilder(CharSequence text,
                                                                   List<SpannableStringEntry> list) {
        if (TextUtils.isEmpty(text) || list == null || list.isEmpty()) {
            return null;
        }

        SpannableStringBuilder result = new SpannableStringBuilder(text);
        for (SpannableStringEntry entry : list) {
            String keyWords;
            if (entry == null || TextUtils.isEmpty(keyWords = entry.getKeyWords())) {
                continue;
            }

            try {
                Matcher matcher = Pattern.compile(keyWords).matcher(text);
                while (matcher.find()) {
                    int start = matcher.start();
                    int end = matcher.end();

                    //Span对象不能共用
                    result.setSpan(new ForegroundColorSpan(entry.getColor()), start,
                            end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

                    if (entry.getStyle() != INVALID_PARAM) {
                        result.setSpan(new StyleSpan(entry.getStyle()), start, end,
                                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    }

                    if (entry.getSize() != INVALID_PARAM) {
                        result.setSpan(new AbsoluteSizeSpan(entry.getSize()), start, end,
                                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    }

                    if (entry.getClickableSpan() != null) {
                        result.setSpan(entry.getClickableSpan(), start, end,
                                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    }
                }
            } catch (PatternSyntaxException e) {
                e.printStackTrace();
            }
        }

        return result;
    }

    public static class SpannableStringEntry {
        private final String keyWords;
        private final int color;
        private final int style;
        private final int size;
        private final ClickableSpan clickableSpan;

        public SpannableStringEntry(String keyWords, int color) {
            this(keyWords, color, INVALID_PARAM, INVALID_PARAM, null);
        }

        public SpannableStringEntry(String keyWords, int color, int style) {
            this(keyWords, color, style,INVALID_PARAM, null);
        }

        public SpannableStringEntry(String keyWords, int color, int style, int size) {
            this(keyWords, color, style, size, null);
        }

        public SpannableStringEntry(String keyWords, int color, ClickableSpan clickableSpan) {
            this(keyWords, color, INVALID_PARAM, INVALID_PARAM, clickableSpan);
        }

        public SpannableStringEntry(String keyWords, int color, int style, int size,
                                    ClickableSpan clickableSpan) {
            this.keyWords = keyWords;
            this.color = color;
            this.style = style;
            this.size = size;
            this.clickableSpan = clickableSpan;
        }

        public String getKeyWords() {
            return keyWords;
        }

        public int getColor() {
            return color;
        }

        public int getStyle() {
            return style;
        }

        public ClickableSpan getClickableSpan() {
            return clickableSpan;
        }

        public int getSize() {
            return size;
        }
    }

}