package com.riverside.skeleton.android.widgettest;

import com.riverside.skeleton.android.R;
import com.riverside.skeleton.android.base.activity.BaseActivity;
import com.riverside.skeleton.android.util.log.CLog;
import com.riverside.skeleton.android.util.notice.SnackbarHelper;
import com.riverside.skeleton.android.widget.captcha.BoxCaptchaView;
import com.riverside.skeleton.android.widget.captcha.CaptchaView;
import com.riverside.skeleton.android.widget.captcha.InputCaptchaView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

@EActivity(R.layout.activity_captcha_view)
public class CaptchaViewActivity extends BaseActivity {
    @ViewById
    InputCaptchaView icv;

    @ViewById
    BoxCaptchaView bcv;

    @AfterViews
    void initView() {
        icv.setOnResultListener(new CaptchaView.ResultListener() {
            @Override
            public void onError(int errorCode) {
                SnackbarHelper.Builder(activity).showSnackbar("请输入手机号码", SnackbarHelper.LENGTH_LONG);
            }

            @Override
            public void onClick() {

            }
        });

        bcv.setOnInputChangedListener(new CaptchaView.InputChangedListener() {
            @Override
            public void inputChanged(String text) {
                CLog.w(text);
            }
        });
    }
}
