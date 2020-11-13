package com.riverside.skeleton.android.base.utils.CollectInfo;

import android.content.Context;
import android.os.Build;

import com.alibaba.fastjson.JSONObject;

/**
 * 取得OS相关信息 1.0
 * b_e  2017/12/10
 */
public class OSInfo implements InfoSource {
    @Override
    public JSONObject getInfo(Context ctx) {
        return ObjectFieldUtils.toJSONObject(Build.class.getDeclaredFields());
    }
}
