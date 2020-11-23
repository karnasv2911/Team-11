package com.ichangemycity.ichangemycommunity.services;

import android.Manifest;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.location.Location;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.IBinder;
import android.os.Looper;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.ichangemycity.ichangemycommunity.R;
import com.ichangemycity.ichangemycommunity.ui.activities.SurveyHomeActivity;
import com.ichangemycity.ichangemycommunity.utils.IChangeMyCommunityConstants;
import com.ichangemycity.ichangemycommunity.utils.map.MapInputContainerEnum;
import com.ichangemycity.ichangemycommunity.utils.map.UserClient;

public class LocationService extends Service {

    private static final String TAG = "LocationService";
    private LocationCallback locationCallback;
    private FusedLocationProviderClient mFusedLocationClient;
    private MapInputContainerEnum mapInputContainerEnum;
    private UserClient userClient;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        userClient = (UserClient) getApplicationContext();
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(getApplicationContext());

        if (Build.VERSION.SDK_INT >= 26) {
            String CHANNEL_ID = "ichangemycommunity";
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID,
                    "ichangemycommunity",
                    NotificationManager.IMPORTANCE_DEFAULT);

            ((NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE)).createNotificationChannel(channel);

            Intent intent = new Intent(this, SurveyHomeActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 1234, intent, PendingIntent.FLAG_ONE_SHOT);

            Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            Notification notification = new NotificationCompat.Builder(getApplicationContext(), CHANNEL_ID)
                    .setContentTitle(getResources().getString(R.string.location))
                    .setContentText(getResources().getString(R.string.locationDetail))
                    .setSmallIcon(R.drawable.app_logo)
                    .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.app_logo))
                    .setAutoCancel(true)
                    .setSound(defaultSoundUri)
                    .setColor(Color.parseColor("#FFD600"))
                    .setContentIntent(pendingIntent)
                    .setChannelId("ichangemycommunity")
                    .setPriority(NotificationCompat.PRIORITY_LOW)
                    .build();

            startForeground(1, notification);
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "onStartCommand: called.");
        getLocation();
        return Service.START_NOT_STICKY;
    }

    private void getLocation() {
        // ---------------------------------- LocationRequest ------------------------------------
        // Create the location request to start receiving updates
        LocationRequest mLocationRequestHighAccuracy = new LocationRequest();
        mLocationRequestHighAccuracy.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequestHighAccuracy.setInterval(IChangeMyCommunityConstants.UPDATE_INTERVAL);
        mLocationRequestHighAccuracy.setFastestInterval(IChangeMyCommunityConstants.FASTEST_INTERVAL);


        // new Google API SDK v11 uses getFusedLocationProviderClient(this)
        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Log.d(TAG, "getLocation: stopping the location service.");
            stopSelf();
            return;
        }
        Log.d(TAG, "getLocation: getting location information.");
        locationCallback();
        mFusedLocationClient.requestLocationUpdates(mLocationRequestHighAccuracy, locationCallback,
                Looper.myLooper()); // Looper.myLooper tells this to repeat forever until thread is destroyed
    }

    private void locationCallback() {
        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                Location location = locationResult.getLastLocation();
                double lat = location.getLatitude();
                double lng = location.getLongitude();
                mapInputContainerEnum = userClient.getMapInputContainerEnum();
                userClient.saveUserLocation(lat, lng);
                if (userClient.isInitialLocationBroadcast() && mapInputContainerEnum.compareTo(MapInputContainerEnum.LoaderFragment) == 0) {
                    Log.d(TAG, "send onLocationResult: got location result: Lat: " + lat + ", Lng: " + lng);
                    sendMessage();
                } else {
                    Log.d(TAG, "stop onLocationResult: got location result: Lat: " + lat + ", Lng: " + lng);
                    stopSelf();
                }
            }
        };
    }

    private void sendMessage() {
        Log.d("sender", "Broadcasting message");
        Intent broadcastIntent = new Intent(IChangeMyCommunityConstants.INITIAL_LOCATION_BROADCAST);
        LocalBroadcastManager.getInstance(this).sendBroadcast(broadcastIntent);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        stopSelf();
    }
}
