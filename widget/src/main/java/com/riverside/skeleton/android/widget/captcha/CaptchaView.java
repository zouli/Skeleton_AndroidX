package com.riverside.skeleton.android.widget.captcha;

/**
 * 验证码控件
 */
public interface CaptchaView {
    /**
     * 取得已输入验证码
     *
     * @return
     */
    String getText();

    /**
     * 输入变更接口
     */
    interface InputChangedListener {
        void inputChanged(String text);
    }

    /**
     * 结果回调接口
     */
    interface ResultListener {
        void onError(int errorCode);

        void onClick();
    }
}
