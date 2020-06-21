package com.kickstart.woc.wocdriverapp.utils.map;

import android.location.Address;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.libraries.places.api.model.Place;
import com.kickstart.woc.wocdriverapp.model.ClusterMarker;
import com.kickstart.woc.wocdriverapp.model.User;
import com.kickstart.woc.wocdriverapp.R;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/*
Driver app shows current driver location, mock rider location
Rider app shows current rider location, mock driver location
*/
public class UserClient {

    private static final String TAG = UserClient.class.getSimpleName();
    private User user = null;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public User getDriverDetails() {
        return new User("driverId", true, true, "driverName", "driver@gmail.com", "1234567890", R.drawable.ic_driver_pin, 4.5, getSourceAddress(), null, null, null, getCurrentTimeStamp());
    }

    public User getRiderDetails() {
        return new User("riderId", true, true, "riderName", "rider@gmail.com", "1234567890", R.drawable.ic_rider_pin, 4.5, getSourceAddress(), getDestinationAddress(), null, null, getCurrentTimeStamp());
    }

    public void setUserVerified(boolean isVerified) {
        user.setVerified(isVerified);
    }

    public boolean getUserVerified() {
        return user.isVerified();
    }

    public Address getSourceAddress() {
        Address sourceAddress = new Address(Locale.ENGLISH);
        sourceAddress.setLatitude(12.9220);
        sourceAddress.setLongitude(77.6803);
        return sourceAddress;
    }

    public Address getDestinationAddress() {
        Address destinationAddress = new Address(Locale.ENGLISH);
        destinationAddress.setLatitude(13.1986);
        destinationAddress.setLongitude(77.7066);
        return destinationAddress;
    }

    public String getCurrentTimeStamp() {
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss");
        return sdf.format(timestamp);
    }

    public ClusterMarker getUserClusterMarker(User user) {
        Address address = user.getSourceAddress();
        ClusterMarker userClusterMarker = new ClusterMarker(
                new LatLng(address.getLatitude(), address.getLongitude()),
                user.getName(),
                user.getId(),
                user.getImage(),
                user
        );
        return userClusterMarker;
    }

    public LatLng getUpdatedDriverLatLng(int flag) {
        if (flag % 2 == 0) {
            // paypal
            Log.d(TAG, "getUpdatedDriverLatLng: PayPal");
            return new LatLng(
                    12.9220, 77.6803
            );
        } else {
            // malleshwaram
            Log.d(TAG, "getUpdatedDriverLatLng: Malleshwaram");
            return new LatLng(
                    13.0055, 77.5692
            );
        }
    }

    // Stadium
    public LatLng getUpdatedRiderLatLng(int flag) {
        if (flag % 2 == 0) {
            // grandcity
            Log.d(TAG, "getUpdatedRiderLatLng: Bellandur");
            return new LatLng(
                    12.9226, 77.6954
            );
        } else {
            // Bangalore Palace
            Log.d(TAG, "getUpdatedRiderLatLng: Bangalore Palace");
            return new LatLng(
                    12.9988, 77.5921
            );
        }
    }

    // N/W call if not servicable in places, give error
    public boolean isLocationServicable(Place place) {
        return true;
    }

    // N/W call to accept rides, notify driver when ride appears
    public User notifyRiderRequest() {
        return getRiderDetails();
    }

    // N/W call to send OTP
    public boolean sendOTPDetails(String otp1, String otp2, String otp3, String otp4) {
        return true;
    }

    // N/W call to end ride
    public void endRide() {
        // Do Nothing
    }

    // N/W call to match drivers
    public boolean sendRiderRequest() {
        return true;
    }

    // N/W call to rate trip
    public void rateTrip(String rating, String comments) {
        // Do Nothing
    }

    // N/W call to get trip summary
    public Map<String, String> getTripSummary() {
        Map<String, String> map = new HashMap<>();
        map.put("source", getSourceAddress().toString());
        map.put("destination", getDestinationAddress().toString());
        map.put("distance", "12 km");
        map.put("time", "35 min");
        map.put("amount", "140");
        return map;
    }

    // N/W call
    public boolean isRideAccepted() {
        return false;
    }

    public void cancelRideRequest() {

    }
}
