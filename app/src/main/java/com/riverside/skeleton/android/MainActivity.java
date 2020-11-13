package com.riverside.skeleton.android;

import android.view.View;

import com.riverside.skeleton.android.base.activity.BaseActivity;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;

@EActivity(R.layout.activity_main)
public class MainActivity extends BaseActivity {
    @AfterViews
    void initView() {
    }

    @Click(R.id.btn_base)
    void onBtnBaseClick(View v) {
        BaseMainActivity_.intent(activity).start();
    }

    @Click(R.id.btn_net)
    void onBtnNetClick(View v) {
        NetMainActivity_.intent(activity).start();
    }

    @Click(R.id.btn_widget)
    void onBtnWidgetClick(View v) {
        WidgetMainActivity_.intent(activity).start();
    }
}
