package cn.xdf.ucan.troy.lib.utils;

import android.text.TextUtils;
import android.util.Log;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

/**
 * @author hulijia
 * @createDate 2021/8/30
 * @description GzipUtils
 */

public class GzipUtils {
    private static final String TAG = "GzipUtils";
    private static final String CHARSET = "UTF-8";

    public static byte[] compress(String data) {
        if (TextUtils.isEmpty(data)) {
            Log.e(TAG, "data null");
            return null;
        }

        byte[] result = null;
        ByteArrayOutputStream out = null;
        try {
            out = new ByteArrayOutputStream();
            GZIPOutputStream gzip = new GZIPOutputStream(out);
            gzip.write(data.getBytes(CHARSET));
            //必要要有close，flush不行
            gzip.close();
            result = out.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return result;
    }

    public static String uncompress(byte[] data) {
        if (data == null || data.length == 0) {
            Log.e(TAG, "data null");
            return null;
        }

        //String result = null;
        ByteArrayOutputStream out = null;
        ByteArrayInputStream in = null;
        GZIPInputStream gzip = null;
        try {
            out = new ByteArrayOutputStream();
            in = new ByteArrayInputStream(data);
            gzip = new GZIPInputStream(in);
            byte[] buffer = new byte[512];
            int length;
            while ((length = gzip.read(buffer)) != -1) {
                out.write(buffer, 0, length);
            }
            //gzip.read(buffer)处会抛异常java.io.EOFException: Unexpected end of ZLIB input stream
            //所以，该行执行不到，应在try以后再执行
            //result = out.toString(CHARSET);
        } catch (IOException e) {
            //e.printStackTrace();
        } finally {
            try {
                if (gzip != null) {
                    gzip.close();
                }
                if (in != null) {
                    in.close();
                }
                if (out != null) {
                    out.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return out.toString();
    }

    public static boolean isGzip(byte[] data) {
        int header = (data[0] << 8) | data[1] & 0xFF;
        return header == 0x1f8b;
    }

}