package com.riverside.skeleton.android.base.fragment;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.riverside.skeleton.android.base.activity.BaseActivity;
import com.riverside.skeleton.android.base.rxbus.RxBus;

import io.reactivex.Scheduler;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

/**
 * Fragment基类 1.0
 * <p>
 * b_e  2017/11/21
 */
public abstract class BaseFragment extends Fragment {
    protected BaseActivity activity;
    //RxBus接收对象
    private Disposable rxSubscription;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initBase();
    }

    protected void initBase() {
        activity = (BaseActivity) getActivity();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        //注销RxBus接收对象
        RxBus.getInstance().unRegister(rxSubscription);
    }

    /**
     * 注册RxBus接收对象
     *
     * @param eventType 消息类型
     * @param action1   回调函数
     */
    protected void registerRxBus(Class eventType, Consumer action1) {
        rxSubscription = RxBus.getInstance().register(eventType, action1);
    }

    /**
     * 注册RxBus接收对象
     *
     * @param eventType 消息类型
     * @param action1   回调函数
     * @param scheduler 线程调度器
     */
    protected void registerRxBus(Class eventType, Consumer action1, Scheduler scheduler) {
        rxSubscription = RxBus.getInstance().register(eventType, action1, scheduler);
    }
}
