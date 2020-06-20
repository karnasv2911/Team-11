package com.kickstart.woc.wocdriverapp.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

import com.kickstart.woc.wocdriverapp.AppConstant;
import com.kickstart.woc.wocdriverapp.R;


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
                Intent i=new Intent(SplashActivity.this,
                     OTPValidationActivity.class);
                startActivity(i);
                finish();
            }
        }, AppConstant.SPLASH_SCREEN_TIME_OUT);
    }
}
