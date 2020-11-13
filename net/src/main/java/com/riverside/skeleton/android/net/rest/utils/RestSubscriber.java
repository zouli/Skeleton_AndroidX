package com.riverside.skeleton.android.net.rest.utils;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

public class RestSubscriber<T> implements Subscriber<T> {
    @Override
    public void onSubscribe(Subscription s) {
        s.request(Long.MAX_VALUE);
    }

    @Override
    public void onNext(T t) {

    }

    @Override
    public void onError(Throwable t) {

    }

    @Override
    public void onComplete() {

    }
}