package com.kickstart.woc.wocdriverapp.ui.services;

import androidx.annotation.NonNull;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.kickstart.woc.wocdriverapp.utils.LogUtils;

public class AppFirebaseMessagingService extends FirebaseMessagingService {
    public static final String TAG=AppFirebaseMessagingService.class.getSimpleName();

    public AppFirebaseMessagingService() {
        super();
    }

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        LogUtils.debug(TAG,"onMessageReceived::"+remoteMessage.toString());
    }

    @Override
    public void onDeletedMessages() {
        super.onDeletedMessages();
        LogUtils.debug(TAG,"onDeletedMessages::");
    }

    @Override
    public void onMessageSent(@NonNull String s) {
        super.onMessageSent(s);
        LogUtils.debug(TAG,"onMessageSent::"+s);
    }

    @Override
    public void onSendError(@NonNull String s, @NonNull Exception e) {
        super.onSendError(s, e);
        LogUtils.debug(TAG,"onSendError::"+s);
        LogUtils.debug(TAG,"onSendError::"+e.getMessage());
    }

    @Override
    public void onNewToken(@NonNull String s) {
        super.onNewToken(s);
        LogUtils.debug(TAG,"onNewToken::"+s);
    }
}
