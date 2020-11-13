package com.riverside.skeleton.android.base.utils;

import com.riverside.skeleton.android.util.log.CLog;

/**
 * 二次点击判断类  1.0
 * b_e  2017/12/07
 */
public class NoDoubleClickUtils {
    private static long lastClickTime;
    private final static int SPACE_TIME = 2000;

    /**
     * 初始化点击时间
     */
    public static void initLastClickTime() {
        lastClickTime = 0;
    }

    /**
     * 是否为二次点击
     *
     * @return
     */
    public synchronized static boolean isDoubleClick() {
        long currentTime = System.currentTimeMillis();
        boolean isClick2;

        CLog.d("start currentTime=%d,lastClickTime=%d,c-l=%d", currentTime, lastClickTime, currentTime - lastClickTime);
        isClick2 = currentTime - lastClickTime <= SPACE_TIME;
        lastClickTime = currentTime;
        CLog.d("end currentTime=%d,lastClickTime=%d,c-l=%d,isClick2=%b", currentTime, lastClickTime, currentTime - lastClickTime, isClick2);

        return isClick2;
    }
}

