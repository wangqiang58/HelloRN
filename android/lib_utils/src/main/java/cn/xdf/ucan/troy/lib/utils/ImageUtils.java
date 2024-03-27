package cn.xdf.ucan.troy.lib.utils;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.text.TextUtils;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.FileDescriptor;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

/**
 * @author hulijia
 * @createDate 2021/8/30
 * @description ImageUtils
 */
public class ImageUtils {
    private static final String TAG = "ImageUtils-";
    private static final int URL_TIMEOUT = 8_000;
    private static final int COMPRESS_QUALITY_FULL = 100;
    private static final int COMPRESS_SCALE_NONE = 1;

    public static Bitmap decodeFile(String filePath) {
        return decodeFile(filePath, null);
    }

    public static Bitmap decodeFile(String filePath, BitmapFactory.Options options) {
        if (!FileUtils.isFileExist(filePath)) {
            Log.e(TAG, "decodeFile file not exist: " + filePath);
            return null;
        }

        Bitmap result = null;
        try {
            result = BitmapFactory.decodeFile(filePath, options);
        } catch (Exception e) {
            Log.e(TAG, "decodeFile exception: " + e.getMessage());
        }

        return result;
    }

    public static Bitmap decodeFileDescriptor(FileDescriptor fd) {
        return decodeFileDescriptor(fd, null, null);
    }

    /**
     * 与decodeFile不同的是，该方法直接调用JNI函数进行读取，效率比较高
     *
     * @param fd         fd
     * @param outPadding outPadding
     * @param options    options
     * @return bitmap
     */
    public static Bitmap decodeFileDescriptor(FileDescriptor fd, Rect outPadding,
                                              BitmapFactory.Options options) {
        if (fd == null) {
            Log.e(TAG, "decodeFileDescriptor fd null");
            return null;
        }

        Bitmap result = null;
        try {
            result = BitmapFactory.decodeFileDescriptor(fd, outPadding, options);
        } catch (Exception e) {
            Log.e(TAG, "decodeFileDescriptor exception: " + e.getMessage());
        }

        return result;
    }

    public static Bitmap decodeAssets(Context context, String filePath) {
        if (context == null || TextUtils.isEmpty(filePath)) {
            Log.e(TAG, "decodeAssets params null");
            return null;
        }

        Bitmap result = null;
        try {
            AssetManager assetManager = context.getResources().getAssets();
            InputStream inputStream = assetManager.open(filePath);
            result = decodeStream(inputStream, true);
        } catch (Exception e) {
            Log.e(TAG, "decodeAssets exception: " + e.getMessage());
        }

        return result;
    }

    public static Bitmap decodeStream(InputStream inputStream, boolean isRecycle) {
        return decodeStream(inputStream, null, null, isRecycle);
    }

    public static Bitmap decodeStream(InputStream inputStream, Rect outPadding,
                                      BitmapFactory.Options options, boolean isRecycle) {
        if (inputStream == null) {
            Log.e(TAG, "decodeStream inputStream null");
            return null;
        }

        Bitmap result = null;
        try {
            result = BitmapFactory.decodeStream(inputStream, outPadding, options);
        } catch (Exception e) {
            Log.e(TAG, "decodeStream exception: " + e.getMessage());
        } finally {
            if (isRecycle) {
                FileUtils.closeQuietly(inputStream);
            }
        }

        return result;
    }

    public static Bitmap decodeResource(Context context, int resId) {
        return decodeResource(context, resId, null);
    }

    public static Bitmap decodeResource(Context context, int resId, BitmapFactory.Options options) {
        if (context == null) {
            Log.e(TAG, "decodeResource context null");
            return null;
        }

        Bitmap result = null;
        try {
            result = BitmapFactory.decodeResource(context.getResources(), resId, options);
        } catch (Exception e) {
            Log.e(TAG, "decodeResource exception: " + e.getMessage());
        }

        return result;
    }

    public static Bitmap decodeBytes(byte[] bytes) {
        if (bytes == null || bytes.length == 0) {
            Log.e(TAG, "decodeBytes bytes null");
            return null;
        }

        return decodeBytes(bytes, 0, bytes.length, null);
    }

    public static Bitmap decodeBytes(byte[] bytes, int offset, int length) {
        return decodeBytes(bytes, offset, length, null);
    }

    public static Bitmap decodeBytes(byte[] bytes, BitmapFactory.Options options) {
        if (bytes == null || bytes.length == 0) {
            Log.e(TAG, "decodeBytes bytes null");
            return null;
        }

        return decodeBytes(bytes, 0, bytes.length, options);
    }

    public static Bitmap decodeBytes(byte[] bytes, int offset, int length,
                                     BitmapFactory.Options options) {
        if (bytes == null || bytes.length == 0 || offset < 0 || length <= 0) {
            Log.e(TAG, "decodeBytes params invalid");
            return null;
        }

        Bitmap result = null;
        try {
            result = BitmapFactory.decodeByteArray(bytes, offset, length, options);
        } catch (Exception e) {
            Log.e(TAG, "decodeBytes exception: " + e.getMessage());
        }

        return result;
    }

    public static byte[] urlToBytes(String url, int timeout) {
        if (TextUtils.isEmpty(url)) {
            Log.e(TAG, "urlToBytes url null");
            return null;
        }

        if (timeout <= 0) {
            Log.w(TAG, "urlToBytes timeout invalid");
            timeout = URL_TIMEOUT;
        }

        byte[] result = null;
        try {
            URLConnection urlConnection = new URL(url).openConnection();
            HttpURLConnection httpUrlConnection = (HttpURLConnection) urlConnection;
            httpUrlConnection.setConnectTimeout(timeout);
            httpUrlConnection.setReadTimeout(timeout);
            int responseCode = httpUrlConnection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                result = inputStreamToBytes(httpUrlConnection.getInputStream(), true);
            }
        } catch (Exception e) {
            Log.e(TAG, "urlToBytes exception: " + e.getMessage());
        }

        return result;
    }

    public static byte[] inputStreamToBytes(InputStream inputStream, boolean isRecycle) {
        if (inputStream == null) {
            Log.e(TAG, "inputStreamToBytes inputStream null");
            return null;
        }

        byte[] result = null;
        ByteArrayOutputStream byteArrayOutputStream = null;
        try {
            byteArrayOutputStream = new ByteArrayOutputStream();
            int length;
            while ((length = inputStream.read()) != -1) {
                byteArrayOutputStream.write(length);
            }
            result = byteArrayOutputStream.toByteArray();
        } catch (Exception e) {
            Log.e(TAG, "inputStreamToBytes exception: " + e.getMessage());
        } finally {
            if (isRecycle) {
                FileUtils.closeQuietly(inputStream);
            }

            if (byteArrayOutputStream != null) {
                try {
                    byteArrayOutputStream.close();
                } catch (IOException e) {
                    Log.e(TAG, "inputStreamToBytes exception: " + e.getMessage());
                }
            }
        }

        return result;
    }

    public static byte[] bitmapToBytes(Bitmap bitmap, Bitmap.CompressFormat compressFormat) {
        if (bitmap == null || compressFormat == null) {
            Log.e(TAG, "bitmapToBytes params null");
            return null;
        }

        return ImageUtils.compressQualityToBytes(bitmap, COMPRESS_QUALITY_FULL, compressFormat);
    }

    /**
     * 质量压缩
     * <p>
     * 保持像素的前提下(像素不会减少)，改变图片的位深及透明度
     * 即：通过算法抹掉(同化)图片中的一些相近的像素，达到压缩图片的目的
     * 使用场景：适合长图
     *
     * @param bitmap         bitmap
     * @param quality        0-100, 对于PNG等无损格式的图片，会忽略此项设置
     * @param compressFormat 图片格式
     * @return byte[]
     */
    public static byte[] compressQualityToBytes(Bitmap bitmap, int quality,
                                                Bitmap.CompressFormat compressFormat) {
        if (bitmap == null || compressFormat == null) {
            Log.e(TAG, "compressQualityToBytes params null");
            return null;
        }

        if (quality <= 0 || quality > COMPRESS_QUALITY_FULL) {
            Log.w(TAG, "compressQualityToBytes quality invalid");
            quality = COMPRESS_QUALITY_FULL;
        }

        byte[] result = null;
        ByteArrayOutputStream byteArrayOutputStream = null;
        try {
            byteArrayOutputStream = new ByteArrayOutputStream();
            bitmap.compress(compressFormat, quality, byteArrayOutputStream);
            result = byteArrayOutputStream.toByteArray();
        } catch (Exception e) {
            Log.e(TAG, "compressQualityToBytes exception: " + e.getMessage());
        } finally {
            if (byteArrayOutputStream != null) {
                try {
                    byteArrayOutputStream.close();
                } catch (IOException e) {
                    Log.e(TAG, "compressQualityToBytes exception: " + e.getMessage());
                }
            }
        }

        return result;
    }

    public static byte[] compressQualityToBytes(Bitmap bitmap, long desSize,
                                                Bitmap.CompressFormat compressFormat) {
        if (bitmap == null || desSize <= 0L || compressFormat == null) {
            Log.e(TAG, "compressQualityToBytes params null");
            return null;
        }

        byte[] result = null;
        ByteArrayOutputStream byteArrayOutputStream = null;
        try {
            byteArrayOutputStream = new ByteArrayOutputStream();
            int quality = COMPRESS_QUALITY_FULL;
            bitmap.compress(compressFormat, quality, byteArrayOutputStream);
            while (byteArrayOutputStream.toByteArray().length > desSize) {
                byteArrayOutputStream.reset();
                bitmap.compress(compressFormat, quality, byteArrayOutputStream);
                quality -= 10;
            }
            result = byteArrayOutputStream.toByteArray();
        } catch (Exception e) {
            Log.e(TAG, "compressQualityToBytes exception: " + e.getMessage());
        } finally {
            if (byteArrayOutputStream != null) {
                try {
                    byteArrayOutputStream.close();
                } catch (IOException e) {
                    Log.e(TAG, "compressQualityToBytes exception: " + e.getMessage());
                }
            }
        }

        return result;
    }

    /**
     * 大小压缩
     * <p>
     * 通过减少单位尺寸的像素值，真正意义上的降低像素(通过缩放图片像素来减少图片占用内存大小)
     * 使用场景：普通很大的图，比如压缩为缩略图
     *
     * @param bitmap bitmap
     * @param scale  值越大，图片尺寸越小
     * @return byte[]
     */
    public static Bitmap compressScale(Bitmap bitmap, float scale, Bitmap.Config config) {
        if (bitmap == null || config == null) {
            Log.e(TAG, "compressScale params null");
            return null;
        }

        if (scale < COMPRESS_SCALE_NONE) {
            Log.w(TAG, "compressScale scale invalid");
            scale = COMPRESS_SCALE_NONE;
        }

        Bitmap result = null;
        try {
            int width = (int) (bitmap.getWidth() / scale);
            int height = (int) (bitmap.getHeight() / scale);
            //mutable
            result = Bitmap.createBitmap(width, height, config);
            Canvas canvas = new Canvas(result);
            Rect rect = new Rect(0, 0, width, height);
            canvas.drawBitmap(bitmap, null, rect, null);
        } catch (Exception e) {
            Log.e(TAG, "compressScale exception: " + e.getMessage());
        }

        return result;
    }

    public static Bitmap compressScale(Bitmap bitmap, float scale) {
        if (bitmap == null) {
            Log.e(TAG, "compressScale bitmap null");
            return null;
        }

        if (scale < COMPRESS_SCALE_NONE) {
            Log.w(TAG, "compressScale scale invalid");
            scale = COMPRESS_SCALE_NONE;
        }

        Bitmap result = null;
        try {
            //immutable
            //内部调用的compressScaleRotate方法中的createBitmap方法
            result = Bitmap.createScaledBitmap(bitmap, (int) (bitmap.getWidth() / scale),
                    (int) (bitmap.getHeight() / scale), false);
        } catch (Exception e) {
            Log.e(TAG, "compressScale exception: " + e.getMessage());
        }

        return result;
    }

    /*public static Bitmap compressScale(Bitmap bitmap, float scale) {
        return compressScaleRotate(bitmap, scale, 0);
    }*/

    public static Bitmap rotate(Bitmap bitmap, float rotateDegree) {
        return compressScaleRotate(bitmap, COMPRESS_SCALE_NONE, rotateDegree);
    }

    public static Bitmap compressScaleRotate(Bitmap bitmap, float scale, float rotateDegree) {
        if (bitmap == null) {
            Log.e(TAG, "compressScaleRotate bitmap null");
            return null;
        }

        if (scale < COMPRESS_SCALE_NONE) {
            Log.w(TAG, "compressScaleRotate scale invalid");
            scale = COMPRESS_SCALE_NONE;
        }

        Matrix matrix = new Matrix();
        matrix.postScale(scale, scale);
        matrix.postRotate(rotateDegree);

        Bitmap result = null;
        try {
            //immutable
            result = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(),
                    bitmap.getHeight(), matrix, false);
        } catch (Exception e) {
            Log.e(TAG, "compressScaleRotate exception: " + e.getMessage());
        }

        return result;
    }

    public static Bitmap compressSampleFile(String filePath, int width, int height) {
        if (!FileUtils.isFileExist(filePath) || width <= 0 || height <= 0) {
            Log.e(TAG, "compressSampleFile prams invalid");
            return null;
        }

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        Bitmap bitmap = decodeFile(filePath, options);
        if (bitmap == null) {
            Log.e(TAG, "compressSampleFile exception");
            return null;
        }

        float srcWidth = options.outWidth;
        float srcHeight = options.outHeight;
        int inSampleSize = 1;
        if (srcHeight > height || srcWidth > width) {
            if (srcWidth > srcHeight) {
                inSampleSize = Math.round(srcHeight / height);
            } else {
                inSampleSize = Math.round(srcWidth / width);
            }
        }

        return compressSampleFile(filePath, inSampleSize);
    }

    /**
     * 采样率压缩(设置图片的采样率，降低图片像素)
     * <p>
     * 不需要将图片读入内存，大大减少了内存占用
     *
     * @param filePath     filePath
     * @param inSampleSize 数值越高，图片像素越低
     */
    public static Bitmap compressSampleFile(String filePath, int inSampleSize) {
        if (!FileUtils.isFileExist(filePath) || inSampleSize <= 0) {
            Log.e(TAG, "compressSampleFile params invalid");
            return null;
        }

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = false;
        options.inSampleSize = inSampleSize;
        return decodeFile(filePath, options);
    }

    public static Bitmap compressSampleBytes(byte[] bytes, int inSampleSize) {
        if (bytes == null || bytes.length == 0 || inSampleSize <= 0) {
            Log.e(TAG, "compressSampleBytes params invalid");
            return null;
        }

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = false;
        options.inSampleSize = inSampleSize;
        return decodeBytes(bytes, options);
    }

}