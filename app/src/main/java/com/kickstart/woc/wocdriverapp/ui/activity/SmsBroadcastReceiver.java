package com.kickstart.woc.wocdriverapp.ui.activity;

import android.annotation.TargetApi;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;
import android.widget.Toast;

import com.kickstart.woc.wocdriverapp.AppConstant;

import java.util.Objects;




public class SmsBroadcastReceiver extends BroadcastReceiver {
    private static final String TAG = SmsBroadcastReceiver.class.getSimpleName();
    public static final String pdu_type = "pdus";
    SmsBroadcastReceiverListener smsBroadcastReceiverListener;
    public void setSmsBroadcastReceiverListener(SmsBroadcastReceiverListener smsBroadcastReceiverListener) {
        this.smsBroadcastReceiverListener = smsBroadcastReceiverListener;
    }

    @TargetApi(Build.VERSION_CODES.M)
    @Override
    public void onReceive(Context context, Intent intent) {
        if (Objects.equals(intent.getAction(), AppConstant.ACTION_SMS_RECEIVED)) {
            Bundle bundle = intent.getExtras();
            SmsMessage[] msgs;
            String strMessage = "";
            String format = bundle.getString("format");
            Object[] pdus = (Object[]) bundle.get(pdu_type);
            if (pdus != null) {
                boolean isVersionM = (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M);
                msgs = new SmsMessage[pdus.length];
                for (int i = 0; i < msgs.length; i++) {
                    if (isVersionM) {
                        msgs[i] = SmsMessage.createFromPdu((byte[]) pdus[i], format);
                    } else {
                        msgs[i] = SmsMessage.createFromPdu((byte[]) pdus[i]);
                    }
                    strMessage += " :" + msgs[i].getMessageBody() + "\n";
                    Log.d(TAG, "onReceive: " + strMessage);
                    Toast.makeText(context, strMessage, Toast.LENGTH_LONG).show();
                    smsBroadcastReceiverListener.handleMsg(strMessage);
                }
            }
        }
    }


    public interface SmsBroadcastReceiverListener {
        void handleMsg(String message);
    }
}
