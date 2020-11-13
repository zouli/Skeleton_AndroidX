package com.riverside.skeleton.android.util.notice;

import android.app.Activity;
import android.content.Context;
import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.annotation.StringRes;
import com.google.android.material.snackbar.Snackbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.riverside.skeleton.android.util.R;

/**
 * Snackbar帮助类  1.0
 * b_e                            2017/12/07
 * 添加LENGTH_SHORT、LENGTH_LONG   2018/02/08
 */
public class SnackbarHelper {
    public static int LENGTH_SHORT = 1777;
    public static int LENGTH_LONG = 3034;

    //当前Activity
    private Context mActivity = null;
    //当前Activity的根View
    private View rootView = null;
    //Snackbar的显示回调
    private Snackbar.Callback snackbarCallback = null;
    //Action的文字
    private String actionString;
    //Action文字的颜色
    private Integer actionStringColor = null;
    //Action的点击事件
    private View.OnClickListener actionListener = null;
    //文字的颜色
    private Integer messageTextColor = null;

    private SnackbarHelper() {
    }

    /**
     * 开始创建Snackbar
     *
     * @param activity
     * @return
     */
    public static SnackbarHelper Builder(@NonNull Context activity) {
        SnackbarHelper snackbarHelper = new SnackbarHelper();
        snackbarHelper.mActivity = activity;
        //取得Activity的根View
        return snackbarHelper.setView(((Activity) activity).getWindow().getDecorView().findViewById(android.R.id.content));
    }

    /**
     * 设置Activity的根View
     *
     * @param view
     * @return
     */
    private SnackbarHelper setView(@NonNull View view) {
        rootView = view;
        return this;
    }

    /**
     * 设置显示回调
     *
     * @param callback
     * @return
     */
    public SnackbarHelper setCallback(Snackbar.Callback callback) {
        this.snackbarCallback = callback;
        return this;
    }

    /**
     * 设置Action的文字与点击事件
     *
     * @param stringId
     * @param listener
     * @return
     */
    public SnackbarHelper setAction(@StringRes int stringId, View.OnClickListener listener) {
        return setAction(mActivity.getResources().getString(stringId), listener);
    }

    /**
     * 设置Action的文字与点击事件
     *
     * @param title
     * @param listener
     * @return
     */
    public SnackbarHelper setAction(String title, View.OnClickListener listener) {
        actionString = title;
        actionListener = listener;
        return this;
    }

    /**
     * 设置Action文字的颜色
     *
     * @param color
     * @return
     */
    public SnackbarHelper setActionColor(@ColorInt int color) {
        actionStringColor = color;
        return this;
    }

    /**
     * 设置文字的颜色
     *
     * @param color
     * @return
     */
    public SnackbarHelper setMessageTextColor(@ColorInt int color) {
        messageTextColor = color;
        return this;
    }

    /**
     * 显示Snackbar
     *
     * @param messageId
     * @param duration
     */
    public void showSnackbar(@StringRes int messageId, @Snackbar.Duration int duration) {
        showSnackbar(mActivity.getResources().getString(messageId), duration);
    }

    /**
     * 显示Snackbar
     *
     * @param message
     * @param duration
     */
    public void showSnackbar(@NonNull String message, @Snackbar.Duration int duration) {
        Snackbar snackbar = Snackbar.make(rootView, message, duration);
        if (actionStringColor != null) {
            snackbar.setActionTextColor(actionStringColor);
        }
        if (messageTextColor != null) {
            setSnackbarMessageTextColor(snackbar, messageTextColor);
        }
        if (!TextUtils.isEmpty(actionString)) {
            snackbar.setAction(actionString, actionListener);
        }
        if (snackbarCallback != null) {
            snackbar.removeCallback(snackbarCallback);
            snackbar.addCallback(snackbarCallback);
        }
        snackbar.show();
    }

    /**
     * 修改文字颜色
     *
     * @param snackbar
     * @param color
     */
    private void setSnackbarMessageTextColor(Snackbar snackbar, int color) {
        View view = snackbar.getView();
        ((TextView) view.findViewById(R.id.snackbar_text)).setTextColor(color);
    }
}
