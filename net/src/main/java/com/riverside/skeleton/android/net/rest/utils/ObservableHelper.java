package com.riverside.skeleton.android.net.rest.utils;

import android.util.Pair;

import com.riverside.skeleton.android.net.ConstUrls;

import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.FlowableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.BiFunction;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * 观察者帮助类   1.0
 * b_e  2017/12/25
 */
public class ObservableHelper {
    /**
     * 开始读取网络数据
     *
     * @param <T>
     * @return
     */
    public static <T> FlowableTransformer<T, T> startReading() {
        return new FlowableTransformer<T, T>() {
            @Override
            public Flowable<T> apply(Flowable<T> entity) {
                return entity
                        .subscribeOn(Schedulers.io())
                        .retryWhen(new RetryPolicy(ConstUrls.CONNECT_RETRY_COUNT, ConstUrls.CONNECT_RETRY_DELAY));
            }
        };
    }

    /**
     * 检查Session是否超时
     *
     * @param <T>
     * @return
     */
    public static <T> FlowableTransformer<T, T> checkSessionTimeout() {
        return new FlowableTransformer<T, T>() {
            @Override
            public Flowable<T> apply(Flowable<T> entity) {
                return entity
                        .subscribeOn(Schedulers.io())
                        .retryWhen(new SessionTimeoutPolicy());
            }
        };
    }

    /**
     * 遍历List对象
     *
     * @param <T>
     * @return
     */
    public static <T> FlowableTransformer<List<T>, T> forEach() {
        return new FlowableTransformer<List<T>, T>() {
            @Override
            public Flowable<T> apply(Flowable<List<T>> entity) {
                return entity
                        .flatMap(new Function<List<T>, Flowable<T>>() {
                            @Override
                            public Flowable<T> apply(List<T> list) {
                                return Flowable.fromIterable(list);
                            }
                        });
            }
        };
    }

    /**
     * 进入主线程
     *
     * @param <T>
     * @return
     */
    public static <T> FlowableTransformer<T, T> toMainThread() {
        return new FlowableTransformer<T, T>() {
            @Override
            public Flowable<T> apply(Flowable<T> entity) {
                return entity
                        .subscribeOn(Schedulers.computation())
                        .observeOn(AndroidSchedulers.mainThread())
                        .unsubscribeOn(Schedulers.io());
            }
        };
    }

    /**
     * 添加索引
     *
     * @param indexs
     * @param <T>
     * @return
     */
    public static <T> FlowableTransformer<T, Pair<Integer, T>> indexed(final Flowable<Integer> indexs) {
        return new FlowableTransformer<T, Pair<Integer, T>>() {
            @Override
            public Flowable<Pair<Integer, T>> apply(Flowable<T> entity) {
                return entity.zipWith(indexs, new BiFunction<T, Integer, Pair<Integer, T>>() {
                    @Override
                    public Pair<Integer, T> apply(T item, Integer index) {
                        return new Pair<>(index, item);
                    }
                });
            }
        };
    }
}
