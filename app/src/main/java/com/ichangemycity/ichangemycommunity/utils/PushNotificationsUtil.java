package com.ichangemycity.ichangemycommunity.utils;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import com.ichangemycity.ichangemycommunity.utils.map.UserClient;

public class PushNotificationsUtil {

    public static final String TAG = PushNotificationsUtil.class.getSimpleName();
    private UserClient userClient;

    public PushNotificationsUtil(Context context) {
        userClient = (UserClient) context.getApplicationContext();
    }

    public void configureNotificationActions(Bundle bundle) {
        Log.d(TAG, "configureNotificationActions: " + bundle);
        String identifier = String.valueOf(bundle.get("identifier"));

        switch (identifier) {
            default:
        }
    }
}
