package com.riverside.skeleton.android.base.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;

import com.riverside.skeleton.android.base.rxbus.RxBus;
import com.riverside.skeleton.android.base.utils.KeyboardHelper;
import com.riverside.skeleton.android.base.utils.NoDoubleClickUtils;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.Scheduler;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

/**
 * Activity基类   1.0
 * <p>
 * b_e  2017/11/21
 */
public abstract class BaseActivity extends AppCompatActivity {
    protected BaseActivity activity;

    //RxBus接收对象
    private Disposable rxSubscription;


    //Fragment列表
    @SuppressLint("UseSparseArrays")
    private Map<Integer, Fragment> fragments = new HashMap<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initBase();
    }

    protected void initBase() {
        activity = this;
    }

    @Override
    protected void onResume() {
        super.onResume();
        //初始化防双击时间
        NoDoubleClickUtils.initLastClickTime();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
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

    /**
     * 取得根View
     *
     * @return
     */
    protected View getRootView() {
        return ((ViewGroup) activity.findViewById(android.R.id.content)).getChildAt(0);
    }

    /**
     * 验证方法
     * <p>
     * 在子类中重写doValidate()方法，填写验证逻辑
     *
     * @return
     */
    public boolean validate() {
        KeyboardHelper.init(activity).hideKeyboard();

        if (!NoDoubleClickUtils.isDoubleClick()) {
            boolean flag = doValidate();
            if (!flag) {
                NoDoubleClickUtils.initLastClickTime();
            }
            return flag;
        }
        return false;
    }

    /**
     * 验证方法实现方法
     *
     * @return
     */
    public boolean doValidate() {
        return true;
    }

    /**
     * 添加需要显示的Fragment
     *
     * @param containerViewId 用来显示Fragment的控件ID
     * @param fragment        需要显示的Fragment
     * @return 当期Fragment
     */
    public Fragment addFragment(int containerViewId, Fragment fragment) {
        // 将需要显示Fragment添加到列表
        if (fragments.containsKey(containerViewId)) {
            fragments.remove(containerViewId);
        }
        fragments.put(containerViewId, fragment);
        return fragment;
    }

    /**
     * 提交显示Fragment
     */
    public void commitFragment() {
        // 启动FragmentTransaction
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        // 添加需要显示的FragmentTransaction
        for (Map.Entry<Integer, Fragment> entry : fragments.entrySet()) {
            int id = entry.getKey();
            Fragment fragment = entry.getValue();
            fragmentTransaction.replace(id, fragment);
        }
        // 提交Fragment
        fragmentTransaction.commitAllowingStateLoss();
    }
}
