package cn.dajiahui.kid.util;

import android.util.Log;

/**
 * Created by mj .
 */

public class Logger {

    //设为false关闭日志
    private static final boolean LOG_ENABLE = true;
    private static String tag = "majin";


    public static void i(String msg) {
        if (LOG_ENABLE) {
            Log.i(tag, msg);
        }
    }

    public static void v(String msg) {
        if (LOG_ENABLE) {
            Log.v(tag, msg);
        }
    }

    public static void d(String msg) {
        if (LOG_ENABLE) {
            Log.d(tag, msg);
        }
    }

    public static void w(String msg) {
        if (LOG_ENABLE) {
            Log.w(tag, msg);
        }
    }

    public static void e(String msg) {
        if (LOG_ENABLE) {
            Log.e(tag, msg);
        }
    }

}
