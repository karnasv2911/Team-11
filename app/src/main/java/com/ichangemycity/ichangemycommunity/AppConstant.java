package com.ichangemycity.ichangemycommunity;

import android.os.Environment;

import java.io.File;

public class AppConstant {

    private static final Object MAIN_FOLDER = "IChangeMyCity" ;
    public static final Object APP_NAME = "IChangeMyCommunity" ;

    public static final String BASE_FOLDER = Environment.getExternalStorageDirectory() + File.separator + MAIN_FOLDER + File.separator;
    public static int SPLASH_SCREEN_TIME_OUT=2000;
    public static final int SMS_PERMISSION_CODE = 1002;
    public static final String ACTION_SMS_RECEIVED="android.provider.Telephony.SMS_RECEIVED";
    public static final String URL_TERMS_N_CONDITION_WEBVIEW="https://developer.android.com/guide/webapps/webview";

    public static final String DEFAULT_PIN = "0000";


    //KEY FOR SHARED PREFERENCE
    public static final String PREF_KEY_MOBILE = "UserMobile";
    public static final String PREF_KEY_USER_DETAILS = "UserDetails";
    public static final String PREF_KEY_DEVICE_ID = "UserDeviceId";

    //Key for request object
    public static final String REQUEST_KEY_MOBILE = "phoneNumber";
    public static final String REQUEST_KEY_OTP = "otp";

    //Keys for network call
    public static final int NETWORK_CALL_INITIATE_OTP = 1;
    public static final int NETWORK_CALL_VALIDATE_OTP = 2;

    //Image request code

    public static final int PICK_IMAGE =10000 ;
    public static final int PICK_LICENCE =10001 ;
    public static final int PICK_INSURANCE =10002 ;
    public static final int PICK_RC =10003 ;


    public enum ImageType{
        PROFILE_PIC("PP"),
        SURVEY_PIC("DL"),
        ;
        private final String type;

        private ImageType(final String type)
        {
            this.type=type;
        }

        @Override
        public String toString() {
            return type;
        }
    }
}
