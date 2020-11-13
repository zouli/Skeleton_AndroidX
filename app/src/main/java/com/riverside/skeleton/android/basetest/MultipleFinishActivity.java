package com.riverside.skeleton.android.basetest;

import android.view.View;
import android.widget.TextView;

import com.riverside.skeleton.android.R;
import com.riverside.skeleton.android.base.activity.ActivityStackManager;
import com.riverside.skeleton.android.base.activity.BaseActivity;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

@EActivity(R.layout.activity_multiple_finish)
public class MultipleFinishActivity extends BaseActivity {
    @ViewById
    TextView tv_title;

    @AfterViews
    void initView() {
        tv_title.setText(ActivityStackManager.getInstance().countActivity() + "");
    }

    @Click(R.id.btn_open)
    void onBtnOpenClick(View v) {
        MultipleFinishActivity_.intent(activity).start();
    }

    @Click(R.id.btn_allclose)
    void onBtnAllcloseClick(View v) {
        ActivityStackManager.getInstance().doMultipleFinish();
    }
}
