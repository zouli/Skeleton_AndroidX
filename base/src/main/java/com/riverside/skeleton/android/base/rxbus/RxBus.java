package com.riverside.skeleton.android.base.rxbus;

import androidx.annotation.NonNull;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import io.reactivex.Flowable;
import io.reactivex.Scheduler;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.processors.FlowableProcessor;
import io.reactivex.processors.PublishProcessor;

/**
 * RxBus 1.1
 * 支持Sticky模式
 * <p>
 * b_e  2017/11/17
 * 1.1  2017/12/16 修改单例模式
 */
public class RxBus {
    private static volatile RxBus mRxBus = null;
    private FlowableProcessor<Object> mRxBusObservable;
    //Sticky数据保存对象
    private final Map<Class<?>, Object> stickyEventMap;

    private RxBus() {
        if (mRxBus != null) {
            throw new RuntimeException("请使用getInstance()取得对象");
        } else {
            mRxBusObservable = PublishProcessor.create().toSerialized();
            stickyEventMap = new ConcurrentHashMap<>();
        }
    }

    /**
     * 单例模式
     *
     * @return RxBus对象
     */
    public static RxBus getInstance() {
        if (mRxBus == null) {
            synchronized (RxBus.class) {
                if (mRxBus == null) {
                    mRxBus = new RxBus();
                }
            }
        }
        return mRxBus;
    }

    /**
     * 发送信息
     *
     * @param o 信息内容
     */
    public void post(@NonNull Object o) {
        mRxBusObservable.onNext(o);
    }

    /**
     * 发生信息 Sticky模式
     *
     * @param o 信息内容
     */
    public void postSticky(@NonNull Object o) {
        synchronized (stickyEventMap) {
            stickyEventMap.put(o.getClass(), o);
        }
        post(o);
    }

    /**
     * 注册接收者
     *
     * @param eventType 接收数据类型
     * @param next      接收事件
     * @return 观察者
     */
    public Disposable register(Class eventType, Consumer next) {
        return register(eventType, next, null);
    }

    /**
     * 注册接收者
     *
     * @param eventType 接收数据类型
     * @param next      接收事件
     * @param scheduler 调度器
     * @return 观察者
     */
    public Disposable register(Class eventType, Consumer next, Scheduler scheduler) {
        Flowable flowable = toStickyFlowable(eventType);
        if (scheduler != null) {
            flowable.observeOn(scheduler);
        }
        return flowable.subscribe(next);
    }

    /**
     * 注销观察者
     *
     * @param disposable 观察者
     */
    public void unRegister(Disposable disposable) {
        if (disposable != null && !disposable.isDisposed()) {
            disposable.dispose();
        }
    }

    /**
     * 生产观察者 Sticky模式
     *
     * @param eventType 接收数据类型
     * @return 观察者
     */
    private Flowable toStickyFlowable(Class eventType) {
        synchronized (stickyEventMap) {
            //取得最后一次接收到的数据，一般最后一次的数据就够用了
            Object stickyEvent = stickyEventMap.get(eventType);
            if (stickyEvent != null) {
                return Flowable.just(stickyEvent).mergeWith(toFlowable(eventType));
            }
        }
        return toFlowable(eventType);
    }

    /**
     * 生产观察者
     *
     * @param eventType 接收数据类型
     * @return 观察者
     */
    private Flowable toFlowable(Class eventType) {
        return mRxBusObservable.ofType(eventType);
    }

    /**
     * 判断是否有观察者
     *
     * @return true:有 false:没有
     */
    public boolean hasSubscribers() {
        return mRxBusObservable.hasSubscribers();
    }

    /**
     * 删除Sticky模式保存的数据
     *
     * @param eventType 接收数据类型
     */
    public void removeSticky(Class eventType) {
        synchronized (stickyEventMap) {
            stickyEventMap.remove(eventType);
        }
    }

    /**
     * 清空所有Sticky模式保存的数据
     */
    public void clearSticky() {
        synchronized (stickyEventMap) {
            stickyEventMap.clear();
        }
    }
}