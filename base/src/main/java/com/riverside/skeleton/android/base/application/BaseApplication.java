package com.riverside.skeleton.android.base.application;

import android.app.Application;
import android.content.Context;
import android.os.StrictMode;

import androidx.multidex.MultiDexApplication;

import com.riverside.skeleton.android.base.activity.ActivityStaticMonitor;
import com.riverside.skeleton.android.base.utils.CollectInfo.AppVersionInfo;
import com.riverside.skeleton.android.base.utils.CollectInfo.CollectInfoHelper;
import com.riverside.skeleton.android.base.utils.CollectInfo.DisplayInfo;
import com.riverside.skeleton.android.base.utils.CollectInfo.OSInfo;
import com.riverside.skeleton.android.base.utils.CollectInfo.OSVersionInfo;
import com.riverside.skeleton.android.base.utils.CollectInfo.TelephonyInfo;
import com.riverside.skeleton.android.base.utils.CrashCallback;
import com.riverside.skeleton.android.util.log.CLog;
import com.zxy.recovery.core.Recovery;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;

/**
 * Application基类    1.0
 * <p>
 * b_e 2017/11/20
 */
public class BaseApplication extends MultiDexApplication {
    private static BaseApplication instance;
    public List<Class<? extends Application>> moduleApplications = new ArrayList<>();
    private List<Application> moduleApplicationList = new ArrayList<>();

    @Override
    public void onCreate() {
        super.onCreate();

        //保存Application单例对象
        instance = this;

        CLog.init(getApplicationContext());

        //设置收集信息
        CollectInfoHelper.init(instance);
        CollectInfoHelper.addInfoSource(
                AppVersionInfo.class
                , OSInfo.class
                , OSVersionInfo.class
                , TelephonyInfo.class
                , DisplayInfo.class
        );

        if (CLog.isDebug()) {
            //开启Android的严格模式
            StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().detectAll().penaltyLog().build());
            StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder().detectAll().penaltyLog().build());
        } else {
            CLog.setLogLevel(CLog.LEVEL_ERROR);
        }

        //启动防灾模式
        Recovery.getInstance()
                .debug(CLog.isDebug())
                .recoverInBackground(true)
                .recoverStack(CLog.isDebug())
                .callback(new CrashCallback(getApplicationContext()))
                .silent(true, Recovery.SilentMode.RESTART)
                .init(this);

        //启动Activity状态监控
        ActivityStaticMonitor.init(this);

        Flowable.fromIterable(this.moduleApplicationList).forEach(new Consumer<Application>() {
            @Override
            public void accept(Application application) {
                application.onCreate();
            }
        });
    }

    /**
     * 取得Application单例对象
     *
     * @return
     */
    public static BaseApplication getInstance() {
        return instance;
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);

        //附加其他module的Application
        Flowable.fromIterable(this.moduleApplications)
                .map(new Function<Class<? extends Application>, Application>() {
                    @Override
                    public Application apply(Class<? extends Application> clazz) throws Exception {
                        Application moduleApplication = (Application) clazz.newInstance();
                        Method method = Application.class.getDeclaredMethod("attach", Context.class);
                        method.setAccessible(true);
                        method.invoke(moduleApplication, getBaseContext());
                        return moduleApplication;
                    }
                }).toList()
                .subscribe(new Consumer<List<Application>>() {
                    @Override
                    public void accept(List<Application> applications) {
                        moduleApplicationList.clear();
                        moduleApplicationList.addAll(applications);
                    }
                });
    }
}
