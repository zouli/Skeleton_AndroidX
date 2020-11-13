package com.riverside.skeleton.android.net.rest.utils;

import com.riverside.skeleton.android.base.activity.ActivityStackManager;
import com.riverside.skeleton.android.net.rest.error.SessionTimeoutThrowable;
import com.riverside.skeleton.android.net.rest.handler.SessionHandlerFactory;

import io.reactivex.Flowable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;

/**
 * Session超时策略类    1.0
 * b_e  2017/12/25
 */
public class SessionTimeoutPolicy implements Function<Flowable<? extends Throwable>, Flowable<?>> {
    @Override
    public Flowable<?> apply(Flowable<? extends Throwable> observable) {
        return observable.flatMap(new Function<Throwable, Flowable<?>>() {
            @Override
            public Flowable<?> apply(Throwable throwable) {
                if (throwable instanceof SessionTimeoutThrowable) {
                    // 重新登录
                    return SessionHandlerFactory.reLogin()
                            .doOnError(new Consumer<Throwable>() {
                                @Override
                                public void accept(Throwable mRefreshTokenError) throws Exception {
                                    if (mRefreshTokenError instanceof SessionTimeoutThrowable) {
                                        //跳转到登录画面
                                        ActivityStackManager.getInstance().toLoginActivity(false);
                                    } else {
                                        Flowable.error(mRefreshTokenError);
                                    }
                                }
                            });
                }
                return Flowable.error(throwable);
            }
        });
    }
}

