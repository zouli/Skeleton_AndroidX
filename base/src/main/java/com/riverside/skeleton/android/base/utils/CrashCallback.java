package com.riverside.skeleton.android.base.utils;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import androidx.core.content.ContextCompat;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.riverside.skeleton.android.base.application.BaseApplication;
import com.riverside.skeleton.android.base.utils.CollectInfo.CollectInfoHelper;
import com.riverside.skeleton.android.util.converter.DateUtils;
import com.riverside.skeleton.android.util.file.FileHelper;
import com.riverside.skeleton.android.util.file.FileUtils;
import com.riverside.skeleton.android.util.log.CLog;
import com.zxy.recovery.callback.RecoveryCallback;

import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Date;

/**
 * 全局异常捕捉回调    1.1
 * b_e 2017/12/06
 * 1.1  2017/12/12  在没有SD卡写入权限时的处理
 */
public class CrashCallback implements RecoveryCallback {
    private Context mContext;

    public CrashCallback(Context context) {
        mContext = context;
    }

    @Override
    public void stackTrace(String stackTrace) {
        if (CLog.isDebug()) {
            CLog.e("exceptionMessage:" + stackTrace);
        }
        // 收集设备参数信息
        String infos = CollectInfoHelper.collectInfo();
        // 保存日志文件
        String errorLog = saveCrashInfo2File(infos, stackTrace);
    }

    @Override
    public void cause(String cause) {
        if (CLog.isDebug()) {
            CLog.e("cause:" + cause);
        }
    }

    @Override
    public void exception(String throwExceptionType, String throwClassName, String throwMethodName, int throwLineNumber) {
        if (CLog.isDebug()) {
            CLog.e("exceptionClassName:" + throwExceptionType);
            CLog.e("throwClassName:" + throwClassName);
            CLog.e("throwMethodName:" + throwMethodName);
            CLog.e("throwLineNumber:" + throwLineNumber);
        }
    }

    @Override
    public void throwable(Throwable throwable) {
        if (CLog.isDebug()) {
            CLog.e(throwable.toString());
        }
    }


    /**
     * 保存错误信息到文件中
     *
     * @param description 其他信息
     * @param ex          错误信息
     * @return 返回文件内容, 便于将文件传送到服务器
     */
    private static String saveCrashInfo2File(String description, Throwable ex) {
        // 取得错误信息的完整信息
        Writer writer = new StringWriter();
        PrintWriter printWriter = new PrintWriter(writer);
        ex.printStackTrace(printWriter);
        Throwable cause = ex.getCause();
        while (cause != null) {
            cause.printStackTrace(printWriter);
            cause = cause.getCause();
        }
        printWriter.close();
        String result = writer.toString();

        return saveCrashInfo2File(description, result);
    }

    /**
     * 保存错误信息到文件中
     *
     * @param description
     * @param ex
     * @return
     */
    private static String saveCrashInfo2File(String description, String ex) {
        try {
            // 将其他信息转换为json对象
            JSONObject infos = JSON.parseObject(description);

            // 将错误信息保存到json对象中
            infos.put("cause", ex);

            // 取得日志文件名
            long timestamp = System.currentTimeMillis();
            String formatter = "yyyy-MM-dd-HH-mm-ss";
            String time = DateUtils.toString(new Date(), formatter);
            String fileName = "crash-" + time + "-" + timestamp + ".log";
            // 取得日志文件保存路径
            String path = BaseApplication.getInstance().getCacheDir().getPath() + "/";
            if (ContextCompat.checkSelfPermission(BaseApplication.getInstance()
                    , Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                path = FileHelper.mkdirs("crash");
            }

            // 生成日志文件
            FileOutputStream fos = new FileOutputStream(path + FileUtils.addSeparator(fileName));
            fos.write(infos.toString().getBytes());
            fos.close();

            return infos.toString();
        } catch (Exception e) {
            CLog.e("an error occured while writing file...", e);
        }
        return null;
    }
}
