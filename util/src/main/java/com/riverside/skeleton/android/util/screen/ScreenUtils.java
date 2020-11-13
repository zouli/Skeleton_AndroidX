package com.riverside.skeleton.android.util.screen;

import android.app.Activity;
import android.content.Context;
import android.graphics.Point;
import android.view.Window;
import android.view.WindowManager;

/**
 * 屏幕信息工具栏  1.0
 * b_e  2018/08/16
 */
public class ScreenUtils {

    /**
     * 获得设备的屏幕宽度
     *
     * @param context
     * @return
     */
    public static int getDeviceWidth(Context context) {
        return getDeviceSize(context).x;
    }

    /**
     * 获取设备的高宽
     *
     * @param context
     * @return
     */
    public static Point getDeviceSize(Context context) {
        WindowManager manager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Point point = new Point();
        manager.getDefaultDisplay().getSize(point);
        return point;
    }

    /**
     * 获得设备的屏幕高度
     *
     * @param context
     * @return
     */
    public static int getDeviceHeight(Context context) {
        return getDeviceSize(context).y;
    }

    /**
     * 获取状态栏高度＋标题栏高度
     *
     * @param activity
     * @return
     */
    public static int getTopBarHeight(Activity activity) {
        return activity.getWindow().findViewById(Window.ID_ANDROID_CONTENT).getTop();
    }
}
