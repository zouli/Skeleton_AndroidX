package com.riverside.skeleton.android.util.file;

import android.text.TextUtils;

import com.riverside.skeleton.android.util.log.CLog;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * 文件工具类    1.0
 * b_e  2018/08/16
 */
public class FileUtils {
    private final static String SEPARATOR = "/";

    /**
     * 添加分隔符
     *
     * @param path
     * @return
     */
    public static String addSeparator(String path) {
        if (path.startsWith(SEPARATOR)) {
            return path;
        } else {
            return SEPARATOR + path;
        }
    }

    /**
     * 创建目录
     *
     * @param path 目录名
     * @return 完整路径
     */
    public static String mkdirs(String path) {
        // 取得目录对象
        File dir = new File(path);
        // 判断目录是否存在
        if (!dir.exists()) {
            // 创建目录
            dir.mkdirs();
        }

        return path;
    }

    /**
     * 取得文件名
     *
     * @param path 文件完整路径
     * @return
     */
    public static String getFilename(String path) {
        File dir = new File(path.trim());
        return dir.getName();
    }

    /**
     * 取得文件名
     *
     * @param file_url
     * @return
     */
    public static String getFilenameWithUrl(String file_url) {
        try {
            URL url = new URL(file_url);
            return url.getFile();
        } catch (MalformedURLException ignored) {
        }
        return "";
    }

    /**
     * 修改文件名
     *
     * @param src  旧文件名
     * @param dest 新文件名
     * @return
     */
    public static boolean rename(String src, String dest) {
        File file = new File(src);
        return file.exists() && file.renameTo(new File(dest));
    }

    /**
     * 删除文件
     *
     * @param filename 文件名
     * @return
     */
    public static boolean delete(String filename) {
        File file = new File(filename);
        return file.exists() && file.delete();
    }

    /**
     * 删除目录
     *
     * @param path
     */
    public static void deleteRecursively(String path) {
        File dir = new File(path);
        if (dir.exists()) {
            for (File file : dir.listFiles()) {
                if (file.isDirectory()) {
                    deleteRecursively(file.getPath());
                } else {
                    file.delete();
                }
            }
            dir.delete();
        }
    }

    /**
     * 判断文件是否存在
     *
     * @param filename
     * @return
     */
    public static boolean isExists(String filename) {
        if (TextUtils.isEmpty(filename)) {
            return false;
        }

        File file = new File(filename);
        return file.exists();
    }

    /**
     * 复制文件
     *
     * @param src     原文件
     * @param dest    目标文件
     * @param reWrite 是否覆盖
     * @return
     */
    public static boolean copyFile(String src, String dest, boolean reWrite) {
        File srcFile = new File(src);
        File destFile = new File(dest);
        if (!srcFile.exists()) {
            CLog.d("copyFile, source file not exist.");
            return false;
        }
        if (!srcFile.isFile()) {
            CLog.d("copyFile, source file not a file.");
            return false;
        }
        if (!srcFile.canRead()) {
            CLog.d("copyFile, source file can't read.");
            return false;
        }
        if (destFile.exists() && reWrite) {
            CLog.d("copyFile, before copy File, delete first.");
            destFile.delete();
        }

        try {
            int length = 2097152;
            InputStream inStream = new FileInputStream(srcFile);
            FileOutputStream outStream = new FileOutputStream(destFile);
            byte[] buf = new byte[length];
            int byteRead;
            while ((byteRead = inStream.read(buf)) != -1) {
                outStream.write(buf, 0, byteRead);
            }
            outStream.flush();
            outStream.close();
            inStream.close();
        } catch (IOException e) {
            return false;
        }

        return true;
    }
}