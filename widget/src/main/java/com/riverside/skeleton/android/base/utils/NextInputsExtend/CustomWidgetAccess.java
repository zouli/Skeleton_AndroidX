package com.riverside.skeleton.android.base.utils.NextInputsExtend;

import android.app.Activity;

import com.github.yoojia.inputs.Input;
import com.riverside.skeleton.android.widget.captcha.CaptchaView;

/**
 * 控件访问扩展   1.0
 * b_e  2017/11/26
 */
public class CustomWidgetAccess extends InputsAccessExtend {
    public CustomWidgetAccess(Activity activity) {
        super(activity);
    }

    /**
     * 取得验证码控件
     *
     * @param viewId
     * @return
     */
    public Input findCaptchaView(int viewId) {
        return CustomWidgetInputs.verificationCodeViewInput((CaptchaView) findView(viewId));
    }
}
