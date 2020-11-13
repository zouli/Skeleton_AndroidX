package com.riverside.skeleton.android.base.utils.NextInputsExtend;

import android.view.View;
import android.widget.TextView;

import com.github.yoojia.inputs.Input;
import com.github.yoojia.inputs.MessageDisplay;
import com.github.yoojia.inputs.TextInput;
import com.github.yoojia.inputs.ViewInput;
import com.riverside.skeleton.android.base.application.BaseApplication;
import com.riverside.skeleton.android.util.log.CLog;
import com.riverside.skeleton.android.util.notice.ToastHelper;

/**
 * 错误提示Toast版    1.0
 * b_e  2017/11/23
 */
public class AndroidToastMessageDisplay implements MessageDisplay {
    @Override
    public void show(Input input, String message) {
        // try attach
        final View inputView;
        if (input instanceof TextInput) {
            inputView = ((TextInput) input).inputView;
        } else if (input instanceof ViewInput) {
            inputView = ((ViewInput) input).inputView;
        } else {
            CLog.e("- When use <AndroidMessageDisplay>, <TextInput> is recommend !");
            inputView = null;
        }
        // try show message
        if (inputView == null) {
            CLog.w("- TestResult.message=" + message);
            return;
        }

        if (TextView.class.isAssignableFrom(inputView.getClass())) {
            final TextView text = (TextView) inputView;
            text.requestFocus();
        }
        ToastHelper.getInstance(BaseApplication.getInstance()).showToast(message);
    }
}