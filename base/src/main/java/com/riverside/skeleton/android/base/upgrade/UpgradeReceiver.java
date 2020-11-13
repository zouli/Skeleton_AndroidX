package com.riverside.skeleton.android.base.upgrade;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import com.riverside.skeleton.android.base.activity.ActivityStackManager;
import com.riverside.skeleton.android.base.application.BaseApplication;
import com.riverside.skeleton.android.util.file.FileHelper;

/**
 * 自动升级监听   1.0
 * b_e  2017/11/23
 */
public class UpgradeReceiver extends BroadcastReceiver {
    public final static String DOWNLOAD_UPGRADE_OVER = BaseApplication.getInstance().getPackageName() + ".upgrade_over";
    public final static String PATH_EXTRA = "path";

    private Context context;

    @Override
    public void onReceive(Context context, Intent intent) {
        this.context = context;

        String action = intent.getAction();
        if (DOWNLOAD_UPGRADE_OVER.equals(action)) {
            Bundle extras_ = ((intent.getExtras() != null) ? intent.getExtras() : new Bundle());
            String path = extras_.getString(PATH_EXTRA);
            doUpgrade(path);
        }
    }

    /**
     * 接受升级消息
     *
     * @param path 文件路径
     */
    void doUpgrade(String path) {
        Intent intent = new Intent();
        // 设置启动新程序
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        // 启动ACTION_VIEW
        intent.setAction(android.content.Intent.ACTION_VIEW);
        //判读版本是否在7.0以上
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            //Granting Temporary Permissions to a URI
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        }
        // 设置启动功能的路径
        intent.setDataAndType(FileHelper.getFileUri(context, path), "application/vnd.android.package-archive");

        BaseApplication.getInstance().startActivity(intent);
        ActivityStackManager.getInstance().finishAllActivity();
    }
}

