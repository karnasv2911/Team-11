package com.kickstart.woc.wocdriverapp.services;

import android.Manifest;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
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
import com.kickstart.woc.wocdriverapp.utils.WocConstants;
import com.kickstart.woc.wocdriverapp.utils.map.MapInputContainerEnum;
import com.kickstart.woc.wocdriverapp.utils.map.UserClient;

public class LocationService extends Service {

    private static final String TAG = "LocationService";
    private final IBinder binder = new WocLocationServiceBinder();
    private LocationCallback locationCallback;
    private FusedLocationProviderClient mFusedLocationClient;

    private UserClient userClient;
    double increment = 0;

    public class WocLocationServiceBinder extends Binder {
        public LocationService getService() {
            return LocationService.this;
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        getLocation();
        return binder;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        userClient = (UserClient) getApplicationContext();
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        if (Build.VERSION.SDK_INT >= 26) {
            String CHANNEL_ID = "my_channel_01";
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID,
                    "My Channel",
                    NotificationManager.IMPORTANCE_DEFAULT);

            ((NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE)).createNotificationChannel(channel);

            Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                    .setContentTitle("")
                    .setContentText("").build();

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
                            increment += 0.03; // used to mimic live location
                            Log.d(TAG, "onLocationResult: got location result: Lat: " + lat + ", Lng: " + lng);
                            userClient.saveUserLocation(lat, lng);
                            if (userClient.isInitialLocationBroadcast() && userClient.getMapInputContainerEnum().compareTo(MapInputContainerEnum.DriverLoaderFragment) == 0) {
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
