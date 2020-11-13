package com.riverside.skeleton.android.net.rest;

import android.text.TextUtils;

import com.riverside.skeleton.android.net.jsonbean.JsonResponse;
import com.riverside.skeleton.android.net.rest.error.RestThrowable;
import com.riverside.skeleton.android.net.rest.error.SessionTimeoutThrowable;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter;
import io.reactivex.FlowableOnSubscribe;
import io.reactivex.FlowableTransformer;
import io.reactivex.functions.Function;

public class ObservableHelper2 {
    public static <T> FlowableTransformer<JsonResponse<T>, T> checkResult() {
        return new FlowableTransformer<JsonResponse<T>, T>() {
            @Override
            public Flowable<T> apply(Flowable<JsonResponse<T>> entity) {
                return entity
                        .flatMap(new Function<JsonResponse<T>, Flowable<T>>() {
                            @Override
                            public Flowable<T> apply(final JsonResponse<T> response) throws Exception {
                                return Flowable.create(new FlowableOnSubscribe<T>() {
                                    @Override
                                    public void subscribe(FlowableEmitter<T> subscriber) throws Exception {
                                        if (response.getResultflag() != null) {
                                            if (response.getResultflag().equals("1")) {
                                                if (response.getIs_default_pw() == null || response.getIs_default_pw().equals("0")) {
                                                    subscriber.onComplete();
                                                } else {
                                                    String error_msg = "密码错误";
                                                    subscriber.onError(new RestThrowable(error_msg, "1001"));
                                                }
                                            } else {
                                                String error_msg = "密码错误";
                                                if (!TextUtils.isEmpty(response.getError_msg())) {
                                                    error_msg = response.getError_msg();
                                                }
                                                subscriber.onError(new RestThrowable(error_msg, response.getError_code()));
                                            }
                                        } else {
                                            if (response.getError_code().equals("1")) {
                                                subscriber.onNext(response.getBus_json());
                                                subscriber.onComplete();
                                            } else if (response.getError_code().equals("-1") && response.getError_msg().equals("SESSION TIMEOUT")) {
                                                subscriber.onError(new SessionTimeoutThrowable(response.getError_msg(), response.getError_code()));
                                            } else {
                                                subscriber.onError(new RestThrowable(response.getError_msg(), response.getError_code()));
                                            }
                                        }
                                    }
                                    //TODO:背压策略
                                }, BackpressureStrategy.DROP);
                            }
                        });
            }
        };
    }
}
