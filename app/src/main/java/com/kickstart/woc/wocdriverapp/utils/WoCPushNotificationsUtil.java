package com.kickstart.woc.wocdriverapp.utils;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import com.kickstart.woc.wocdriverapp.utils.map.MapInputContainerEnum;
import com.kickstart.woc.wocdriverapp.utils.map.UserClient;

public class WoCPushNotificationsUtil {

    public static final String TAG = WoCPushNotificationsUtil.class.getSimpleName();
    private UserClient userClient;

    public WoCPushNotificationsUtil(Context context) {
        userClient = (UserClient) context.getApplicationContext();
    }

    /* Bundle[{google.delivered_priority=normal, google.sent_time=1595342442697,
    identifier=RIDE_REQUEST_FOUND, google.ttl=2419200, google.original_priority=normal,
    rideId=123, body=You have ride request. Kindly click to accept / reject ride,
    from=588674654843, title=WoC Ride Request,
    google.message_id=0:1595342442715858%d41ff3b0f9fd7ecd, google.c.sender.id=588674654843,
    isVerified=true}] */
    public void configureNotificationActions(Bundle bundle) {
        Log.d(TAG, "configureNotificationActions: " + bundle);
        String identifier = String.valueOf(bundle.get("identifier"));

        switch (identifier) {
            case "DRIVER_VERIFICATION":
                userClient.setDriverVerified(true);
                userClient.setDriverAvailable(true);
                break;
            case "RIDE_REQUEST_FOUND":
                String rideId = (String) bundle.get("rideRequestId");
                userClient.setRideAlertNotificationReceived(rideId);
                break;
            case "RIDE_CANCELLED_BY_RIDER":
                userClient.setRideRequestCancelledByRider();
                break;
            default:
        }
    }
}
