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
    private String destination = "";
    private String source = "";
    private LatLng driverLatLng;
    private String distance; // = "12 km";
    private String time; // = "35 min";
    private String amount = "120";
    private boolean isDriverVerified;
    private boolean isDriverAvailable;
    private boolean isRiderNotificationReceived;
    private boolean isRideAlertAccepted;
    private boolean isRideRequestCancelledByRider;
    private boolean isTripStarted;
    private boolean isInitialBroadcast = true;
    private String[] riderPin;
    private  MapInputContainerEnum mapInputContainerEnum;
    private Set<String> wocEnabledLocations;
    private User rider;
    private User driver;
    private String contactSupport;
    private String rideId;
    private boolean isInitCallCompleted;

    /* Start Map Screen */
    // N/W calls
    public void init() {
        /*
        1. Get driver details and set in getDriverDetails
        2. Get list of WocEnabled locations and update in setInWocEnabledLocation
         */
        getInitialConfigs();
        fetchDriverDetails();
    }

    // N/W call
    public void getInitialConfigs() {
        Map<String, Object> map = new HashMap<>();
        map.put("wocEnabledLocations", new HashSet<>());
        map.put("contactSupport", "1234567890");
        wocEnabledLocations = (Set<String>) map.get("wocEnabledLocations");
        contactSupport = (String) map.get("contactSupport");
        isInitCallCompleted = true;
    }

    public void reset() {
        source = null;
        destination = null;
        isRiderNotificationReceived = false;
        isRideAlertAccepted = false;
        isRideRequestCancelledByRider = false;
        isTripStarted = false;
    }

    public boolean isInitCallCompleted() {
        return isInitCallCompleted;
    }

    public void setMapInputContainerEnum(MapInputContainerEnum mapInputContainerEnum) {
        this.mapInputContainerEnum = mapInputContainerEnum;
    }

    public MapInputContainerEnum getMapInputContainerEnum() {
        return mapInputContainerEnum;
    }

    // Retrieve user details from db, mocked driver details are fetched below
    public void fetchDriverDetails() {
        driver = new User("driverId", true, "driverName", "driver@gmail.com", "1234567890", R.drawable.ic_driver_pin, 4.5, source, null, null, getCurrentTimeStamp());
        isDriverVerified = getDriverDetails().isVerified();
        if (isDriverVerified) {
            isDriverAvailable = true;
        }
    }

    // Retrieve user details from db, mocked rider details are fetched below
    public void fetchRiderDetails(String rideId) {
        riderPin = new String[]{"1", "2", "3", "4"};
        rider = new User("riderId", true, "riderName", "rider@gmail.com", "1234567890", R.drawable.ic_rider_pin, 4.5, "RMZ Eco World, Bellandur, Bengaluru, Karnataka", "Airport, KIAL Rd, Devanahalli, Bengaluru, Karnataka", null, getCurrentTimeStamp());
        source = rider.getSource();
        destination = rider.getDestination();
    }

    public User getDriverDetails() {
        return driver;
    }

    public User getRiderDetails() {
        return rider;
    }

    public String getContactSupport() {
        return contactSupport;
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
        driverDetails.setLiveLocation(getLiveAddress(driverLatLng));
        driverDetails.setTimeStamp(getCurrentTimeStamp());
        // N/W call to update driver location;
        if (isInitialBroadcast) {
            checkIfInWocServiceableLocation(driverLatLng);
        }
    }

    private void checkIfInWocServiceableLocation(LatLng latLng) {
        String address = getLiveAddress(latLng);
        if (wocEnabledLocations.contains(address)) {
            // N/W call: driver is able to access map and is in wocEnabledLocation
        }
    }

    private String getLiveAddress(LatLng latLng) {
        String address = "";
        Geocoder geocoder = new Geocoder(getApplicationContext());
        List<Address> addresses = new ArrayList<>();
        try {
            addresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1);
        } catch (IOException e) {
            Log.d(TAG, "Exception while getting address from LatLng");
        }
        if (addresses.size() > 0) {
            address = addresses.get(0).toString();
            Log.d(TAG, "address from LatLng: " + address);
        }
        return address;
    }

    public LatLng getDriverLatLng() {
        return driverLatLng;
    }

    public boolean isInitialLocationBroadcast() {
        return isInitialBroadcast;
    }

    public void setInitialLocationBroadcast(boolean flag) {
        isInitialBroadcast = flag;
    }
    /* End Map Screen */

    /* Start Driver Verification Screen */
    public boolean isDriverVerified() {
        return isDriverVerified;
    }

    public void setDriverVerified(boolean flag) {
        isDriverVerified = flag;
    }
    /* End Driver Verification Screen */

    /* Start Driver Availability Screen*/
    // N/W call to accept rides, notify driver when ride appears
    public void setDriverAvailable(boolean flag) {
        isDriverAvailable = flag;
    }

    public boolean isDriverAvailable() {
        return isDriverAvailable;
    }

    // N/W call to add driver to pool
    public void sendRideRequest() {
        // send driver details
    }

    public boolean isRideRequestCancelledByRider() {
        return isRideRequestCancelledByRider;
    }

    public void setRideRequestCancelledByRider() {
        isRideRequestCancelledByRider = true;
        isRiderNotificationReceived = false;
        isRideAlertAccepted = false;
    }

    public void setRideAlertNotificationReceived(String id) {
        isRiderNotificationReceived = true;
        rideId = id;
    }

    // When push notification received, update rider
    public boolean getRideAlert() {
        // Get rider details from notification and set in getRiderDetails
//        isRiderNotificationReceived = true;
        return isRiderNotificationReceived;
    }

    // N/W to accept ride alert
    public void acceptRideAlert() {
        isRideAlertAccepted = true;
        isRiderNotificationReceived = false;
        fetchRiderDetails(rideId);
    }

    // N/W to cancel ride alert
    public void cancelRideAlert() {
        isRideAlertAccepted = false;
        isRiderNotificationReceived = false;
        cancelRide();
    }

    private void cancelRide() {
        // N/W to BE to cancel ride
        rideId = null;
        rider = null;
    }

    public boolean isRideAlertAccepted() {
        return isRideAlertAccepted;
    }
    /* End Driver Availability Screen*/

    /* Start Driver Found Screen*/
    // Should we log navigate to map or callRider or contactSupport
    // N/W to cancel ride request
    public void cancelRideRequest() {
        cancelRide();
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
