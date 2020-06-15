package com.kickstart.woc.wocdriverapp;

import android.os.Environment;

import java.io.File;

public class AppConstant {
    public static final int CAMERA_REQUEST_CODE =1000 ;
    public static final int REQ_USER_CONSENT = 1001;
    private static final Object MAIN_FOLDER = "WOCDriver" ;
    public static final Object APP_NAME = "WOCDriver" ;

    public static final String BASE_FOLDER = Environment.getExternalStorageDirectory() + File.separator + MAIN_FOLDER + File.separator;
    public static int SPLASH_SCREEN_TIME_OUT=2000;
    public static final int SMS_PERMISSION_CODE = 1002;
    public static final String ACTION_SMS_RECEIVED="android.provider.Telephony.SMS_RECEIVED";
    public static final String URL_TERMS_N_CONDITION_WEBVIEW="https://developer.android.com/guide/webapps/webview";






}
