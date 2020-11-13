package com.riverside.skeleton.android.util.converter;

import android.text.TextUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * 类型转换工具类  1.0
 * b_e  2018/08/08
 */
public class TypeCastingUtils {
    /**
     * double to String
     *
     * @param number
     * @return
     */
    public static String toString(Double number) {
        if (null == number) {
            return "0.00";
        } else {
            return String.format("%.2f", number);
        }
    }

    /**
     * float to String
     *
     * @param number
     * @return
     */
    public static String toString(Float number) {
        if (null == number) {
            return "0.0";
        } else {
            String s = String.format("%.1f", number);
            if (s.indexOf(".") > 0) {
                //正则表达
                s = s.replaceAll("0+?$", "");//去掉后面无用的零
                s = s.replaceAll("[.]$", "");//如小数点后面全是零则去掉小数点
            }
            return s;
        }
    }

    /**
     * integer to String
     *
     * @param number
     * @return
     */
    public static String toString(Integer number) {
        if (null == number) {
            return "0";
        } else {
            return number.toString();
        }
    }

    /**
     * long to String
     *
     * @param number
     * @return
     */
    public static String toString(Long number) {
        if (null == number) {
            return "0";
        } else {
            return number.toString();
        }
    }

    /**
     * String to int
     *
     * @param val
     * @return
     */
    public static int toInt(String val) {
        try {
            return Integer.parseInt(val);
        } catch (Exception e) {
            return 0;
        }
    }

    /**
     * Long to int
     *
     * @param val
     * @return
     */
    public static int toInt(Long val) {
        try {
            return val.intValue();
        } catch (Exception e) {
            return 0;
        }
    }

    /**
     * String to double
     *
     * @param val
     * @return
     */
    public static double toDouble(String val) {
        try {
            return Double.parseDouble(val.replaceAll(",", ""));
        } catch (Exception e) {
            return 0.0;
        }
    }

    /**
     * String to float
     *
     * @param val
     * @return
     */
    public static float toFloat(String val) {
        try {
            return Float.parseFloat(val.replaceAll(",", ""));
        } catch (Exception e) {
            return 0.0f;
        }
    }

    /**
     * String to Long
     *
     * @param val
     * @return
     */
    public static long toLong(String val) {
        try {
            return Long.parseLong(val.replaceAll(",", ""));
        } catch (Exception e) {
            return 0L;
        }
    }

    /**
     * MapString to Map
     *
     * @param str
     * @return
     */
    public static Map<String, String> mapString2Map(String str) {
        if (TextUtils.isEmpty(str) || str.length() < 3) {
            return null;
        }
        str = str.substring(1, str.length() - 1);
        Map<String, String> map = new HashMap<String, String>();
        for (String string : str.split(",")) {
            String[] item = string.trim().split("=");
            String key = item[0];
            String value = item[1];
            map.put(key, value);
        }
        return map;
    }
}

