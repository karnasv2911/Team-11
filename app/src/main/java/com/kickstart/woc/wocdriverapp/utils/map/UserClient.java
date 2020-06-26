package com.kickstart.woc.wocdriverapp.utils.map;

import android.app.Application;

import com.google.android.gms.maps.model.LatLng;
import com.kickstart.woc.wocdriverapp.R;
import com.kickstart.woc.wocdriverapp.model.User;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

public class UserClient extends Application {

    private static final String TAG = UserClient.class.getSimpleName();
    private String source = "Airport Road, Vaikuntam Layout, Lakshminarayana Pura, BEML Layout, Marathahalli, Bengaluru, Karnataka, India";
    private String destination = "Adarsh Nagar Road No 2, West Balaji Hill Colony, Adarsh Nagar, Uppal, Hyderabad, Telangana, India";
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
    private String[] riderPin;

    /* Start Map Screen */
    // N/W calls
    public void init() {
        /*
        1. Get driver details and set in getDriverDetails
        2. Get list of WocEnabled locations and update in setInWocEnabledLocation
         */
        setInWocEnabledLocation(true);

    }

    // Retrieve user details from db, mocked driver details are fetched below
    public User getDriverDetails() {
        return new User("driverId", true, "driverName", "driver@gmail.com", "1234567890", R.drawable.ic_driver_pin, 4.5, source, null, getCurrentTimeStamp());
    }

    // Retrieve user details from db, mocked rider details are fetched below
    public User getRiderDetails() {
        riderPin = new String[] {"1", "2", "3", "4"};
        return new User("riderId", true, "riderName", "rider@gmail.com", "1234567890", R.drawable.ic_rider_pin, 4.5, source, destination, getCurrentTimeStamp());
    }

    public void setInWocEnabledLocation(boolean isInWocEnabledLocation) {
        this.isInWocEnabledLocation = isInWocEnabledLocation;
    }

    public boolean isInWocEnabledLocation() {
        return isInWocEnabledLocation;
    }

    public String getCurrentTimeStamp() {
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss");
        return sdf.format(timestamp);
    }

    // N/W call: driver is able to access map and is in wocEnabledLocation
    public void setLocationServiceable(boolean isLocationServiceable) {
        this.isLocationServiceable = isLocationServiceable;
    }

    // N/W call Save user location
    public void saveUserLocation(double lat, double lng) {
        User driverDetails = getDriverDetails();
        driverLatLng = new LatLng(lat, lng);
        driverDetails.setTimeStamp(getCurrentTimeStamp());
        // N/W call to update driver location;
    }

    public LatLng getDriverLatLng() {
        return driverLatLng;
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
