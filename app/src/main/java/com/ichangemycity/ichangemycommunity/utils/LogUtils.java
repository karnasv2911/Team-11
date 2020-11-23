package com.ichangemycity.ichangemycommunity.utils;

import com.ichangemycity.ichangemycommunity.AppConfig;

public class LogUtils {

    public static String makeLogTag(Class cls)
    {
        return cls.getSimpleName();
    }


    public static void debug(String tag, String text) {
        if (tag == null) {
            tag = AppConfig.getDefaultLogTag();
        }
        if (AppConfig.isLogEnabled())
            android.util.Log.d(tag, text);
    }

    public static void error(String tag, String text) {
        if (tag == null) {
            tag = AppConfig.getDefaultLogTag();
        }
        if (AppConfig.isLogEnabled())
            android.util.Log.e(tag, text);
    }

    public static void info(String tag, String text) {
        if (tag == null) {
            tag = AppConfig.getDefaultLogTag();
        }
        if (AppConfig.isLogEnabled())
            android.util.Log.i(tag, text);
    }

    public static void debug(String text) {
        if (AppConfig.isLogEnabled())
            android.util.Log.d(AppConfig.getDefaultLogTag(), text);
    }

    public static void error(String text) {
        if (AppConfig.isLogEnabled())
            android.util.Log.e(AppConfig.getDefaultLogTag(), text);
    }

    public static void info(String text) {
        if (AppConfig.isLogEnabled())
            android.util.Log.i(AppConfig.getDefaultLogTag(), text);
    }

    public static void warning(String tag, String s) {
        if (AppConfig.isLogEnabled())
            android.util.Log.w(tag, s);
    }
}

