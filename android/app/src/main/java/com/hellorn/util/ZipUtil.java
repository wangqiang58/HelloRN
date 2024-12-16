package com.hellorn.util;

import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;

public class ZipUtil {

    private static String TAG  = "ZipUtil";


    public static void unzipFolder(String zipFilePath,String outputDirectory) {
        try {
            ZipFile zipFile = new ZipFile(zipFilePath);
            Enumeration<? extends ZipEntry> entries = zipFile.entries();
            while (entries.hasMoreElements()) {
                ZipEntry entry = entries.nextElement();
                String entryName = entry.getName();
                String filePath = outputDirectory + File.separator + entryName;
                File entryFile = new File(filePath);

                if (entry.isDirectory()) {
                    if (!entryFile.exists()) {
                        if (!entryFile.mkdirs()) {
                            Log.e(TAG, "Failed to create directory: " + filePath);
                        }
                    }
                } else {
                    File parent = entryFile.getParentFile();
                    if (parent!= null &&!parent.exists()) {
                        if (!parent.mkdirs()) {
                            Log.e(TAG, "Failed to create parent directory: " + parent.getAbsolutePath());
                        }
                    }
                    extractFile(zipFile, entry, filePath);
                }
            }
            zipFile.close();
        } catch (IOException e) {
            Log.e(TAG, "Error unzipping file", e);
        }
    }

    private static void extractFile(ZipFile zipFile, ZipEntry entry, String filePath) throws IOException {
        try (InputStream inputStream = zipFile.getInputStream(entry);
             BufferedOutputStream outputStream = new BufferedOutputStream(new FileOutputStream(filePath))) {
            byte[] buffer = new byte[4096];
            int length;
            while ((length = inputStream.read(buffer)) > 0) {
                outputStream.write(buffer, 0, length);
            }
        }
    }
}
