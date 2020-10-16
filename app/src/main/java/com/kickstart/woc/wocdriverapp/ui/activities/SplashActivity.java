package com.kickstart.woc.wocdriverapp.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.kickstart.woc.wocdriverapp.AppConstant;
import com.kickstart.woc.wocdriverapp.R;
import com.kickstart.woc.wocdriverapp.model.Driver;
import com.kickstart.woc.wocdriverapp.utils.SharedPreferenceUtils;


public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        init();
    }


    private void init() {
        String driverDetails= SharedPreferenceUtils.getStringValue(AppConstant.PREF_KEY_DRIVER_DETAILS,null);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                Driver rider =new Gson().fromJson(driverDetails, Driver.class);
                if(rider==null){
                    Intent createIntent = new Intent(SplashActivity.this, OTPValidationActivity.class);
                    startActivity(createIntent);
                }else {
                    Intent navIntent = new Intent(SplashActivity.this, DriverHomeActivity.class);
                    startActivity(navIntent);
                }
                finish();
            }
        }, AppConstant.SPLASH_SCREEN_TIME_OUT);
    }
}
