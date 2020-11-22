package com.ichangemycity.ichangemycommunity.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

import com.ichangemycity.ichangemycommunity.AppConstant;
import com.ichangemycity.ichangemycommunity.R;


public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        init();
    }


    private void init() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent navIntent = new Intent(SplashActivity.this, SurveyHomeActivity.class);
                startActivity(navIntent);
                finish();
            }
        }, AppConstant.SPLASH_SCREEN_TIME_OUT);
    }
}
