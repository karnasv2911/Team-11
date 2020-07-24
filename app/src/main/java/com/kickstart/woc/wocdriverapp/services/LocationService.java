package com.kickstart.woc.wocdriverapp.services;

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
import android.os.Binder;
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
import com.kickstart.woc.wocdriverapp.R;
import com.kickstart.woc.wocdriverapp.ui.activities.DriverHomeActivity;
import com.kickstart.woc.wocdriverapp.utils.WocConstants;
import com.kickstart.woc.wocdriverapp.utils.map.MapInputContainerEnum;
import com.kickstart.woc.wocdriverapp.utils.map.UserClient;

public class LocationService extends Service {

    private static final String TAG = "LocationService";
    private LocationCallback locationCallback;
    private FusedLocationProviderClient mFusedLocationClient;
    private MapInputContainerEnum mapInputContainerEnum;
    private UserClient userClient;
    double increment = 0;

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
            String CHANNEL_ID = "wocdriverapp";
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID,
                    "wocdriverapp",
                    NotificationManager.IMPORTANCE_DEFAULT);

            ((NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE)).createNotificationChannel(channel);

            Intent intent = new Intent(this, DriverHomeActivity.class);
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
                    .setChannelId("wocdriverapp")
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
        mLocationRequestHighAccuracy.setInterval(WocConstants.UPDATE_INTERVAL);
        mLocationRequestHighAccuracy.setFastestInterval(WocConstants.FASTEST_INTERVAL);


        // new Google API SDK v11 uses getFusedLocationProviderClient(this)
        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Log.d(TAG, "getLocation: stopping the location service.");
            stopSelf();
            return;
        }
        Log.d(TAG, "getLocation: getting location information.");
        wocLocationCallback();
        mFusedLocationClient.requestLocationUpdates(mLocationRequestHighAccuracy, locationCallback,
                Looper.myLooper()); // Looper.myLooper tells this to repeat forever until thread is destroyed
    }

    private void wocLocationCallback() {
        if (userClient.isDriverAvailable()) {
            locationCallback = new LocationCallback() {
                @Override
                public void onLocationResult(LocationResult locationResult) {
                    if (userClient.isDriverAvailable()) {
                        Location location = locationResult.getLastLocation();
                        if (location != null) {
                            double lat = location.getLatitude() + increment;
                            double lng = location.getLongitude() + increment;
                            mapInputContainerEnum = userClient.getMapInputContainerEnum();
//                            increment -= 0.03; // used to mimic live location
                            Log.d(TAG, "onLocationResult: got location result: Lat: " + lat + ", Lng: " + lng);
                            userClient.saveUserLocation(lat, lng);
                            if (userClient.isInitialLocationBroadcast() && mapInputContainerEnum.compareTo(MapInputContainerEnum.DriverLoaderFragment) == 0) {
                                sendMessage();
                            }
                        }
                    } else {
                        stopSelf();
                    }
                }
            };
        }  else {
            Log.d(TAG, "stop...");
            stopSelf();
        }
    }

    private void sendMessage() {
        Log.d("sender", "Broadcasting message");
        Intent broadcastIntent = new Intent(WocConstants.INITIAL_LOCATION_BROADCAST);
        LocalBroadcastManager.getInstance(this).sendBroadcast(broadcastIntent);
    }
}
