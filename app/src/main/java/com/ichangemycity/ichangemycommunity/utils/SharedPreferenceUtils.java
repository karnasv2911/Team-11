package com.ichangemycity.ichangemycommunity.utils;

import android.content.SharedPreferences;

import com.ichangemycity.ichangemycommunity.AppMain;

import static android.os.Build.VERSION.SDK_INT;
import static android.os.Build.VERSION_CODES.GINGERBREAD;

public class SharedPreferenceUtils {
    private static SharedPreferenceUtils mInstance;
    static SharedPreferences mSharedPreferences;
    private static String defaultValue = "";


    private static SharedPreferences getSharedPref() {
        mSharedPreferences = AppMain.getSharedPreferences();
        return mSharedPreferences;

    }

    public static String getStringValue(String key) {
        mSharedPreferences = getSharedPref();
        String value = mSharedPreferences.getString(key, defaultValue);
        return value;
    }

    public static String getStringValue(String key, String defValue) {
        mSharedPreferences = getSharedPref();
        String value = mSharedPreferences.getString(key, defValue);
        return value;
    }

    public static void putStringValue(String key, String value) {
        mSharedPreferences = getSharedPref();
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putString(key, value);
        save(editor);
    }

    private static long getLongValue(String key, long defValue) {

        mSharedPreferences = getSharedPref();
        long value = mSharedPreferences.getLong(key, defValue);
        return value;
    }

    private static void putLongValue(String key, long value) {
        mSharedPreferences = getSharedPref();
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putLong(key, value);
        save(editor);
    }

    private static void putBooleanValue(String key, boolean value) {
        mSharedPreferences = getSharedPref();
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putBoolean(key, value);
        save(editor);
    }

    private static boolean getBooleanValue(String key) {

        mSharedPreferences = getSharedPref();
        boolean result = mSharedPreferences.getBoolean(key, false);
        return result;
    }

    private static boolean getBooleanValue(String key, boolean defValue) {

        mSharedPreferences = getSharedPref();
        boolean result = mSharedPreferences.getBoolean(key, defValue);
        return result;
    }

    private static int getIntValue(String key) {

        mSharedPreferences = getSharedPref();
        int result = mSharedPreferences.getInt(key, 0);
        return result;
    }

    private static void putIntValue(String key, int value) {
        mSharedPreferences = getSharedPref();
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putInt(key, value);
        save(editor);
    }

    private static void removeKey(String key) {
        mSharedPreferences = getSharedPref();
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.remove(key);
        save(editor);
    }

    private static boolean isEditorApplyAvailable() {
        return SDK_INT >= GINGERBREAD;
    }

    /**
     * Save preferences in given editor
     *
     * @param editor
     */
    private static void save(final SharedPreferences.Editor editor) {
        /*commit() writes the data synchronously (blocking the thread its called from).
                It then informs you about the success of the operation.

          apply() schedules the data to be written asynchronously.
           It does not inform you about the success of the operation.*/

        if (isEditorApplyAvailable())
            editor.apply();
        else
            editor.commit();
    }

}
