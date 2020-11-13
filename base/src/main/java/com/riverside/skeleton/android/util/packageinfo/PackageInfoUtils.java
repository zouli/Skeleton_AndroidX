package com.riverside.skeleton.android.util.packageinfo;

import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import com.riverside.skeleton.android.base.application.BaseApplication;

import java.util.List;

/**
 * PackageInfo工具类   1.1
 * b_e  2017/12/24
 * 1.1  添加检查手机上是否安装了指定的软件方法
 */
public class PackageInfoUtils {
    /**
     * 取得带有Meta Data的ApplicationInfo
     *
     * @return
     */
    public static ApplicationInfo getApplicationHaveMetadata() {
        try {
            return BaseApplication.getInstance().getPackageManager()
                    .getApplicationInfo(BaseApplication.getInstance().getPackageName()
                            , PackageManager.GET_META_DATA);
        } catch (PackageManager.NameNotFoundException e) {
            return null;
        }
    }

    /**
     * 取得文字内容
     *
     * @param key
     * @return
     */
    public static String getMetadataString(String key, String defaultValue) {
        ApplicationInfo applicationInfo = getApplicationHaveMetadata();
        if (applicationInfo == null) {
            return defaultValue;
        } else {
            return applicationInfo.metaData.getString(key, defaultValue);
        }
    }

    /**
     * 取得数字内容
     *
     * @param key
     * @return
     */
    public static Integer getMetadataInt(String key, int defaultValue) {
        ApplicationInfo applicationInfo = getApplicationHaveMetadata();
        if (applicationInfo == null) {
            return defaultValue;
        } else {
            return applicationInfo.metaData.getInt(key, defaultValue);
        }
    }

    /**
     * 检查手机上是否安装了指定的软件
     *
     * @param packageName
     * @return
     */
    public static boolean isAvailable(String packageName) {
        PackageManager packageManager = BaseApplication.getInstance().getPackageManager();
        List<PackageInfo> packageInfoList = packageManager.getInstalledPackages(0);
        if (packageInfoList != null) {
            for (int i = 0; i < packageInfoList.size(); i++) {
                if (packageInfoList.get(i).packageName.equals(packageName)) {
                    return true;
                }
            }
        }
        return false;
    }
}
