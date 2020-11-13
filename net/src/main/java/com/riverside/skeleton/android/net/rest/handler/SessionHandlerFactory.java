package com.riverside.skeleton.android.net.rest.handler;

import android.text.TextUtils;

import com.riverside.skeleton.android.net.rest.error.SessionTimeoutThrowable;
import com.riverside.skeleton.android.util.packageinfo.PackageInfoUtils;

import io.reactivex.Flowable;

/**
 * Session处理工厂类  1.0
 * b_e  2017/12/25
 */
public class SessionHandlerFactory {
    private static SessionHandler sessionHandler = create();

    /**
     * 取得当前Session处理对象
     * 根据session_save_point中设置的类创建对象
     * session_save_point中设置的类需要实现SessionCheckerHandler接口
     *
     * @return
     */
    private static SessionHandler create() {
        try {
            String sessionSavePoint = PackageInfoUtils.getMetadataString("SESSION_SAVE_POINT", "");
            if (!TextUtils.isEmpty(sessionSavePoint)) {
                Class clazz = Class.forName(sessionSavePoint);
                return (SessionHandler) clazz.newInstance();
            }
        } catch (Exception e) {
            throw new RuntimeException("SESSION_SAVE_POINT中设置的类需要实现SessionCheckerHandler接口");
        }
        return null;
    }

    /**
     * 调用当前Session处理对象的"判断是否可以保存当前Session"方法
     *
     * @param url
     * @return
     */
    public static boolean canToSave(String url) {
        return sessionHandler != null && sessionHandler.canToSave(url);
    }

    /**
     * 调用当前Session处理对象的"重新登录"方法
     */
    public static Flowable<?> reLogin() {
        if (sessionHandler != null) {
            return sessionHandler.reLogin();
        }
        return Flowable.error(new SessionTimeoutThrowable("Session过期", "-1"));
    }

    /**
     * 调用当前Session处理对象的"取得Header对应的Value"方法
     *
     * @param key
     * @return
     */
    public static String getHeaderValue(String key) {
        return (sessionHandler != null) ? sessionHandler.getHeaderValue(key) : "";
    }
}
