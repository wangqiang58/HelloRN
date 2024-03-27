package cn.xdf.ucan.troy.lib.utils;

import android.os.Build;
import android.os.StatFs;
import android.text.TextUtils;
import android.util.Log;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * @author hulijia
 * @createDate 2021/8/30
 * @description FileUtils
 */
public class FileUtils {
    private static final String TAG = "FileUtils-";

    public static boolean isFileExist(String filePath) {
        if (TextUtils.isEmpty(filePath)) {
            Log.e(TAG, "isFileExist filePath null");
            return false;
        }

        boolean result = false;
        try {
            result = new File(filePath).exists();
        } catch (Exception e) {
            Log.e(TAG, "isFileExist exception: " + e.getMessage());
        }

        return result;
    }

    public static boolean isFile(String filePath) {
        if (TextUtils.isEmpty(filePath)) {
            Log.e(TAG, "isFile filePath null");
            return false;
        }

        boolean result = false;
        try {
            result = new File(filePath).isFile();
        } catch (Exception e) {
            Log.e(TAG, "isFile exception: " + e.getMessage());
        }

        return result;
    }

    public static String getFilePath(File file) {
        if (file == null || !file.exists()) {
            Log.e(TAG, "getFilePath file invalid");
            return null;
        }

        return file.getAbsolutePath();
    }

    public static long getFileFreeSize(String filePath) {
        if (!isFileExist(filePath)) {
            Log.e(TAG, "getFileFreeSize not exist: " + filePath);
            return -1L;
        }

        StatFs statFs = new StatFs(filePath);
        long result;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            result = statFs.getBlockSizeLong() * statFs.getAvailableBlocksLong();
        } else {
            result = statFs.getBlockSize() * statFs.getAvailableBlocks();
        }

        return result;
    }

    public static File writeFile(byte[] bytes, String filePath) {
        if (bytes == null || bytes.length == 0) {
            Log.e(TAG, "writeFile bytes null");
            return null;
        }

        if (!createFile(filePath)) {
            Log.e(TAG, "writeFile createFile failed");
            return null;
        }

        File result = null;
        ByteArrayInputStream byteArrayInputStream = null;
        FileOutputStream fileOutputStream = null;
        try {
            byteArrayInputStream = new ByteArrayInputStream(bytes);
            fileOutputStream = new FileOutputStream(filePath);
            byte[] buf = new byte[4096];
            int length;
            while ((length = byteArrayInputStream.read(buf)) != -1) {
                fileOutputStream.write(buf, 0, length);
            }
            result = new File(filePath);
        } catch (Exception e) {
            Log.e(TAG, "writeFile exception: " + e.getMessage());
        } finally {
            closeQuietly(fileOutputStream);
            if (byteArrayInputStream != null) {
                try {
                    byteArrayInputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return result;
    }

    public static byte[] readFile(String filePath) {
        if (!isFileExist(filePath)) {
            Log.e(TAG, "readFile file invalid");
            return null;
        }

        byte[] result = null;
        ByteArrayOutputStream byteArrayOutputStream = null;
        FileInputStream fileInputStream = null;
        try {
            byteArrayOutputStream = new ByteArrayOutputStream();
            fileInputStream = new FileInputStream(filePath);
            byte[] buf = new byte[4096];
            int length;
            while ((length = fileInputStream.read(buf)) != -1) {
                byteArrayOutputStream.write(buf, 0, length);
            }
            result = byteArrayOutputStream.toByteArray();
        } catch (Exception e) {
            Log.e(TAG, "writeFile exception: " + e.getMessage());
        } finally {
            closeQuietly(fileInputStream);
            if (byteArrayOutputStream != null) {
                try {
                    byteArrayOutputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return result;
    }

    public static void addFilePermissions(String filePath, boolean isRecursive) {
        if (!isFileExist(filePath)) {
            Log.e(TAG, "addFilePermissions not exist: " + filePath);
            return;
        }

        try {
            String recursiveParams = isRecursive ? "-R " : "";
            Runtime.getRuntime().exec("chmod " + recursiveParams + "777 " + filePath);
        } catch (Exception e) {
            Log.e(TAG, "addPermissionsFile exception: " + e.getMessage());
        }
    }

    public static boolean createFile(String filePath) {
        if (TextUtils.isEmpty(filePath)) {
            Log.e(TAG, "createFile filePath null");
            return false;
        }

        File file = new File(filePath);
        if (file.exists()) {
            return true;
        }

        //file.mkdirs()会把最后一个文件当做文件夹来创建，因此需要getParentFile()
        if (!file.getParentFile().mkdirs()) {
            //目录已经存在时，mkdirs会返回false，此时不能return false
            Log.d(TAG, "createFile mkdirs failed");
        }

        try {
            return file.createNewFile();
        } catch (IOException e) {
            Log.e(TAG, "createFile exception: " + e.getMessage());
            return false;
        }
    }

    public static boolean deleteFile(String filePath) {
        if (TextUtils.isEmpty(filePath)) {
            Log.e(TAG, "deleteFile filePath null");
            return false;
        }

        boolean result = false;
        try {
            result = new File(filePath).delete();
        } catch (Exception e) {
            Log.e(TAG, "deleteFile exception: " + e.getMessage());
        }

        if (!result) {
            Log.e(TAG, "deleteFile failure: " + filePath);
        }

        return result;
    }

    public static void deleteFiles(File file, boolean isDeleteRoot) {
        if (file == null || !file.exists()) {
            Log.e(TAG, "deleteFiles filePath invalid");
            return;
        }

        File[] files = file.listFiles();
        if (files == null || files.length == 0) {
            Log.e(TAG, "deleteFiles files null");
            return;
        }

        for (File item : files) {
            if (item == null) {
                continue;
            }

            if (item.isDirectory()) {
                deleteFiles(item, true);
            } else {
                item.delete();
            }
        }

        if (isDeleteRoot) {
            file.delete();
        }
    }

    public static void closeQuietly(Closeable closeable) {
        if (closeable == null) {
            Log.e(TAG, "closeQuietly closeable exist");
            return;
        }

        try {
            closeable.close();
        } catch (Throwable e) {
            Log.e(TAG, "closeQuietly exception: " + e.getMessage());
        }
    }

    /**
     * 按文件名排序
     *
     * @param filePath
     * @return
     */
    public static ArrayList<String> orderByName(String filePath) {
        ArrayList<String> filePathList = new ArrayList<>();
        File file = new File(filePath);
        File[] files = file.listFiles();
        assert files != null;
        List<File> fileList = Arrays.asList(files);
        Collections.sort(fileList, new Comparator<File>() {
            @Override
            public int compare(File o1, File o2) {
                if (o1.isDirectory() && o2.isFile()) {
                    return -1;
                }
                if (o1.isFile() && o2.isDirectory()) {
                    return 1;
                }
                return o2.getName().compareTo(o1.getName());
            }
        });
        for (File file1 : files) {
            if (file1.isFile()) {
                filePathList.add(file1.getAbsolutePath());
            }
        }
        return filePathList;
    }

    /**
     * 遍历目标文件夹下以及子目录下的所有文件
     *
     * @param sPath            目标文件夹
     * @param filterDir        要过滤的目录
     * @param filterFileSuffix 要过滤的文件后缀
     * @return
     */
    public static List<File> getFilesInDirectory(String sPath, String filterDir, String filterFileSuffix) {
        List<File> result = new ArrayList<>();
        // 如果sPath不以文件分隔符结尾，自动添加文件分隔符
        if (!sPath.endsWith(File.separator)) {
            sPath = sPath + File.separator;
        }
        File dirFile = new File(sPath);
        // 如果dir对应的文件不存在，或者不是一个目录，则退出
        if (!dirFile.exists() || !dirFile.isDirectory()) {
            return result;
        }
        File[] files = dirFile.listFiles();
        for (File file : files) {
            if (file.isFile()) {
                String suffix = file.getName().substring(file.getName().lastIndexOf(".") + 1);
                if (!TextUtils.equals(suffix, filterFileSuffix)) {
                    result.add(file);
                }
            } else {
                if (!TextUtils.equals(file.getName(), filterDir)) {
                    result.addAll(getFilesInDirectory(file.getAbsolutePath(), filterDir, filterFileSuffix));
                }
            }
        }
        return result;
    }

    public static long getFileSize(String filePath) {
        FileChannel fc = null;
        long fileSize = 0;
        try {
            File f = new File(filePath);
            if (f.exists() && f.isFile()) {
                FileInputStream fis = new FileInputStream(f);
                fc = fis.getChannel();
                fileSize = fc.size();
            } else {
                Log.e("getFileSize", "file doesn't exist or is not a file");
            }
        } catch (FileNotFoundException e) {
            Log.e("getFileSize", e.getMessage());
        } catch (IOException e) {
            Log.e("getFileSize", e.getMessage());
        } finally {
            if (null != fc) {
                try {
                    fc.close();
                } catch (IOException e) {
                    Log.e("getFileSize", e.getMessage());
                }
            }
        }
        return fileSize;
    }

}