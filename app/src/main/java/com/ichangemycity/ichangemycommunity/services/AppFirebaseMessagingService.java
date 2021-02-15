//package com.ichangemycity.ichangemycommunity.services;
//
//import android.app.Notification;
//import android.app.NotificationChannel;
//import android.app.NotificationManager;
//import android.app.PendingIntent;
//import android.content.Context;
//import android.content.Intent;
//import android.graphics.BitmapFactory;
//import android.graphics.Color;
//import android.media.RingtoneManager;
//import android.net.Uri;
//import android.os.Bundle;
//import android.util.Log;
//
//import androidx.annotation.NonNull;
//import androidx.core.app.NotificationCompat;
//
//import com.google.firebase.messaging.FirebaseMessagingService;
//import com.google.firebase.messaging.RemoteMessage;
//import com.ichangemycity.ichangemycommunity.R;
//import com.ichangemycity.ichangemycommunity.ui.activities.SurveyHomeActivity;
//import com.ichangemycity.ichangemycommunity.utils.AppUtils;
//import com.ichangemycity.ichangemycommunity.utils.LogUtils;
//import com.ichangemycity.ichangemycommunity.utils.PushNotificationsUtil;
//
//import java.util.HashMap;
//import java.util.Map;
//
//import static androidx.core.content.ContextCompat.getSystemService;
//
//public class AppFirebaseMessagingService extends FirebaseMessagingService {
//    public static final String TAG = AppFirebaseMessagingService.class.getSimpleName();
//    private static int count = 0;
//    private PushNotificationsUtil pushNotificationsUtil;
//
//    public AppFirebaseMessagingService() {
//        super();
//    }
//
//    @Override
//    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
//        Map<String, Map<String, String>> data = new HashMap<>();
//        Intent intent = remoteMessage.toIntent();
//        Bundle bundle = intent.getExtras();
//        Log.d(TAG, "onMessageReceived: " + bundle);
//        sendNotification(bundle);
//    }
//
//    @Override
//    public void onDeletedMessages() {
//        super.onDeletedMessages();
//        LogUtils.debug(TAG, "onDeletedMessages::");
//    }
//
//    @Override
//    public void onMessageSent(@NonNull String s) {
//        super.onMessageSent(s);
//        LogUtils.debug(TAG, "onMessageSent::" + s);
//    }
//
//    @Override
//    public void onSendError(@NonNull String s, @NonNull Exception e) {
//        super.onSendError(s, e);
//        LogUtils.debug(TAG, "onSendError::" + s);
//        LogUtils.debug(TAG, "onSendError::" + e.getMessage());
//    }
//
//    @Override
//    public void onNewToken(@NonNull String s) {
//        super.onNewToken(s);
//        LogUtils.debug(TAG, "onNewToken::" + s);
//        AppUtils.saveFCMToken(s);
//        //TODO call updateProfile
//    }
//
//    private void sendNotification(Bundle bundle) {
//        Log.d(TAG, "sendNotification: " + bundle);
//        String title = String.valueOf(bundle.get("title"));
//        String body = String.valueOf(bundle.get("body"));
//        pushNotificationsUtil = new PushNotificationsUtil(this);
//        pushNotificationsUtil.configureNotificationActions(bundle);
//        Intent intent = new Intent(this, SurveyHomeActivity.class);
//        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);
//
//        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
//        NotificationManager mNotifyManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
//        //For Android Version Orio and greater than orio.
//        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
//            NotificationChannel mChannel = new NotificationChannel("ichangemycommunity", "ichangemycommunity", NotificationManager.IMPORTANCE_HIGH);
//            mChannel.setDescription(body);
//            mChannel.enableLights(true);
//            mChannel.setLightColor(Color.RED);
//            mChannel.setSound(defaultSoundUri, Notification.AUDIO_ATTRIBUTES_DEFAULT);
//            mChannel.enableVibration(true);
//            mChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
//            mNotifyManager.createNotificationChannel(mChannel);
//        }
//        //For Android Version lower than oreo.
//        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this, "ichangemycommunity");
//        mBuilder.setContentTitle(title)
//                .setContentText(body)
//                .setSmallIcon(R.drawable.app_logo)
//                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.app_logo))
//                .setAutoCancel(true)
//                .setSound(defaultSoundUri)
//                .setColor(Color.parseColor("#FFD600"))
//                .setContentIntent(pendingIntent)
//                .setChannelId("ichangemycommunity")
//                .setPriority(NotificationCompat.PRIORITY_HIGH);
//
//        mNotifyManager.notify(count, mBuilder.build());
//        count++;
//    }
//}
