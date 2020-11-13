package com.riverside.skeleton.android.base.utils.NextInputsExtend;

import com.github.yoojia.inputs.Input;
import com.riverside.skeleton.android.widget.captcha.CaptchaView;

/**
 * 选择器扩展    1.0
 * b_e  2017/11/26
 */
public class CustomWidgetInputs extends AndroidInputsExtend {
    /**
     * 取得验证码控件是否有值
     *
     * @param captchaView
     * @return
     */
    public static Input verificationCodeViewInput(final CaptchaView captchaView) {
        return new Input() {
            @Override
            public String getValue() {
                return captchaView.getText();
            }
        };
    }
}
