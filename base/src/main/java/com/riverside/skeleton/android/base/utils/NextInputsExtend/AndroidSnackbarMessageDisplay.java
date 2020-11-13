package com.riverside.skeleton.android.base.utils.NextInputsExtend;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.view.View;
import android.widget.TextView;

import com.github.yoojia.inputs.Input;
import com.github.yoojia.inputs.MessageDisplay;
import com.github.yoojia.inputs.TextInput;
import com.github.yoojia.inputs.ViewInput;
import com.riverside.skeleton.android.util.log.CLog;
import com.riverside.skeleton.android.util.notice.SnackbarHelper;

/**
 * 错误提示Snackbar版    1.0
 * b_e  2017/11/23
 */
public class AndroidSnackbarMessageDisplay implements MessageDisplay {
    @SuppressLint("Range")
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

        Context context = inputView.getContext();
        if (!(context instanceof Activity) && TextView.class.isAssignableFrom(inputView.getClass())) {
            context = ((ContextWrapper) inputView.getContext()).getBaseContext();
            final TextView text = (TextView) inputView;
            text.requestFocus();
        }
        SnackbarHelper.Builder(context).showSnackbar(message, SnackbarHelper.LENGTH_LONG);
    }
}