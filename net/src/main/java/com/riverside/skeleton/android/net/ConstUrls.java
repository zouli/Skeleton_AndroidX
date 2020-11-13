package com.riverside.skeleton.android.net;

import com.riverside.skeleton.android.util.packageinfo.PackageInfoUtils;

public class ConstUrls {
    // 链接超时时间
    public static int CONNECT_TIME_OUT = PackageInfoUtils.getMetadataInt("CONNECT_TIME_OUT", 15);
    // 服务器地址
    public static String SERVER_ROOT = PackageInfoUtils.getMetadataString("SERVER_HOST", "");
    // 链接重试次数
    public static int CONNECT_RETRY_COUNT = PackageInfoUtils.getMetadataInt("CONNECT_RETRY_COUNT", 3);
    // 链接重试间隔
    public static int CONNECT_RETRY_DELAY = PackageInfoUtils.getMetadataInt("CONNECT_RETRY_DELAY", 3000);
}