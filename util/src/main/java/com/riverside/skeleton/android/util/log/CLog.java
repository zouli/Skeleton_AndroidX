package com.riverside.skeleton.android.util.log;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.util.Log;

/**
 * 通用Log输出类 1.0
 * b_e  2017/11/26
 */
public final class CLog {

    public static final int LEVEL_VERBOSE = 0;
    public static final int LEVEL_DEBUG = 1;
    public static final int LEVEL_INFO = 2;
    public static final int LEVEL_WARNING = 3;
    public static final int LEVEL_ERROR = 4;
    public static final int LEVEL_FATAL = 5;

    private static int sLevel = LEVEL_VERBOSE;
    private static String TAG = "CLog";

    private static Boolean isDebug = null;

    private CLog() {
    }

    /**
     * 初始化CLog
     *
     * @param context
     */
    public static void init(Context context) {
        if (isDebug == null) {
            isDebug = context.getApplicationInfo() != null &&
                    (context.getApplicationInfo().flags & ApplicationInfo.FLAG_DEBUGGABLE) != 0;
        }
    }

    /**
     * 返回当前是否为Debug
     *
     * @return
     */
    public static boolean isDebug() {
        return isDebug == null ? false : isDebug;
    }

    /**
     * 设置Log级别，比设置的Log级别低的不会被打印
     *
     * @param level
     */
    public static void setLogLevel(int level) {
        sLevel = level;
    }

    /**
     * 当前级别是否可以打印
     *
     * @param printLevel
     * @return
     */
    private static boolean isPrint(int printLevel) {
        return sLevel <= printLevel;
    }

    //===========================================================
    public static void v(String msg) {
        if (isPrint(LEVEL_VERBOSE)) {
            v(getTag(), msg);
        }
    }

    public static void v(String tag, String msg) {
        if (isPrint(LEVEL_VERBOSE)) {
            Log.v(tag, msg);
        }
    }

    public static void v(String msg, Throwable throwable) {
        if (isPrint(LEVEL_VERBOSE)) {
            v(getTag(), msg, throwable);
        }
    }

    public static void v(String tag, String msg, Throwable throwable) {
        if (isPrint(LEVEL_VERBOSE)) {
            Log.v(tag, msg, throwable);
        }
    }

    public static void v(String msg, Object... args) {
        if (isPrint(LEVEL_VERBOSE)) {
            v(getTag(), msg, args);
        }
    }

    public static void v(String tag, String msg, Object... args) {
        if (isPrint(LEVEL_VERBOSE)) {
            if (args.length > 0) {
                msg = String.format(msg, args);
            }
            Log.v(tag, msg);
        }
    }

    //===========================================================
    public static void d(String msg) {
        if (isPrint(LEVEL_DEBUG)) {
            d(getTag(), msg);
        }
    }

    public static void d(String tag, String msg) {
        if (isPrint(LEVEL_DEBUG)) {
            Log.d(tag, msg);
        }
    }

    public static void d(String msg, Object... args) {
        if (isPrint(LEVEL_DEBUG)) {
            d(getTag(), msg, args);
        }
    }

    public static void d(String tag, String msg, Object... args) {
        if (isPrint(LEVEL_DEBUG)) {
            if (args.length > 0) {
                msg = String.format(msg, args);
            }
            Log.d(tag, msg);
        }
    }

    public static void d(String msg, Throwable throwable) {
        if (isPrint(LEVEL_DEBUG)) {
            d(getTag(), msg, throwable);
        }
    }

    public static void d(String tag, String msg, Throwable throwable) {
        if (isPrint(LEVEL_DEBUG)) {
            Log.d(tag, msg, throwable);
        }
    }

    //===========================================================
    public static void i(String msg) {
        if (isPrint(LEVEL_INFO)) {
            i(getTag(), msg);
        }
    }

    public static void i(String tag, String msg) {
        if (isPrint(LEVEL_INFO)) {
            Log.i(tag, msg);
        }
    }

    public static void i(String msg, Object... args) {
        if (isPrint(LEVEL_INFO)) {
            i(getTag(), msg, args);
        }
    }

    public static void i(String tag, String msg, Object... args) {
        if (isPrint(LEVEL_INFO)) {
            if (args.length > 0) {
                msg = String.format(msg, args);
            }
            Log.i(tag, msg);
        }
    }

    public static void i(String msg, Throwable throwable) {
        if (isPrint(LEVEL_INFO)) {
            i(getTag(), msg, throwable);
        }
    }

    public static void i(String tag, String msg, Throwable throwable) {
        if (isPrint(LEVEL_INFO)) {
            Log.i(tag, msg, throwable);
        }
    }

    //===========================================================
    public static void w(String msg) {
        if (isPrint(LEVEL_WARNING)) {
            w(getTag(), msg);
        }
    }

    public static void w(String tag, String msg) {
        if (isPrint(LEVEL_WARNING)) {
            Log.w(tag, msg);
        }
    }

    public static void w(String msg, Object... args) {
        if (isPrint(LEVEL_WARNING)) {
            w(getTag(), msg, args);
        }
    }

    public static void w(String tag, String msg, Object... args) {
        if (isPrint(LEVEL_WARNING)) {
            if (args.length > 0) {
                msg = String.format(msg, args);
            }
            Log.w(tag, msg);
        }
    }

    public static void w(String msg, Throwable throwable) {
        if (isPrint(LEVEL_WARNING)) {
            w(getTag(), msg, throwable);
        }
    }

    public static void w(String tag, String msg, Throwable throwable) {
        if (isPrint(LEVEL_WARNING)) {
            Log.w(tag, msg, throwable);
        }
    }

    //===========================================================
    public static void e(String msg) {
        if (isPrint(LEVEL_ERROR)) {
            e(getTag(), msg);
        }
    }

    public static void e(String tag, String msg) {
        if (isPrint(LEVEL_ERROR)) {
            Log.e(tag, msg);
        }
    }

    public static void e(String msg, Object... args) {
        if (isPrint(LEVEL_ERROR)) {
            e(getTag(), msg, args);
        }
    }

    public static void e(String tag, String msg, Object... args) {
        if (isPrint(LEVEL_ERROR)) {
            if (args.length > 0) {
                msg = String.format(msg, args);
            }
            Log.e(tag, msg);
        }
    }

    public static void e(String msg, Throwable throwable) {
        if (isPrint(LEVEL_ERROR)) {
            e(getTag(), msg, throwable);
        }
    }

    public static void e(String tag, String msg, Throwable throwable) {
        if (isPrint(LEVEL_ERROR)) {
            Log.e(tag, msg, throwable);
        }
    }

    //===========================================================
    public static void f(String msg) {
        if (isPrint(LEVEL_FATAL)) {
            f(getTag(), msg);
        }
    }

    public static void f(String tag, String msg) {
        if (isPrint(LEVEL_FATAL)) {
            Log.wtf(tag, msg);
        }
    }

    public static void f(String msg, Object... args) {
        if (isPrint(LEVEL_FATAL)) {
            f(getTag(), msg, args);
        }
    }

    public static void f(String tag, String msg, Object... args) {
        if (isPrint(LEVEL_FATAL)) {
            if (args.length > 0) {
                msg = String.format(msg, args);
            }
            Log.wtf(tag, msg);
        }
    }

    public static void f(String msg, Throwable throwable) {
        if (isPrint(LEVEL_FATAL)) {
            f(getTag(), msg, throwable);
        }
    }

    public static void f(String tag, String msg, Throwable throwable) {
        if (isPrint(LEVEL_FATAL)) {
            Log.wtf(tag, msg, throwable);
        }
    }

    /**
     * 自动生成TAG
     *
     * @return
     */
    private static String getTag() {
        StackTraceElement[] sts = Thread.currentThread().getStackTrace();
        if (sts == null) {
            return TAG;
        }

        for (StackTraceElement st : sts) {
            if (st.isNativeMethod()) {
                continue;
            }

            if (st.getClassName().equals(Thread.class.getName())) {
                continue;
            }

            if (st.getClassName().equals(CLog.class.getName())) {
                continue;
            }

            return "[" + Thread.currentThread().getName()
                    + "(" + Thread.currentThread().getId() + "): "
                    + st.getFileName()
                    + "(" + st.getLineNumber() + "):"
                    + st.getMethodName() + "]";
        }

        return TAG;
    }
}
