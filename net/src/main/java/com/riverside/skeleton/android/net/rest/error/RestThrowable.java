package com.riverside.skeleton.android.net.rest.error;

/**
 * Rest通用错误类    1.0
 * b_e  2017/12/25
 */
public class RestThrowable extends Throwable {

    private String errorCode;

    public RestThrowable(String errorCode) {
        this.errorCode = errorCode;
    }

    public RestThrowable(String detailMessage, String errorCode) {
        super(detailMessage);
        this.errorCode = errorCode;
    }

    public RestThrowable(String detailMessage, Throwable cause, String errorCode) {
        super(detailMessage, cause);
        this.errorCode = errorCode;
    }

    public RestThrowable(Throwable cause, String errorCode) {
        super(cause);
        this.errorCode = errorCode;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }
}
