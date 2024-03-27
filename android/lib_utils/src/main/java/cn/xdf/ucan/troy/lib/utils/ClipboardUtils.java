package cn.xdf.ucan.troy.lib.utils;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

/**
 * @author hulijia
 * @createDate 2021/8/30
 * @description ClipboardUtils
 */
public class ClipboardUtils {
    private static final String TAG = "ClipboardUtils-";

    public static boolean setClipData(Context context, String text) {
        if (context == null || TextUtils.isEmpty(text)) {
            Log.e(TAG, "setClipData params null");
            return false;
        }

        try {
            ClipboardManager clipboardManager = (ClipboardManager) context.getSystemService(
                    Context.CLIPBOARD_SERVICE);
            ClipData clipData = ClipData.newPlainText("Label", text);
            clipboardManager.setPrimaryClip(clipData);
            return true;
        } catch (Exception e) {
            Log.e(TAG, "setClipData exception: " + e.getMessage());
            return false;
        }
    }

    public static String getClipData(final Context context) {
        String result = null;
        try {
            ClipboardManager clipboardManager = (ClipboardManager) context.getSystemService(
                    Context.CLIPBOARD_SERVICE);
            ClipData clipData = clipboardManager.getPrimaryClip();
            ClipData.Item item = clipData.getItemAt(0);
            result = item.getText().toString();
        } catch (Exception e) {
            Log.e(TAG, "getClipData exception: " + e.getMessage());
            e.printStackTrace();
        }

        return result == null ? "" : result;
    }

}