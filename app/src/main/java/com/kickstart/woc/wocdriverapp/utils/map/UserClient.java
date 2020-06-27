package com.kickstart.woc.wocdriverapp.utils.map;

import android.app.Application;
import android.location.Address;
import android.location.Geocoder;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;
import com.kickstart.woc.wocdriverapp.R;
import com.kickstart.woc.wocdriverapp.model.User;

import java.io.IOException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class UserClient extends Application {

    private static final String TAG = UserClient.class.getSimpleName();
    private String destination = "KIAL Rd, Devanahalli, Bengaluru, Karnataka";
    private String source = "Adarsh Palm Retreat, Bellandur, Bengaluru";
    private LatLng driverLatLng;
    private String distance; // = "12 km";
    private String time; // = "35 min";
    private String amount = "120";
    private boolean isLocationServiceable;
    private boolean isDriverAvailable = true;
    private boolean isInWocEnabledLocation;
    private boolean isRiderNotificationReceived;
    private boolean isRideAlertAccepted;
    private boolean isTripStarted;
    private boolean isInitialBroadcast = true;
    private String[] riderPin;
    private  MapInputContainerEnum mapInputContainerEnum;
    private Set<String> wocEnabledLocations;

    /* Start Map Screen */
    // N/W calls
    public void init() {
        /*
        1. Get driver details and set in getDriverDetails
        2. Get list of WocEnabled locations and update in setInWocEnabledLocation
         */
        if (isInitialBroadcast) {
            mapInputContainerEnum = MapInputContainerEnum.DriverLoaderFragment;
        } else {
            mapInputContainerEnum = MapInputContainerEnum.Unknown;
        }
        wocEnabledLocations = new HashSet<>();
    }

    public void setMapInputContainerEnum(MapInputContainerEnum mapInputContainerEnum) {
        this.mapInputContainerEnum = mapInputContainerEnum;
    }

    public MapInputContainerEnum getMapInputContainerEnum() {
        return mapInputContainerEnum;
    }

    // Retrieve user details from db, mocked driver details are fetched below
    public User getDriverDetails() {
        return new User("driverId", true, "driverName", "driver@gmail.com", "1234567890", R.drawable.ic_driver_pin, 4.5, source, null, getCurrentTimeStamp());
    }

    // Retrieve user details from db, mocked rider details are fetched below
    public User getRiderDetails() {
        riderPin = new String[]{"1", "2", "3", "4"};
        return new User("riderId", true, "riderName", "rider@gmail.com", "1234567890", R.drawable.ic_rider_pin, 4.5, source, destination, getCurrentTimeStamp());
    }

    public String getCurrentTimeStamp() {
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss");
        return sdf.format(timestamp);
    }

    // N/W call Save user location
    public void saveUserLocation(double lat, double lng) {
        User driverDetails = getDriverDetails();
        driverLatLng = new LatLng(lat, lng);
        driverDetails.setTimeStamp(getCurrentTimeStamp());
        // N/W call to update driver location;
        if (isInitialBroadcast) {
            checkIfInWocServiceableLocation();
        }
    }

    private void checkIfInWocServiceableLocation() {
        Geocoder geocoder = new Geocoder(getApplicationContext());
        List<Address> addresses = new ArrayList<>();
        try {
            addresses = geocoder.getFromLocation(driverLatLng.latitude, driverLatLng.longitude, 1);
        } catch (IOException e) {
            Log.d(TAG, "Exception while getting address from LatLng");
        }
        Log.d(TAG, "address from LatLng: " + addresses.get(0));
        if (addresses.size() > 0 && wocEnabledLocations.contains(addresses.get(0))) {
            // N/W call: driver is able to access map and is in wocEnabledLocation
        }
    }

    public LatLng getDriverLatLng() {
        return driverLatLng;
    }

    public boolean isInitialLocationBroadcast() {
        return isInitialBroadcast;
    }

    public void setInitialLocationBroadcast(boolean isInitialBroadcast) {
        this.isInitialBroadcast = isInitialBroadcast;
    }
    /* End Map Screen */

    /* Start Driver Verification Screen */
    public boolean isDriverVerified() {
        return getDriverDetails().isVerified();
    }
    /* End Driver Verification Screen */

    /* Start Driver Availability Screen*/
    // N/W call to accept rides, notify driver when ride appears
    public void setDriverAvailable(boolean isDriverAvailable) {
        this.isDriverAvailable = isDriverAvailable;
        // default behavior: is available ?
    }

    public boolean isDriverAvailable() {
        return isDriverAvailable;
    }

    // N/W call to add driver to pool
    public void sendRideRequest() {
        // send driver details
    }

    // When push notification received, update rider
    public boolean getRideAlert() {
        // Get rider details from notification and set in getRiderDetails
        isRiderNotificationReceived = true;
        return isRiderNotificationReceived;
    }

    // N/W to accept ride alert
    public void acceptRideAlert() {
        isRideAlertAccepted = true;
        isRiderNotificationReceived = false;
    }

    // N/W to cancel ride alert
    public void cancelRideAlert() {
        isRideAlertAccepted = false;
        isRiderNotificationReceived = false;
    }

    public boolean isRideAlertAccepted() {
        return isRideAlertAccepted;
    }
    /* End Driver Availability Screen*/

    /* Start Driver Found Screen*/
    // Should we log navigate to map or callRider or contactSupport
    // N/W to cancel ride request
    public void cancelRideRequest() {

    }

    public void startTrip() {
    }
    /* End Driver Found Screen*/

    /* Start Driver Enter Rider Pin Screen */
    // Pin fetched when rider details are sent along with notification, this avoids N/W call
    public boolean isValidPin(String otp1, String otp2, String otp3, String otp4) {
        String[] arr = {otp1, otp2, otp3, otp4};
        for (int i = 0; i < 4; i++) {
            if (!riderPin[i].equalsIgnoreCase(arr[i])) {
                isTripStarted = false;
                return false;
            }
        }
        isTripStarted = true;
        return true;
    }
    /* End Driver Enter Rider Pin Screen */

    /* Start Driver On Trip Screen */
    // N/W call to end ride
    public void endRide() {
        isTripStarted = false;
        isRideAlertAccepted = false;
        isRiderNotificationReceived = false;
    }

    public boolean isTripStarted() {
        return isTripStarted;
    }
    /* End Driver On Trip Screen */

    /* Start Driver Trip Summary Screen */
    // N/W call to rate trip
    public void rateTrip(String rating, String comments) {
        // Do Nothing
    }

    // N/W call to get trip summary
    public Map<String, String> getTripSummary() {
        Map<String, String> map = new HashMap<>();
        map.put("source", getSource());
        map.put("destination", getDestination());
        map.put("distance", getDistance());
        map.put("time", getTime());
        map.put("amount", getAmount());
        return map;
    }

    public String getSource() {
        return source;
    }

    public String getDestination() {
        return destination;
    }

    private String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    private String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    private String getAmount() {
        return amount;
    }
    /* End Driver Trip Summary Screen*/
}
