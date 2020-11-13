package com.riverside.skeleton.android.base.utils.CollectInfo;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import androidx.core.content.ContextCompat;
import android.telephony.TelephonyManager;

import com.alibaba.fastjson.JSONObject;

/**
 * 取得话机信息   1.0
 * b_e  2017/12/10
 */
public class TelephonyInfo implements InfoSource {
    @SuppressLint("HardwareIds")
    @Override
    public JSONObject getInfo(Context ctx) {
        // 生成json对象用于保存信息
        JSONObject info = new JSONObject();
        //判读是否有读取权限
        if (ContextCompat.checkSelfPermission(ctx, Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED) {
            // 取得设备通讯信息
            TelephonyManager tm = (TelephonyManager) ctx.getSystemService(Context.TELEPHONY_SERVICE);
            if (tm != null) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    info.put("PhoneCount", tm.getPhoneCount());
                }
                info.put("DeviceSoftwareVersion", tm.getDeviceSoftwareVersion());
                info.put("Line1Number", tm.getLine1Number());
                info.put("SimSerialNumber", tm.getSimSerialNumber());
                info.put("DeviceId", tm.getDeviceId());
                info.put("PhoneType", tm.getPhoneType());
            }
        }
        return info;
    }
}