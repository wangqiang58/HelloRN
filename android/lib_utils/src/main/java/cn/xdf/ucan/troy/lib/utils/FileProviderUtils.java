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
 * @createDate 2021/9/2
 * @description FileProviderUtils
 */
public class FileProviderUtils {
    private static final String TAG = "FileProviderUtils-";
    private static final String AUTHORITY_SUFFIX = ".fileprovider";

    public static String getFileProviderAuthority(Context context) {
        if (context == null) {
            Log.e(TAG, "getFileProviderAuthority context null");
            return "";
        }

        return PackageUtils.getPackageName(context) + AUTHORITY_SUFFIX;
    }

    public static Uri getFileProviderUri(Context context, String filePath) {
        if (context == null || TextUtils.isEmpty(filePath)) {
            Log.e(TAG, "getFileProviderUri params null");
            return null;
        }

        return getFileProviderUri(context, new File(filePath));
    }

    public static Uri getFileProviderUri(Context context, File file) {
        if (context == null || file == null) {
            Log.e(TAG, "getFileProviderUri params null");
            return null;
        }

        Uri result;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            result = FileProvider.getUriForFile(context, getFileProviderAuthority(context), file);
        } else {
            result = Uri.fromFile(file);
        }

        return result;
    }

}