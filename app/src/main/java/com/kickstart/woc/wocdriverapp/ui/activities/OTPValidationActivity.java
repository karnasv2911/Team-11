package com.kickstart.woc.wocdriverapp.ui.activities;


import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.kickstart.woc.wocdriverapp.R;
import com.kickstart.woc.wocdriverapp.utils.LogUtils;

import static com.kickstart.woc.wocdriverapp.AppConstant.SMS_PERMISSION_CODE;


public class OTPValidationActivity extends BaseActivity implements View.OnClickListener, SmsBroadcastReceiver.SmsBroadcastReceiverListener {

    LinearLayout mMobileScreenContainer,mOTPScreenContainer;
    Button mContinue,mConfirm;
    SmsBroadcastReceiver smsBroadcastReceiver;

    private EditText editText1, editText2, editText3, editText4;
    private EditText[] editTexts;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_otpvalidation;
    }


    @Override
    protected void onCreateActivity(Bundle bundle) {
        initUI();
        fcm();
        registerBroadcastReceiver();
        checkForSmsPermission();
    }

    private void fcm() {
        FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(OTPValidationActivity.this, new OnSuccessListener<InstanceIdResult>() {
            @Override
            public void onSuccess(InstanceIdResult instanceIdResult) {
                String token = instanceIdResult.getToken();
                LogUtils.error("Shikha::FCM Token", token);
                //TODO send this to server
                //saveToken(token);
            }
        });
    }


    private void checkForSmsPermission() {
        if (!hasReadSmsPermission()) {
            requestReadAndSendSmsPermission();
        }
    }

    private void initUI() {
        setStatusBar();
        mMobileScreenContainer= findViewById(R.id.mobileScreenContainer);
        mOTPScreenContainer= findViewById(R.id.otpScreenContainer);

        mContinue=findViewById(R.id.btnContinue);
        mContinue.setOnClickListener(this);

        mConfirm=findViewById(R.id.btnConfirm);
        mConfirm.setOnClickListener(this);


        editText1 = (EditText) findViewById(R.id.etOPT1);
        editText2 = (EditText) findViewById(R.id.etOPT2);
        editText3 = (EditText) findViewById(R.id.etOPT3);
        editText4 = (EditText) findViewById(R.id.etOPT4);
        editTexts = new EditText[]{editText1, editText2, editText3, editText4};

        editText1.addTextChangedListener(new PinTextWatcher(0));
        editText2.addTextChangedListener(new PinTextWatcher(1));
        editText3.addTextChangedListener(new PinTextWatcher(2));
        editText4.addTextChangedListener(new PinTextWatcher(3));

        editText1.setOnKeyListener(new PinOnKeyListener(0));
        editText2.setOnKeyListener(new PinOnKeyListener(1));
        editText3.setOnKeyListener(new PinOnKeyListener(2));
        editText4.setOnKeyListener(new PinOnKeyListener(3));

    }


    private void setStatusBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            Drawable background = getResources().getDrawable(R.drawable.bg_blue_gradient);
            window.setStatusBarColor(getResources().getColor(android.R.color.transparent));
            window.setNavigationBarColor(getResources().getColor(android.R.color.transparent));
            window.setBackgroundDrawable(background);
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnConfirm:
                performConfirm();
                break;


            case R.id.btnContinue:
                performContinue();
                break;
        }
    }

    private void performContinue() {
        toogleView(mOTPScreenContainer,mMobileScreenContainer);
    }

    private void performConfirm() {
        Intent navIntent = new Intent(OTPValidationActivity.this, DriverHomeActivity.class);
        startActivity(navIntent);
        finish();
    }




    public class PinTextWatcher implements TextWatcher {

        private int currentIndex;
        private boolean isFirst = false, isLast = false;
        private String newTypedString = "";

        PinTextWatcher(int currentIndex) {
            this.currentIndex = currentIndex;

            if (currentIndex == 0)
                this.isFirst = true;
            else if (currentIndex == editTexts.length - 1)
                this.isLast = true;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            newTypedString = s.subSequence(start, start + count).toString().trim();
        }

        @Override
        public void afterTextChanged(Editable s) {

            String text = newTypedString;

            /* Detect paste event and set first char */
            if (text.length() > 1)
                text = String.valueOf(text.charAt(0)); // TODO: We can fill out other EditTexts

            editTexts[currentIndex].removeTextChangedListener(this);
            editTexts[currentIndex].setText(text);
            editTexts[currentIndex].setSelection(text.length());
            editTexts[currentIndex].addTextChangedListener(this);

            if (text.length() == 1)
                moveToNext();
            else if (text.length() == 0)
                moveToPrevious();
        }

        private void moveToNext() {
            if (!isLast)
                editTexts[currentIndex + 1].requestFocus();

            if (isAllEditTextsFilled() && isLast) { // isLast is optional
                editTexts[currentIndex].clearFocus();
                hideKeyboard();
            }
        }

        private void moveToPrevious() {
            if (!isFirst)
                editTexts[currentIndex - 1].requestFocus();
        }

        private boolean isAllEditTextsFilled() {
            for (EditText editText : editTexts)
                if (editText.getText().toString().trim().length() == 0)
                    return false;
            return true;
        }

        private void hideKeyboard() {
            if (getCurrentFocus() != null) {
                InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
            }
        }

    }

    public class PinOnKeyListener implements View.OnKeyListener {

        private int currentIndex;

        PinOnKeyListener(int currentIndex) {
            this.currentIndex = currentIndex;
        }

        @Override
        public boolean onKey(View v, int keyCode, KeyEvent event) {
            if (keyCode == KeyEvent.KEYCODE_DEL && event.getAction() == KeyEvent.ACTION_DOWN) {
                if (editTexts[currentIndex].getText().toString().isEmpty() && currentIndex != 0)
                    editTexts[currentIndex - 1].requestFocus();
            }
            return false;
        }

    }

    private boolean hasReadSmsPermission() {
        return ContextCompat.checkSelfPermission(OTPValidationActivity.this,
                Manifest.permission.READ_SMS) == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(OTPValidationActivity.this,
                        Manifest.permission.RECEIVE_SMS) == PackageManager.PERMISSION_GRANTED;
    }

    private void requestReadAndSendSmsPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(OTPValidationActivity.this, Manifest.permission.READ_SMS)) {
            Log.d(TAG, "shouldShowRequestPermissionRationale(), no permission requested");
            return;
        }
        ActivityCompat.requestPermissions(OTPValidationActivity.this, new String[]{Manifest.permission.READ_SMS, Manifest.permission.RECEIVE_SMS},
                SMS_PERMISSION_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        // For the requestCode, check if permission was granted or not.
        switch (requestCode) {
            case SMS_PERMISSION_CODE: {
                for (int i = 0; i < permissions.length; i++) {
                        String permission = permissions[i];
                        int grantResult = grantResults[i];

                        if (permission.equals(Manifest.permission.READ_SMS)) {
                            if (grantResult == PackageManager.PERMISSION_GRANTED) {
                                LogUtils.debug("SMS_PERMISSION_GRANTED");
                            } else {
                                LogUtils.debug("SMS_PERMISSION_NOT_GRANTED");
                            }
                        }
                    }
            }
        }
    }


    private void registerBroadcastReceiver() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.provider.Telephony.SMS_RECEIVED");
        smsBroadcastReceiver = new SmsBroadcastReceiver();
        smsBroadcastReceiver.setSmsBroadcastReceiverListener(this);
        registerReceiver(smsBroadcastReceiver, intentFilter);
    }

    @Override
    protected void onStop() {
        super.onStop();
        unregisterReceiver(smsBroadcastReceiver);
    }

    @Override
    public void handleMsg(String message) {
        //TODO populate in UI
        LogUtils.debug("handleMsg:"+message);
    }
}



