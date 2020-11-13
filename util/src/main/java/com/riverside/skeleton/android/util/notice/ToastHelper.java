package com.riverside.skeleton.android.util.notice;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;


/**
 * Toast共通类 1.2
 * b_e  2017/12/7
 * 1.1  修改构造方法
 * 1.2  修改显示逻辑
 */
public class ToastHelper {
    /**
     * 生成静态的Toast，保证在显示多个Toast的时候使用的是同一个对象
     */
    private static Toast toast = null;
    private Handler handler_ = new Handler(Looper.getMainLooper());
    private Context context_;

    private ToastHelper(Context context) {
        context_ = context;
    }

    public static ToastHelper getInstance(Context context) {
        return new ToastHelper(context);
    }

    /**
     * 显示Toast
     *
     * @param id String ID
     */
    public void showToast(int id) {
        showToast(context_.getString(id));
    }

    /**
     * 显示Toast
     *
     * @param info 需要显示的信息
     */
    public void showToast(final String info) {
        handler_.post(new Runnable() {
            @Override
            public void run() {
                // 判断是否有Toast正在显示
                if (toast != null) {
                    toast.cancel();
                }
                // 生成一个新的Toast
                toast = Toast.makeText(context_, info, Toast.LENGTH_LONG);
                // 显示Toast
                toast.show();
            }
        });
    }
}

