package com.riverside.skeleton.android.net.rest.utils;

import com.riverside.skeleton.android.util.log.CLog;

import java.net.ConnectException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import io.reactivex.Flowable;
import io.reactivex.functions.BiFunction;
import io.reactivex.functions.Function;

/**
 * 网络连接重试策略类    1.0
 * b_e  2017/12/25
 */
public class RetryPolicy implements Function<Flowable<? extends Throwable>, Flowable<?>> {
    //retry次数
    private int count = 3;
    //延迟
    private long delay = 3000;
    //叠加延迟
    private long increaseDelay = 3000;

    public RetryPolicy(int count, long delay) {
        this.count = count;
        this.delay = delay;
    }

    public RetryPolicy(int count, long delay, long increaseDelay) {
        this.count = count;
        this.delay = delay;
        this.increaseDelay = increaseDelay;
    }

    @Override
    public Flowable<?> apply(Flowable<? extends Throwable> observable) {
        return observable
                .zipWith(Flowable.range(1, count + 1), new BiFunction<Throwable, Integer, ThrowableCountWrapper>() {
                    @Override
                    public ThrowableCountWrapper apply(Throwable throwable, Integer integer) throws Exception {
                        return new ThrowableCountWrapper(throwable, integer);
                    }
                }).flatMap(new Function<ThrowableCountWrapper, Flowable<?>>() {
                    @Override
                    public Flowable<?> apply(ThrowableCountWrapper wrapper) {
                        if ((wrapper.throwable instanceof ConnectException
//                                || wrapper.throwable instanceof SocketTimeoutException
                                || wrapper.throwable instanceof TimeoutException)
                                && wrapper.count < count + 1) {
                            //如果超出重试次数也抛出错误，否则默认是会进入onCompleted
                            CLog.e("在" + delay + "毫秒后重试,共" + count + "次");
                            return Flowable.timer(delay + (wrapper.count - 1) * increaseDelay, TimeUnit.MILLISECONDS);
                        }
                        return Flowable.error(wrapper.throwable);
                    }
                });
    }

    private class ThrowableCountWrapper {
        private int count;
        private Throwable throwable;

        public ThrowableCountWrapper(Throwable throwable, int count) {
            this.count = count;
            this.throwable = throwable;
        }
    }
}

