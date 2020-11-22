package com.ichangemycity.ichangemycommunity.utils.map;

import android.app.Application;
import android.location.Address;
import android.location.Geocoder;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;
import com.ichangemycity.ichangemycommunity.R;
import com.ichangemycity.ichangemycommunity.model.User;

import java.io.IOException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserClient extends Application {

    private static final String TAG = UserClient.class.getSimpleName();
    private LatLng userLatLng;
    private boolean isInitialBroadcast = true;
    private MapInputContainerEnum mapInputContainerEnum;
    private User user;

    public MapInputContainerEnum getMapInputContainerEnum() {
        return mapInputContainerEnum;
    }

    // Retrieve user details from db, mocked driver details are fetched below
    public User fetchUser() {
        user = new User("Citizen", true, "Citizen", "citizen@gmail.com", "1234567890", R.drawable.ic_driver_pin, 4.5, null, getCurrentTimeStamp());
        return user;
    }

    public User getUserDetails() {
        return user;
    }

    public void setMapInputContainerEnum(MapInputContainerEnum mapInputContainerEnum) {
        this.mapInputContainerEnum = mapInputContainerEnum;
    }

    public String getCurrentTimeStamp() {
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss");
        return sdf.format(timestamp);
    }

    // N/W call Save user location
    public void saveUserLocation(double lat, double lng) {
        fetchUser();
        userLatLng = new LatLng(lat, lng);
        user.setLiveLocation(getLiveAddress(userLatLng));
        user.setTimeStamp(getCurrentTimeStamp());
    }

    private String getLiveAddress(LatLng latLng) {
//        String address = "";
//        Geocoder geocoder = new Geocoder(getApplicationContext());
//        List<Address> addresses = new ArrayList<>();
//        try {
//            addresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1);
//        } catch (IOException e) {
//            Log.d(TAG, "Exception while getting address from LatLng");
//        }
//        if (addresses.size() > 0) {
//            address = addresses.get(0).toString();
//            Log.d(TAG, "address from LatLng: " + address);
//        }
        return getGeoCoder(latLng);
    }

    private Map<String, LatLng> getGeoCoder2() {
        Map<String, LatLng> map = new HashMap<>();
        map.put("Kariyammana Agrahara, Bellandur", new LatLng(12.9360, 77.6808));
        map.put("RMZ Eco Space, Bellandur", new LatLng(12.9275, 77.6810));
        map.put("RGA Tech Park, Bellandur", new LatLng(12.9009, 77.7072));
        map.put("Carmelmaram Railway Station, Bellandur", new LatLng(12.9072, 77.7054));
        map.put("BTM Layout 2nd Stage", new LatLng(12.9166, 77.6101));
        map.put("Kadubeesanahalli", new LatLng(12.9394, 77.6952));
        map.put("New Horizon Gurukul, Kadubeesanahalli", new LatLng(12.9340, 77.6977));
        map.put("BTM Layout 2nd Stage", new LatLng(12.9083, 77.6051));
        map.put("Gear School, Bellandur", new LatLng(12.9183, 77.6976));
        return map;
    }

    private String getGeoCoder(LatLng latLng) {
        Map<LatLng, String> map = new HashMap<>();
        map.put(new LatLng(12.9360, 77.6808), "Kariyammana Agrahara, Bellandur");
        map.put(new LatLng(12.9275, 77.6810), "RMZ Eco Space, Bellandur");
        map.put(new LatLng(12.9009, 77.7072), "RGA Tech Park, Bellandur");
        map.put(new LatLng(12.90733, 77.7058517), "Karmelmaram Railway Station, Bellandur");
        map.put(new LatLng(12.9166, 77.6101), "BTM Layout 2nd Stage");
        map.put(new LatLng(12.9394, 77.6952), "Kadubeesanahalli");
        map.put(new LatLng(12.9340, 77.6977), "New Horizon Gurukul, Kadubeesanahalli");
        map.put(new LatLng(12.9083, 77.6051), "BTM Layout 2nd Stage");
        map.put(new LatLng(12.9183, 77.6976), "Gear School, Bellandur");
        return map.get(latLng);
    }

    public LatLng getUserLatLng() {
        return userLatLng;
    }

    public boolean isInitialLocationBroadcast() {
        return isInitialBroadcast;
    }

    /* Start Driver Trip Summary Screen */
    // N/W call to rate trip
    public void rateTrip(String rating, String comments) {
        // Do Nothing
    }

    public void setInitialLocationBroadcast(boolean flag) {
        isInitialBroadcast = flag;
    }
}
