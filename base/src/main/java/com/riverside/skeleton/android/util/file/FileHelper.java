package com.riverside.skeleton.android.util.file;

import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import androidx.core.content.FileProvider;

import com.riverside.skeleton.android.util.packageinfo.PackageInfoUtils;

import java.io.File;
import java.io.IOException;

/**
 * 文件帮助类    1.3
 * b_e  2017/12/06
 * 1.1  抽出PackageInfo方法
 * 1.2  添加删除目录方法、添加取得cache目录方法
 * 1.3  添加取得文件URI方法 2018/04/10
 */
public class FileHelper {
    //程序数据的根目录
    public static String BASE_PATH = getBasePath();

    /**
     * 取得程序根目录
     *
     * @return 根目录
     */
    private static String getBasePath() {
        return PackageInfoUtils.getMetadataString("APP_HOME", "");
    }

    /**
     * 取得SD卡的根目录
     *
     * @return SD卡的根目录
     */
    public static String getSdBasePath() {
        // SD根目录
        String sdDir;
        // 取得SD信息
        boolean sdCardExist = Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED);
        // 判读是否有SD卡
        if (sdCardExist) {
            // 取得SD卡根目录
            sdDir = Environment.getExternalStorageDirectory().toString();
        } else {
            // 使用系统根目录
            sdDir = "";
        }
        return sdDir;
    }

    /**
     * 取得cache目录
     *
     * @param context
     * @return
     */
    public static String getCachePath(Context context) {
        File cache = context.getExternalCacheDir();
        if (cache == null) {
            return "";
        } else {
            return cache.getPath();
        }
    }

    /**
     * 返回包含SD目录的根目录
     *
     * @param path 目标路径
     * @return 根目录
     */
    public static String getPath(String path) {
        // 取得SD根目录
        String sdDir = getSdBasePath();
        return sdDir + FileUtils.addSeparator(BASE_PATH) + FileUtils.addSeparator(path.trim());
    }

    /**
     * 返回不包含SD目录的根目录
     *
     * @param path 目标路径
     * @return 根目录
     */
    public static String getPathWithoutSD(String path) {
        return FileUtils.addSeparator(BASE_PATH) + FileUtils.addSeparator(path.trim());
    }

    /**
     * 创建目录
     *
     * @param path 目录名
     * @return 完整路径
     */
    public static String mkdirs(String path) {
        // 生成完整路径
        String okPath = getPath(path);
        return FileUtils.mkdirs(okPath);
    }

    /**
     * 创建.nomedia文件
     */
    public static void CreateNomediaFile() {
        String filename = getPath(".nomedia");
        if (!FileUtils.isExists(filename)) {
            File nomedia = new File(filename);
            try {
                nomedia.createNewFile();
            } catch (IOException e) {
            }
        }
    }

    /**
     * 取得Uri
     *
     * @param context
     * @param url
     * @return
     */
    public static Uri getFileUri(Context context, String url) {
        File mTmpFile = new File(url);
        Uri uri;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            uri = FileProvider.getUriForFile(context, context.getPackageName() + ".fileprovider", mTmpFile);
        } else
            uri = Uri.fromFile(mTmpFile);

        return uri;
    }
}
