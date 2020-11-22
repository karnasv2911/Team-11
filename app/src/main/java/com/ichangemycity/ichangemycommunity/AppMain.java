package com.ichangemycity.ichangemycommunity;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import com.ichangemycity.ichangemycommunity.network.NetworkClient;
import com.ichangemycity.ichangemycommunity.network.NetworkClientBuilder;

public class AppMain extends Application {
    private static Context mContext;
    private static NetworkClient sNetworkClient;
    private static AppMain sApplicationInstance = null;

    public static AppMain getInstance() {
        return sApplicationInstance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = getApplicationContext();
        sApplicationInstance = this;
    }



    public static SharedPreferences getSharedPreferences() {
        return mContext.getSharedPreferences(AppConfig.getSharedPrefName(), MODE_PRIVATE);
    }

    public static Context getAppMainContext() {
        return mContext;
    }

    public static NetworkClient getDefaultNetWorkClient() {
        if (sNetworkClient == null) {
            NetworkClientBuilder networkClientBuilder = new NetworkClientBuilder(mContext);
            sNetworkClient = networkClientBuilder.getDefaultNetworkClient();
        }
        return sNetworkClient;
    }
}
