package com.riverside.skeleton.android.util.converter;

public class StringUtils {
    /**
     * 替换指定位置的字符串
     *
     * @param str
     * @param start
     * @param end
     * @param str1
     * @return
     */
    public static String replace(String str, int start, int end, String str1) {
        if (str.length() < start) {
            return str;
        }
        StringBuilder sb = new StringBuilder(str);
        sb.replace(start, end, str1);
        return sb.toString();
    }
}
