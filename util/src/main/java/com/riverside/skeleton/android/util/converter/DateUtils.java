package com.riverside.skeleton.android.util.converter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * 日期工具类    1.1
 * b_e  2017/11/30
 * <p>
 * 1.1  添加时间格式化方法   2019/4/8
 */
public class DateUtils {
    public static final String DATE_FORMAT_PATTERN1 = "yyyy年MM月dd日";
    public static final String DATE_FORMAT_PATTERN2 = "yyyy-MM-dd HH:mm:ss";
    public static final String DATE_FORMAT_PATTERN3 = "yyyy-MM-dd";
    public static final String TIME_FORMAT_PATTERN1 = "%02d:%02d:%02d";

    /**
     * Date to Calendar
     *
     * @param date
     * @return
     */
    public static Calendar getCalendar(Date date) {
        if (date == null) {
            return null;
        }

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar;
    }

    /**
     * Date to String
     *
     * @param date
     * @param pattern
     * @return
     */
    public static String toString(Date date, String pattern) {
        if (date == null) {
            return null;
        }
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern, Locale.getDefault());
        return simpleDateFormat.format(date);
    }

    /**
     * Calendar to String
     *
     * @param date
     * @param pattern
     * @return
     */
    public static String toString(Calendar date, String pattern) {
        return toString(date.getTime(), pattern);
    }

    /**
     * String to Date
     *
     * @param sDate
     * @param pattern
     * @return
     */
    public static Date toDate(String sDate, String pattern) {
        if (sDate == null) {
            return null;
        }

        SimpleDateFormat sdf = new SimpleDateFormat(pattern, Locale.getDefault());
        Date date;
        try {
            date = sdf.parse(sDate);
        } catch (ParseException e) {
            return null;
        }

        return date;
    }

    /**
     * String to Calendar
     *
     * @param sDate
     * @param pattern
     * @return
     */
    public static Calendar toCalendar(String sDate, String pattern) {
        return getCalendar(toDate(sDate, pattern));
    }


    /**
     * 取得日期0点
     *
     * @param date
     * @return
     */
    public static Calendar getTimes0am(Calendar date) {
        Calendar date0 = Calendar.getInstance();
        date0.set(date.get(Calendar.YEAR), date.get(Calendar.MONTH), date.get(Calendar.DAY_OF_MONTH), 0, 0, 0);
        return date0;
    }

    /**
     * 比较2个日期的间隔天数
     *
     * @param date1
     * @param date2
     * @return
     */
    public static int daysBetween(Calendar date1, Calendar date2) {
        long time1 = getTimes0am(date1).getTimeInMillis();
        long time2 = getTimes0am(date2).getTimeInMillis();

        long between_days = (time2 - time1) / (1000 * 3600 * 24);
        return TypeCastingUtils.toInt(between_days);
    }

    /**
     * 比较2个日期的间隔天数
     *
     * @param date1
     * @param date2
     * @return
     */
    public static int daysBetween(Date date1, Date date2) {
        return daysBetween(getCalendar(date1), getCalendar(date2));
    }

    /**
     * 比较2个日期是否为同一天
     *
     * @param date1
     * @param date2
     * @return
     */
    public static boolean isSameDay(Calendar date1, Calendar date2) {
        if (date1 == null) {
            return false;
        }

        if (date2 == null) {
            return false;
        }

        return daysBetween(date1, date2) == 0;
    }

    /**
     * 比较2个日期是否为同一天
     *
     * @param date1
     * @param date2
     * @return
     */
    public static boolean isSameDay(Date date1, Date date2) {
        return isSameDay(getCalendar(date1), getCalendar(date2));
    }

    /**
     * 改变日期的格式
     *
     * @param sDate
     * @param pattern1
     * @param pattern2
     * @return
     */
    public static String transformFormat(String sDate, String pattern1, String pattern2) {
        return toString(toDate(sDate, pattern1), pattern2);
    }

    /**
     * 将秒格式化为时间格式
     *
     * @param seconds
     * @return
     */
    public static String formatSeconds(long seconds) {
        return formatSeconds(seconds, TIME_FORMAT_PATTERN1);
    }

    /**
     * 将秒格式化为时间格式
     *
     * @param seconds
     * @param pattern
     * @return
     */
    public static String formatSeconds(long seconds, String pattern) {
        int hour = 0;
        int minute = 0;
        int second = 0;
        if (seconds > 0) {
            hour = TypeCastingUtils.toInt(seconds / 3600);
            minute = TypeCastingUtils.toInt(seconds % 3600 / 60);
            second = TypeCastingUtils.toInt(seconds % 60);
        }
        return String.format(Locale.getDefault(), pattern, hour, minute, second);
    }
}

