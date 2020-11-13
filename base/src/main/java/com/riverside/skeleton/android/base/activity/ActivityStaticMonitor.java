package com.riverside.skeleton.android.base.activity;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Bundle;

import java.util.ArrayList;

/**
 * Activity状态监控 1.1
 * <p>
 * b_e  2017/11/19
 * 1.1  2017/12/16 修改单例模式
 */
public class ActivityStaticMonitor implements Application.ActivityLifecycleCallbacks {
    private static volatile ActivityStaticMonitor instance = null;
    //当前在前台显示的Activity数量
    private static int activityActive;
    //前后台转换Listener列表
    private ArrayList<TransferListener> transferListenerList = null;

    private ActivityStaticMonitor() {
        if (instance != null) {
            throw new RuntimeException("请使用init()取得对象");
        }
    }

    /**
     * 初始化，单例模式
     *
     * @param application
     * @return
     */
    public static ActivityStaticMonitor init(Application application) {
        if (instance == null) {
            synchronized (ActivityStaticMonitor.class) {
                if (instance == null) {
                    instance = new ActivityStaticMonitor();
                }
            }
        }
        application.registerActivityLifecycleCallbacks(instance);
        return instance;
    }

    /**
     * 根据Application创建并返回对象
     *
     * @param application
     * @return
     */
    public static ActivityStaticMonitor get(Application application) {
        if (instance == null) {
            init(application);
        }
        return instance;
    }

    /**
     * 根据Context创建并返回对象
     *
     * @param ctx
     * @return
     */
    public static ActivityStaticMonitor get(Context ctx) {
        if (instance == null) {
            Context appCtx = ctx.getApplicationContext();
            if (appCtx instanceof Application) {
                init((Application) appCtx);
            } else {
                throw new IllegalStateException("ActivityStaticCallbacks初始化时无法获得Application对象");
            }
        }
        return instance;
    }

    /**
     * 返回对象，前提为已经创建对象
     *
     * @return
     */
    public static ActivityStaticMonitor get() {
        if (instance == null) {
            throw new IllegalStateException("ActivityStaticCallbacks未初始化 - 请先调用 init/get");
        }
        return instance;
    }

    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
        //加入生命周期管理
        ActivityStackManager.getInstance().addActivity(activity);
    }

    @Override
    public void onActivityStarted(Activity activity) {
        activityActive++;
        if (activityActive == 1) {
            //程序进入前台
            if (transferListenerList != null && transferListenerList.size() > 0) {
                //抄袭的android的处理方式，应该是防止多线程数据混乱
                ArrayList<TransferListener> tmpListeners = (ArrayList<TransferListener>) transferListenerList.clone();
                int numListeners = tmpListeners.size();
                for (int i = 0; i < numListeners; ++i) {
                    tmpListeners.get(i).onBecameForeground();
                }
            }
        }
    }

    @Override
    public void onActivityResumed(Activity activity) {

    }

    @Override
    public void onActivityPaused(Activity activity) {

    }

    @Override
    public void onActivityStopped(Activity activity) {
        activityActive--;
        if (activityActive == 0) {
            //程序进入后台
            if (transferListenerList != null && transferListenerList.size() > 0) {
                //抄袭的android的处理方式，应该是防止多线程数据混乱
                ArrayList<TransferListener> tmpListeners = (ArrayList<TransferListener>) transferListenerList.clone();
                int numListeners = tmpListeners.size();
                for (int i = 0; i < numListeners; ++i) {
                    tmpListeners.get(i).onBecameBackground();
                }
            }
        }
    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

    }

    @Override
    public void onActivityDestroyed(Activity activity) {
        //移除生命周期管理
        ActivityStackManager.getInstance().finishActivity(activity);
    }

    /**
     * 添加前后台转换监听
     *
     * @param listener
     */
    public void addTransferListener(TransferListener listener) {
        if (transferListenerList == null) {
            transferListenerList = new ArrayList<>();
        }
        transferListenerList.add(listener);
    }

    /**
     * 删除前后台转换监听
     *
     * @param listener
     */
    public void removeTransferListener(TransferListener listener) {
        if (transferListenerList == null) {
            return;
        }
        transferListenerList.remove(listener);
        if (transferListenerList.size() == 0) {
            transferListenerList = null;
        }
    }

    /**
     * 前后台转换监听接口
     */
    public interface TransferListener {
        /**
         * 程序进入前台
         */
        void onBecameForeground();

        /**
         * 程序进入后台
         */
        void onBecameBackground();
    }
}
