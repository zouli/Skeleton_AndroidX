package com.riverside.skeleton.android.base.utils.CollectInfo;

import android.content.Context;

import com.alibaba.fastjson.JSONObject;

/**
 * 信息源接口    1.0
 * b_e  2017/12/10
 */
public interface InfoSource {
    /**
     * 取得信息
     *
     * @param ctx
     * @return
     */
    JSONObject getInfo(Context ctx);
}
