package com.riverside.skeleton.android;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import android.view.KeyEvent;
import android.view.View;
import android.widget.CheckBox;

import com.riverside.skeleton.android.base.activity.ActivityStackManager;
import com.riverside.skeleton.android.base.activity.ActivityStaticMonitor;
import com.riverside.skeleton.android.base.activity.BaseActivity;
import com.riverside.skeleton.android.base.application.BaseApplication;
import com.riverside.skeleton.android.base.upgrade.UpgradeHelper;
import com.riverside.skeleton.android.base.utils.BackButtonHelper;
import com.riverside.skeleton.android.basetest.MultipleFinishActivity_;
import com.riverside.skeleton.android.basetest.RxBusActivity_;
import com.riverside.skeleton.android.basetest.ValidateActivity_;
import com.riverside.skeleton.android.util.log.CLog;
import com.riverside.skeleton.android.util.notice.ToastHelper;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

@EActivity(R.layout.activity_base_main)
public class BaseMainActivity extends BaseActivity {
    ActivityStaticMonitor.TransferListener transferListener;

    @ViewById
    CheckBox cb_all;

    UpgradeHelper upgradeHelper;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityStaticMonitor.get().addTransferListener(transferListener = new ActivityStaticMonitor.TransferListener() {
            @Override
            public void onBecameForeground() {
                CLog.d("程序进入前台");
            }

            @Override
            public void onBecameBackground() {
                CLog.d("程序进入后台");
            }
        });
    }

    @AfterViews
    void initView() {
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        return BackButtonHelper.onKeyUp(keyCode, event, new BackButtonHelper.Listener() {
            @Override
            public void onBackClick(int hits) {
                if (hits == 1) {
                    ToastHelper.getInstance(BaseApplication.getInstance())
                            .showToast("再按一次返回键，退出APP");
                } else if (hits == 2) {
                    finish();
                }
            }
        }) || super.onKeyUp(keyCode, event);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityStaticMonitor.get().removeTransferListener(transferListener);
    }

    @Click(R.id.btnToLogin)
    void onBtnToLoginClick(View v) {
        ActivityStackManager.getInstance().toLoginActivity(cb_all.isChecked());
    }

    @Click(R.id.btnUpgrade)
    void onBtnUpgradeClick(View v) {
        upgradeHelper = new UpgradeHelper(activity);
        upgradeHelper.startUpgrade("", "", "http://www.915guo.com.cn/EXAM/upload/MedicalExam_CSTP.apk");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        upgradeHelper.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Click(R.id.btnMultiplefinish)
    void onBtnMultipleFinishClick(View v) {
        ActivityStackManager.getInstance().startMultipleFinish();
        MultipleFinishActivity_.intent(activity).start();
    }

    @Click(R.id.btnRxbus)
    void onBtnRxbusClick(View v) {
        RxBusActivity_.intent(activity).start();
    }

    @Click(R.id.btnValidate)
    void onBtnValidateClick(View v) {
        ValidateActivity_.intent(activity).start();
    }
}
