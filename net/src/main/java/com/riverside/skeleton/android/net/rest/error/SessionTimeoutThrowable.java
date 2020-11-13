package com.riverside.skeleton.android.net.rest.error;

/**
 * Session过期错误类 1.0
 * b_e  2017/12/25
 */
public class SessionTimeoutThrowable extends RestThrowable {
    public SessionTimeoutThrowable(String detailMessage, String errorCode) {
        super(detailMessage, errorCode);
    }
}
