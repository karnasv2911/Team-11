package com.kickstart.woc.wocdriverapp.ui.activities;


import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;

import com.kickstart.woc.wocdriverapp.AppConstant;
import com.kickstart.woc.wocdriverapp.R;


public class TermsAndConditionActivity  extends BaseActivity implements View.OnClickListener{

    Button mBack;
    WebView mTermsAndConditionsWebView;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_terms_and_condition;
    }

    @Override
    protected void onCreateActivity(Bundle bundle) {
        initUI();
    }


    private void initUI() {

        mBack=findViewById(R.id.btnBack);
        mBack.setOnClickListener(this);

        mTermsAndConditionsWebView=findViewById(R.id.wvTermAndCondition);
        mTermsAndConditionsWebView.loadUrl(AppConstant.URL_TERMS_N_CONDITION_WEBVIEW);


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnBack:
                finish();
                break;
        }
    }
}
