package com.riverside.skeleton.android.base.utils.CollectInfo;

import android.content.Context;
import android.util.DisplayMetrics;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

/**
 * 取得显示信息   1.0
 * b_e  2017/12/10
 */
public class DisplayInfo implements InfoSource {
    @Override
    public JSONObject getInfo(Context ctx) {
        // 取得设备屏幕信息
        DisplayMetrics dm = ctx.getResources().getDisplayMetrics();
        String dmString = dm.toString().replace("DisplayMetrics", "").replace("=", ":");
        return JSON.parseObject(dmString);
    }
}
