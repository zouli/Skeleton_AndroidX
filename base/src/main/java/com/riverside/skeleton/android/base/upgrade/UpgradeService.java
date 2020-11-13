package com.riverside.skeleton.android.base.upgrade;

import android.Manifest;
import android.app.DownloadManager;
import android.app.IntentService;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import androidx.core.content.ContextCompat;
import android.text.TextUtils;

import com.riverside.skeleton.android.base.R;
import com.riverside.skeleton.android.base.activity.ActivityStackManager;
import com.riverside.skeleton.android.base.application.BaseApplication;
import com.riverside.skeleton.android.util.file.FileHelper;
import com.riverside.skeleton.android.util.file.FileUtils;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;

/**
 * 自动升级服务   1.0
 * b_e  2017/11/23
 */
public class UpgradeService extends IntentService {

    /**
     * 系统下载服务上下文URI
     */
    public static final Uri CONTENT_URI = Uri.parse("content://downloads/my_downloads");
    /**
     * 下载出现错误
     */
    public static final String DOWNLOAD_ERROR = BaseApplication.getInstance().getPackageName() + ".download_error";

    public final static String ACTION_DOWNLOAD_URL = "DownloadUrl";
    public final static String TITLE_EXTRA = "title";
    public final static String MESSAGE_EXTRA = "message";
    public final static String DOWNLOAD_URL_EXTRA = "downloadUrl";

    /**
     * 引用系统下载服务
     */
    DownloadManager downloadManager;

    // {{ 参数
    // 下载窗口标题
    private String title;
    // 下载窗口信息
    private String message;
    // 下载地址
    private String downloadUrl;
    // 文件下载ID，用于和系统下载服务通信
    private long lastDownloadId;
    // 文件下载监控服务
    private DownloadChangeObserver downloadObserver;
    // 下载窗口
    private ProgressDialog downloadDialog;
    // }}

    public UpgradeService() {
        super(UpgradeService.class.getSimpleName());
    }

    @Override
    public void onCreate() {
        downloadManager = ((DownloadManager) this.getSystemService(Context.DOWNLOAD_SERVICE));
        super.onCreate();
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        String action = intent.getAction();
        if (ACTION_DOWNLOAD_URL.equals(action)) {
            Bundle extras = intent.getExtras();
            if (extras != null) {
                String titleExtra = extras.getString(TITLE_EXTRA);
                String messageExtra = extras.getString(MESSAGE_EXTRA);
                String downloadUrlExtra = extras.getString(DOWNLOAD_URL_EXTRA);
                DownloadUrl(titleExtra, messageExtra, downloadUrlExtra);
            }
        }
    }

    void DownloadUrl(String title, String message, String downloadUrl) {
        // 保存参数
        this.title = title;
        this.message = message;
        this.downloadUrl = downloadUrl;

        // 显示下载窗口
        showDialog();
        // 启动下载服务
        startDownload();
    }

    /**
     * 显示下载窗口
     */
    public void showDialog() {
        Observable.just("")
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Exception {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            downloadDialog = new ProgressDialog(ActivityStackManager.getInstance().currentActivity(), R.style.Colored_Dialog_Alert);
                        } else {
                            downloadDialog = new ProgressDialog(ActivityStackManager.getInstance().currentActivity());
                        }
                        downloadDialog.setTitle(title);
                        if (!TextUtils.isEmpty(message)) {
                            downloadDialog.setMessage(message);
                        }
                        downloadDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                            @Override
                            public void onCancel(DialogInterface dialog) {
                                cancelDownload();
                            }
                        });
                        downloadDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                        downloadDialog.setCanceledOnTouchOutside(false);
                        downloadDialog.setProgressNumberFormat("");
                        downloadDialog.show();
                    }
                });
    }

    /**
     * 取消下载
     */
    protected void cancelDownload() {
        // 关闭窗口
        downloadDialog.dismiss();
        // 取消系统下载任务
        downloadManager.remove(lastDownloadId);
        // 结束程序
        ActivityStackManager.getInstance().finishAllActivity();
    }

    /**
     * 设置进度条的进度
     *
     * @param progress 当前进度 1~100
     */
    protected void setProgress(final int progress) {
        Observable.just("")
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Exception {
                        downloadDialog.setProgress(progress);
                    }
                });
    }

    /**
     * 启动系统下载服务
     */
    private void startDownload() {
        DownloadManager.Request down;
        try {
            // 下载设置
            down = new DownloadManager.Request(Uri.parse(this.downloadUrl));
        } catch (IllegalArgumentException e) {
            // 发送错误消息
            Intent intent = new Intent(DOWNLOAD_ERROR);
            intent.putExtra("error", e);
            sendBroadcast(intent);

            // 关闭下载窗口
            downloadDialog.dismiss();

            return;
        }

        // 设置允许使用的网络类型，wifi
        down.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_MOBILE | DownloadManager.Request.NETWORK_WIFI);
        // 发出通知
        down.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE);
        // 显示下载界面
        down.setVisibleInDownloadsUi(true);

        if (ContextCompat.checkSelfPermission(getApplication(), Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            // 设置文件保存路径
            FileHelper.mkdirs("upgrade");
            String filepath = FileHelper.getPath("upgrade");
            String path = FileHelper.getPathWithoutSD("upgrade");
            // 设置文件名
            String filename = "upgrade.apk";
            // 设置备份文件名
            String filenameBak = "upgradebak.apk";

            // 备份上一版安装程序
            FileUtils.delete(filepath + FileUtils.addSeparator(filenameBak));
            FileUtils.rename(filepath + FileUtils.addSeparator(filename)
                    , filepath + FileUtils.addSeparator(filenameBak));

            // 设置下载后文件存放的位置
            down.setDestinationInExternalPublicDir(path, filename);
        }

        // 将下载请求放入队列
        lastDownloadId = downloadManager.enqueue(down);

        // 启动下载监控服务
        downloadObserver = new DownloadChangeObserver(null);
        getContentResolver().registerContentObserver(CONTENT_URI, true, downloadObserver);
    }

    /**
     * 下载完成
     *
     * @param filename 文件名
     */
    protected void downloadOver(final String filename) {
        Observable.just("")
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Exception {
                        // 关闭下载窗口
                        downloadDialog.dismiss();

                        // 取消系统下载任务
                        // downloadManager.remove(lastDownloadId);

                        // 发送文件下载完成消息
                        Intent intent = new Intent(UpgradeReceiver.DOWNLOAD_UPGRADE_OVER);
                        intent.setPackage(getPackageName());
                        intent.putExtra("path", filename);
                        sendBroadcast(intent);
                    }
                });
    }

    /**
     * 查看当前下载状态
     *
     * @param id 下载文件ID
     */
    private void queryDownloadStatus(long id) {
        // 取得当前下载文件状态
        DownloadManager.Query query = new DownloadManager.Query();
        query.setFilterById(id);
        // 查询下载状态
        Cursor cursor = downloadManager.query(query);
        if (cursor != null && cursor.moveToFirst()) {
            // 当前下载状态ID
            int columnIndex = cursor.getColumnIndex(DownloadManager.COLUMN_STATUS);
            // 当前下载状态
            int status = cursor.getInt(columnIndex);
            // 当前状态理由ID
            int columnReason = cursor.getColumnIndex(DownloadManager.COLUMN_REASON);
            // 当前状态理由
            int reason = cursor.getInt(columnReason);

            String filename = "";
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
                // 当前下载文件名ID
                int fileUriIdx = cursor.getColumnIndex(DownloadManager.COLUMN_LOCAL_URI);
                // 当前下载文件名
                String fileUri = cursor.getString(fileUriIdx);
                if (fileUri != null) {
                    filename = Uri.parse(fileUri).getPath();
                }
            } else {
                // 当前下载文件名ID
                int fileNameIdx = cursor.getColumnIndex(DownloadManager.COLUMN_LOCAL_FILENAME);
                // 当前下载文件名
                filename = cursor.getString(fileNameIdx);
            }

            // 文件大小ID
            int fileSizeIdx = cursor.getColumnIndex(DownloadManager.COLUMN_TOTAL_SIZE_BYTES);
            // 文件大小
            int fileSize = cursor.getInt(fileSizeIdx);
            // 已下载的文件大小ID
            int bytesDLIdx = cursor.getColumnIndex(DownloadManager.COLUMN_BYTES_DOWNLOADED_SO_FAR);
            // 已下载的文件大小
            int bytesDL = cursor.getInt(bytesDLIdx);

            // TODO:下载的状态是不是都需要处理呢？
            switch (status) {
                case DownloadManager.STATUS_FAILED:
                    switch (reason) {
                        case DownloadManager.ERROR_CANNOT_RESUME:
                            // some possibly transient error occurred but we can't
                            // resume the download
                            break;
                        case DownloadManager.ERROR_DEVICE_NOT_FOUND:
                            // no external storage device was found. Typically, this is
                            // because the SD card is not mounted
                            break;
                        case DownloadManager.ERROR_FILE_ALREADY_EXISTS:
                            // the requested destination file already exists (the
                            // download manager will not overwrite an existing file)
                            break;
                        case DownloadManager.ERROR_FILE_ERROR:
                            // a storage issue arises which doesn't fit under any other
                            // error code
                            break;
                        case DownloadManager.ERROR_HTTP_DATA_ERROR:
                            // an error receiving or processing data occurred at the
                            // HTTP level
                            break;
                        case DownloadManager.ERROR_INSUFFICIENT_SPACE:// sd卡满了
                            // here was insufficient storage space. Typically, this is
                            // because the SD card is full
                            break;
                        case DownloadManager.ERROR_TOO_MANY_REDIRECTS:
                            // there were too many redirects
                            break;
                        case DownloadManager.ERROR_UNHANDLED_HTTP_CODE:
                            // an HTTP code was received that download manager can't
                            // handle
                            break;
                        case DownloadManager.ERROR_UNKNOWN:
                            // he download has completed with an error that doesn't fit
                            // under any other error code
                            break;
                    }
                    // isNeedDownloadAgain = true;
                    break;
                case DownloadManager.STATUS_PAUSED:

                    switch (reason) {
                        case DownloadManager.PAUSED_QUEUED_FOR_WIFI:
                            // the download exceeds a size limit for downloads over the
                            // mobile network and the download manager is waiting for a
                            // Wi-Fi connection to proceed

                            break;
                        case DownloadManager.PAUSED_UNKNOWN:
                            // the download is paused for some other reason
                            break;
                        case DownloadManager.PAUSED_WAITING_FOR_NETWORK:
                            // the download is waiting for network connectivity to
                            // proceed
                            break;
                        case DownloadManager.PAUSED_WAITING_TO_RETRY:
                            // the download is paused because some network error
                            // occurred and the download manager is waiting before
                            // retrying the request
                            break;
                    }
                    // isNeedDownloadAgain = false;
                    break;
                case DownloadManager.STATUS_PENDING:
                    // the download is waiting to start
                    // isNeedDownloadAgain = false;
                    break;
                case DownloadManager.STATUS_RUNNING:
                    // the download is currently running
                    // isNeedDownloadAgain = false;

                    // 设置当前下载进度
                    int progress = (int) (((float) bytesDL / fileSize) * 100);
                    setProgress(progress);
                    break;
                case DownloadManager.STATUS_SUCCESSFUL:
                    // the download has successfully completed
                    // isNeedDownloadAgain = false;

                    // 下载完成
                    downloadOver(filename);
                    break;
            }
        }
    }

    /**
     * 系统下载服务监控类
     *
     * @author Zouli
     */
    class DownloadChangeObserver extends ContentObserver {

        public DownloadChangeObserver(Handler handler) {
            super(handler);
        }

        /**
         * 状态监控
         */
        @Override
        public void onChange(boolean selfChange) {
            // 检测当前下载状态
            queryDownloadStatus(lastDownloadId);
        }
    }

}

