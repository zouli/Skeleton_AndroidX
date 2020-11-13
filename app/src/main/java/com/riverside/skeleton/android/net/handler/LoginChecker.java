package com.riverside.skeleton.android.net.handler;

import com.riverside.skeleton.android.net.jsonbean.JsonResponse;
import com.riverside.skeleton.android.net.rest.CommonRestService;
import com.riverside.skeleton.android.net.rest.ConstUrls2;
import com.riverside.skeleton.android.net.rest.ObservableHelper2;
import com.riverside.skeleton.android.net.rest.handler.SessionHandler;
import com.riverside.skeleton.android.net.rest.utils.ObservableHelper;
import com.riverside.skeleton.android.net.rest.utils.RetrofitBindHelper;
import com.riverside.skeleton.android.util.converter.DateUtils;

import java.util.Date;

import io.reactivex.Flowable;

public class LoginChecker implements SessionHandler {
    @Override
    public boolean canToSave(String url) {
        return url.contains(ConstUrls2.LOGIN);
    }

    @Override
    public Flowable<?> reLogin() {
        return RetrofitBindHelper.getInstance().doBind(CommonRestService.class).login()
                .compose(ObservableHelper.<JsonResponse<String>>startReading())
                .compose(ObservableHelper2.<String>checkResult());
    }

    @Override
    public String getHeaderValue(String key) {
        return DateUtils.toString(new Date(), DateUtils.DATE_FORMAT_PATTERN2);
    }
}
