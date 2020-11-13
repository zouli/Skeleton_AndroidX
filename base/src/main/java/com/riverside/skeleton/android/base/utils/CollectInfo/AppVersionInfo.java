package com.riverside.skeleton.android.base.utils.CollectInfo;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import com.alibaba.fastjson.JSONObject;

/**
 * 取得App的版本信息   1.0
 * b_e  2017/12/10
 */
public class AppVersionInfo implements InfoSource {
    @Override
    public JSONObject getInfo(Context ctx) {
        // 生成json对象用于保存信息
        JSONObject info = new JSONObject();
        try {
            // 取得Package信息
            PackageManager pm = ctx.getPackageManager();
            PackageInfo pi = pm.getPackageInfo(ctx.getPackageName(), PackageManager.GET_ACTIVITIES);
            // 判断Package是否能够取得
            if (pi != null) {
                // 取得版本名称
                String versionName = pi.versionName == null ? "null" : pi.versionName;
                // 取得版本号
                String versionCode = pi.versionCode + "";
                // 保存版本名称和版本号
                info.put("versionName", versionName);
                info.put("versionCode", versionCode);
            }
        } catch (Exception ignored) {
        }
        return info;
    }
}
