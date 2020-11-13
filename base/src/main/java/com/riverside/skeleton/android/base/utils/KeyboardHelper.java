package com.riverside.skeleton.android.base.utils;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import java.lang.ref.WeakReference;

/**
 * 软键盘帮助类   1.0
 * b_e  2017/12/07
 */
public class KeyboardHelper {
    private WeakReference<Activity> activity;

    private KeyboardHelper(WeakReference<Activity> activity) {
        this.activity = activity;
    }

    public static KeyboardHelper init(Activity activity) {
        return new KeyboardHelper(new WeakReference<>(activity));
    }

    /**
     * 取得InputMethodManager
     *
     * @return
     */
    private InputMethodManager getInputMethodManager() {
        return (InputMethodManager) activity.get().getSystemService(Context.INPUT_METHOD_SERVICE);
    }

    /**
     * 隐藏键盘
     */
    public void hideKeyboard() {
        View view = activity.get().getCurrentFocus();
        if (view != null) {
            getInputMethodManager().hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    /**
     * 显示虚拟键盘
     */
    public void showKeyboard() {
        View view = activity.get().getCurrentFocus();
        if (view != null) {
            getInputMethodManager().showSoftInput(view, InputMethodManager.SHOW_FORCED);
        }
    }

    /**
     * 键盘是否显示
     *
     * @return
     */
    public boolean isShow() {
        boolean flag = false;
        View view = activity.get().getCurrentFocus();
        if (view != null) {
            flag = getInputMethodManager().isActive();
        }
        return flag;
    }
}

