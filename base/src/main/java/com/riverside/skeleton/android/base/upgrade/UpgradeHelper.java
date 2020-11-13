package com.riverside.skeleton.android.base.upgrade;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import com.riverside.skeleton.android.base.application.BaseApplication;
import com.tbruyelle.rxpermissions2.RxPermissions;

import io.reactivex.functions.Consumer;

/**
 * 自动升级服务   1.0
 * b_e  2019/04/10
 */
public class UpgradeHelper {
    private static final int REQUEST_CODE_UNKNOWN_APP = 100;

    private Activity activity;
    private Intent intent;
    private String title;
    private String message;
    private String downloadUrl;

    public UpgradeHelper(Activity activity) {
        this.activity = activity;
        intent = new Intent(activity, UpgradeService.class);
    }

    /**
     * 开始升级
     *
     * @param title
     * @param message
     * @param downloadUrl
     */
    public void startUpgrade(final String title, final String message, final String downloadUrl) {
        this.title = title;
        this.message = message;
        this.downloadUrl = downloadUrl;
        checkPermission();
    }

    /**
     * 验证安装权限
     */
    private void checkPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            boolean hasInstallPermission = BaseApplication.getInstance().getPackageManager().canRequestPackageInstalls();
            if (hasInstallPermission) {
                doUpgrade();
            } else {
                //跳转至“安装未知应用”权限界面，引导用户开启权限
                Uri selfPackageUri = Uri.parse("package:" + activity.getPackageName());
                Intent intent = new Intent(Settings.ACTION_MANAGE_UNKNOWN_APP_SOURCES, selfPackageUri);
                activity.startActivityForResult(intent, REQUEST_CODE_UNKNOWN_APP);
            }
        } else {
            doUpgrade();
        }
    }

    /**
     * 开始下载APK
     */
    private void doUpgrade() {
        RxPermissions permissions = new RxPermissions((FragmentActivity) activity);
        permissions.request(Manifest.permission.WRITE_EXTERNAL_STORAGE).subscribe(new Consumer<Boolean>() {
            @Override
            public void accept(Boolean has) throws Exception {
                if (has) {
                    intent.setAction(UpgradeService.ACTION_DOWNLOAD_URL);
                    intent.putExtra(UpgradeService.TITLE_EXTRA, title);
                    intent.putExtra(UpgradeService.MESSAGE_EXTRA, message);
                    intent.putExtra(UpgradeService.DOWNLOAD_URL_EXTRA, downloadUrl);
                    activity.startService(intent);
                } else {
                    ActivityCompat.requestPermissions(activity
                            , new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 101);
                }
            }
        });
    }

    /**
     * 停止升级
     */
    public void stopUpgrade() {
        activity.stopService(intent);
    }

    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == REQUEST_CODE_UNKNOWN_APP) {
            checkPermission();
        }
    }
}
