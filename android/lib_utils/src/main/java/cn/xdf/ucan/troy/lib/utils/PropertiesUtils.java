package cn.xdf.ucan.troy.lib.utils;

import android.content.Context;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;

/**
 * @author hulijia
 * @createDate 2021/8/30
 * @description PropertiesUtils
 */
public class PropertiesUtils {

    public static Properties getProperties(Context context, String filePath, boolean isAssets) {
        Properties properties = new Properties();
        InputStream inputStream = null;
        try {
            inputStream = isAssets ? context.getAssets().open(filePath)
                    : new FileInputStream(filePath);
            properties.load(inputStream);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return properties;
    }

    public static void setProperties(Context context, Properties properties,
                                     String filePath, boolean isAssets) {
        OutputStream outputStream = null;
        try {
            outputStream = isAssets ? context.getAssets().openFd(filePath).createOutputStream()
                    : new FileOutputStream(filePath);
            properties.store(outputStream, filePath);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}