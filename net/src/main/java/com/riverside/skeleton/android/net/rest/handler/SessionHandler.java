package com.riverside.skeleton.android.net.rest.handler;

import io.reactivex.Flowable;

/**
 * Session处理接口  1.1
 * b_e  2017/12/25
 * 1.1  添加取得Header对应Value   2019/04/11
 */
public interface SessionHandler {
    /**
     * 判断是否可以保存当前Session
     */
    boolean canToSave(String url);

    /**
     * 重新登录
     */
    Flowable<?> reLogin();

    /**
     * 取得Header对应的Value
     *
     * @param key
     * @return
     */
    String getHeaderValue(String key);
}

