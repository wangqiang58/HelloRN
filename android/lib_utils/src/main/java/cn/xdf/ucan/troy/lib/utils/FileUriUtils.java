package cn.xdf.ucan.troy.lib.utils;

import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.text.TextUtils;
import android.util.Log;

import androidx.core.content.FileProvider;

import java.io.File;

/**
 * @author hulijia
 * @createDate 2021/8/30
 * @description FileUriUtils
 */
public class FileUriUtils {
    private static final String TAG = "FileUriUtils-";

    public static Uri convertFileUri(Context context, String authority, String filePath) {
        if (context == null || TextUtils.isEmpty(authority) || TextUtils.isEmpty(filePath)) {
            Log.e(TAG, "convertFileUri params null");
            return null;
        }

        return convertFileUri(context, authority, new File(filePath));
    }

    public static Uri convertFileUri(Context context, String authority, File file) {
        if (context == null || TextUtils.isEmpty(authority) || file == null || !file.exists()) {
            Log.e(TAG, "convertFileUri params null");
            return null;
        }

        Uri result;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            result = FileProvider.getUriForFile(context, authority, file);
        } else {
            result = Uri.fromFile(file);
        }

        return result;
    }
}