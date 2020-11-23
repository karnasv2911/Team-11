package com.ichangemycity.ichangemycommunity.ui.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.JsonObject;
import com.ichangemycity.ichangemycommunity.R;
import com.ichangemycity.ichangemycommunity.utils.LogUtils;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    Button mGetApiClick, mPostApiClick;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mGetApiClick = findViewById(R.id.getApiCLick);
        mGetApiClick.setOnClickListener(this);

        mPostApiClick = findViewById(R.id.postApiClick);
        mPostApiClick.setOnClickListener(this);
    }

    private void dummyGetCall(String todoId) {
        LogUtils.debug("dummyGetCall", "fetchIssueDetails");
//
//        AppMain.getDefaultNetWorkClient().todos(todoId).enqueue(new Callback<JsonObject>() {
//            @Override
//            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
////                if(response.code() == HTTP_CODE_SUCCESS){
////                    setIncidentValues(response.body());
////                }
//
//                LogUtils.debug("Response");
//                LogUtils.debug(String.valueOf(response.code()));
//                LogUtils.debug(new Gson().toJson(response.body()));
//            }
//
//            @Override
//            public void onFailure(Call<JsonObject> call, Throwable t) {
//                //TODO: Show retrofit error dialog
//                LogUtils.debug("Network call onFailure get callv", new Gson().toJson(call));
//            }
//        });

    }


    private void dummyPostCall() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("title", "foo");
        jsonObject.addProperty("body", "bar");
        jsonObject.addProperty("userId", 1);

//        AppMain.getDefaultNetWorkClient().posts(jsonObject).enqueue(new Callback<JsonObject>() {
//            @Override
//            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
////                if(response.code() == HTTP_CODE_SUCCESS){
////                    setIncidentValues(response.body());
////                }
//                LogUtils.debug("Network call OnResponse post call", new Gson().toJson(response));
//            }
//
//            @Override
//            public void onFailure(Call<JsonObject> call, Throwable t) {
//                //TODO: Show retrofit error dialog
//                LogUtils.debug("Network call onFailure post call", new Gson().toJson(call));
//            }
//        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.getApiCLick:
                dummyGetCall("1");
                break;

            case R.id.postApiClick:
                dummyPostCall();
                break;


        }
    }
}

