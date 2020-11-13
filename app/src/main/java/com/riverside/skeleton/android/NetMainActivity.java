package com.riverside.skeleton.android;

import android.view.View;

import com.riverside.skeleton.android.base.activity.BaseActivity;
import com.riverside.skeleton.android.net.jsonbean.JsonResponse;
import com.riverside.skeleton.android.net.rest.CommonRestService;
import com.riverside.skeleton.android.net.rest.ObservableHelper2;
import com.riverside.skeleton.android.net.rest.utils.ObservableHelper;
import com.riverside.skeleton.android.net.rest.utils.RestSubscriber;
import com.riverside.skeleton.android.net.rest.utils.RetrofitBindHelper;
import com.riverside.skeleton.android.util.log.CLog;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@EActivity(R.layout.activity_net_main)
public class NetMainActivity extends BaseActivity {
    @AfterViews
    void initView() {
    }

    @Click(R.id.btn_login)
    void onBtnLoginClick(View v) {
        RetrofitBindHelper.getInstance().doBind(CommonRestService.class).login()
                .compose(ObservableHelper.<JsonResponse<String>>startReading())
                .compose(ObservableHelper2.<String>checkResult())
                .compose(ObservableHelper.<String>toMainThread())
                .subscribe(new RestSubscriber<String>() {
                    @Override
                    public void onNext(String s) {

                    }

                    @Override
                    public void onError(Throwable t) {
                        CLog.e("onError", t);
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    @Click(R.id.btn_logout)
    void onBtnLogoutClick(View v) {
        RetrofitBindHelper.getInstance().doBind(CommonRestService.class).logout()
                .compose(ObservableHelper.<JsonResponse<String>>startReading())
                .compose(ObservableHelper2.<String>checkResult())
                .compose(ObservableHelper.<String>toMainThread())
                .subscribe(new RestSubscriber<String>() {
                    @Override
                    public void onNext(String s) {

                    }

                    @Override
                    public void onError(Throwable t) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    @Click(R.id.btn_session_timeout)
    void onBtnSessionTimeoutClick(View v) {
        RetrofitBindHelper.getInstance().doBind(CommonRestService.class).sessionTimeout()
                .compose(ObservableHelper.<JsonResponse<String>>startReading())
                .compose(ObservableHelper2.<String>checkResult())
                .compose(ObservableHelper.<String>checkSessionTimeout())
                .compose(ObservableHelper.<String>toMainThread())
                .subscribe(new RestSubscriber<String>() {
                    @Override
                    public void onNext(String s) {

                    }

                    @Override
                    public void onError(Throwable t) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    @Click(R.id.btn_get_list)
    void onBtnGetListClick(View v) {
        Map<String, String> param = new HashMap<>();
        RetrofitBindHelper.getInstance().doBind(CommonRestService.class).getList(param)
                .compose(ObservableHelper.<JsonResponse<List<String>>>startReading())
                .compose(ObservableHelper2.<List<String>>checkResult())
                .compose(ObservableHelper.<String>forEach())
                .compose(ObservableHelper.<String>toMainThread())
                .subscribe(new RestSubscriber<String>() {
                    @Override
                    public void onNext(String s) {
                        CLog.w(s);
                    }

                    @Override
                    public void onError(Throwable t) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

}
