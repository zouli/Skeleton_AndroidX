package com.riverside.skeleton.android.base.utils;

import android.view.KeyEvent;

import java.lang.ref.WeakReference;

/**
 * 返回键点击次数统计    1.0
 * b_e  2017/11/24
 */
public class BackButtonHelper {
    //最后一次点击的时间
    private static long lastTime = 0;
    //连续点击次数
    private static int hits = 0;

    public static boolean onKeyUp(int keyCode, KeyEvent event, Listener listener) {
        WeakReference<KeyEvent> keyEvent = new WeakReference<>(event);
        if (keyCode == KeyEvent.KEYCODE_BACK && keyEvent.get().getRepeatCount() == 0) {
            //点击间隔时间大于2秒重新计算
            if ((System.currentTimeMillis() - lastTime) > 2000) {
                hits = 0;
            }
            if (listener != null) {
                listener.onBackClick(++hits);
            }
            lastTime = System.currentTimeMillis();
            return true;
        }
        return false;
    }

    public interface Listener {
        void onBackClick(int hits);
    }
}
