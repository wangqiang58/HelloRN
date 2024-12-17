package com.hellorn.util;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class FileUtil {

    public static String findJSBundleFiles(String folderPath, String suffix) {

        File folder = new File(folderPath);
        File[] files = folder.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isFile() && file.getName().endsWith(suffix)) {
                    return file.getAbsolutePath();
                } else if (file.isDirectory()) {
                    findJSBundleFiles(file.getAbsolutePath(), suffix);
                }
            }
        }
        return null;
    }
}
