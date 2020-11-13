package com.riverside.skeleton.android.base.activity;

import android.app.Activity;
import android.app.Application;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Activity生存周期管理类 1.2
 * <p>
 * b_e  2017/11/19
 * 1.1  2017/11/20 添加toLogin方法
 * 1.2  2017/12/16 修改单列模式
 */
public class ActivityStackManager {
    private static volatile ActivityStackManager instance = null;
    //Activity列表
    private static List<Activity> activityList = Collections.synchronizedList(new LinkedList<Activity>());
    //多重关闭开始标记，关闭后的当前画面index
    private static int multipleFinishFlag = -1;

    private ActivityStackManager() {
        if (instance != null) {
            throw new RuntimeException("请使用getInstance()取得对象");
        }
    }

    /**
     * 单例模式
     *
     * @return ActivityStackManager对象
     */
    public static ActivityStackManager getInstance() {
        if (instance == null) {
            synchronized (ActivityStackManager.class) {
                if (instance == null) {
                    instance = new ActivityStackManager();
                }
            }
        }
        return instance;
    }

    /**
     * 加入列表
     *
     * @param activity
     */
    public void addActivity(Activity activity) {
        activityList.add(activity);
    }

    /**
     * 当前Activity
     *
     * @return
     */
    public Activity currentActivity() {
        if (activityList == null || activityList.isEmpty()) {
            return null;
        }
        return activityList.get(activityList.size() - 1);
    }

    /**
     * 列表中Activity数量
     *
     * @return
     */
    public int countActivity() {
        return activityList.size();
    }

    /**
     * 开启多重关闭模式，执行多重关闭(doMultipleFinish)后会显示当前画面
     */
    public void startMultipleFinish() {
        if (activityList == null || activityList.isEmpty()) {
            return;
        }
        for (int i = activityList.size() - 1; i > multipleFinishFlag; i--) {
            //防止有activity正在关闭中
            if (activityList.get(i).isFinishing()) continue;
            multipleFinishFlag = i;
            break;
        }
    }

    /**
     * 执行多重关闭
     */
    public void doMultipleFinish() {
        if (activityList == null || activityList.isEmpty()) {
            return;
        }
        if (multipleFinishFlag == -1) {
            return;
        }
        for (int i = activityList.size() - 1; i > multipleFinishFlag; i--) {
            finishActivity(activityList.get(i));
        }
    }

    /**
     * 从列表中删除Activity，并关闭Activity
     *
     * @param activity
     */
    public void finishActivity(Activity activity) {
        if (activityList == null || activityList.isEmpty()) {
            return;
        }
        if (activity != null && activityList.contains(activity)) {
            activityList.remove(activity);
            activity.finish();
        }
    }

    /**
     * 关闭所有Activity
     */
    public void finishAllActivity() {
        for (Activity activity : activityList) {
            if (activity != null) {
                activity.finish();
            }
        }
        activityList.clear();
    }

    /**
     * 跳转到Login窗体
     * <p>
     * 使用前需要在AndroidManifest.xml的
     * LoginActivity的<intent-filter></intent-filter>中
     * 添加<action android:name="${applicationId}.LOGIN_ACTIVITY" />
     *
     * @param closeAll
     */
    public void toLoginActivity(boolean closeAll) {
        Application app = currentActivity().getApplication();

        Intent intent = new Intent();
        intent.setAction(app.getPackageName() + ".LOGIN_ACTIVITY");
        List<ResolveInfo> activities = app.getPackageManager().queryIntentActivities(intent, PackageManager.GET_META_DATA);

        for (ResolveInfo resolveInfo : activities) {
            if (!currentActivity().getComponentName().getClassName().equals(resolveInfo.activityInfo.name)
                    || currentActivity().isFinishing()) {
                if (closeAll) {
                    finishAllActivity();
                }

                Intent intentLogin = new Intent();
                intentLogin.setClassName(resolveInfo.activityInfo.packageName, resolveInfo.activityInfo.name);
                if (closeAll) {
                    intentLogin.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    app.startActivity(intentLogin);
                } else {
                    currentActivity().startActivity(intentLogin);
                }

                return;
            }
        }
    }
}

