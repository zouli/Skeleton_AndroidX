package com.riverside.skeleton.android.util.converter;

import android.content.Context;

/**
 * 单位转换工具类  1.0
 * b_e  2018/01/16
 */
public class UnitConverterUtils {
    /**
     * dip to px
     *
     * @param density
     * @param dpValue
     * @return
     */
    public static int dip2px(float density, float dpValue) {
        return (int) (dpValue * density + 0.5f);
    }

    /**
     * px to dip
     *
     * @param density
     * @param pxValue
     * @return
     */
    public static int px2dip(float density, float pxValue) {
        return (int) (pxValue / density + 0.5f);
    }

    /**
     * dip to px
     *
     * @param context
     * @param dpValue
     * @return
     */
    public static int dip2px(Context context, float dpValue) {
        float density = context.getResources().getDisplayMetrics().density;
        return dip2px(density, dpValue);
    }

    /**
     * px to dip
     *
     * @param context
     * @param pxValue
     * @return
     */
    public static int px2dip(Context context, float pxValue) {
        float density = context.getResources().getDisplayMetrics().density;
        return px2dip(density, pxValue);
    }

    /**
     * px to sp
     *
     * @param context
     * @param pxValue
     * @return
     */
    public static int px2sp(Context context, float pxValue) {
        float scaledDensity = context.getResources().getDisplayMetrics().scaledDensity;
        return px2dip(scaledDensity, pxValue);
    }

    /**
     * sp to px
     *
     * @param context
     * @param spValue
     * @return
     */
    public static int sp2px(Context context, float spValue) {
        float scaledDensity = context.getResources().getDisplayMetrics().scaledDensity;
        return dip2px(scaledDensity, spValue);
    }

    /**
     * 摄氏度 to 华氏度
     *
     * @param degree
     * @return
     */
    public static float celsius2fahrenheit(float degree) {
        return degree * 1.8f + 32f;
    }

    /**
     * 华氏度 to 摄氏度
     *
     * @param degree
     * @return
     */
    public static float fahrenheit2celsius(float degree) {
        return (degree - 32f) / 1.8f;
    }
}

