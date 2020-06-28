package com.kickstart.woc.wocdriverapp.utils;

import com.kickstart.woc.wocdriverapp.utils.map.MapInputContainerEnum;

public class WocConstants {

    public static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 9001;
    public static final int PERMISSIONS_REQUEST_ENABLE_GPS = 9002;
    public static final int ERROR_DIALOG_REQUEST = 9003;
    public static final String MAPVIEW_BUNDLE_KEY = "MapViewBundleKey";
    public static final int MAP_LAYOUT_STATE_CONTRACTED = 0;
    public static final int MAP_LAYOUT_STATE_EXPANDED = 1;
    public static final int REQUEST_CALL = 1;
    public static final String CONTACT_SUPPORT = "1234567890";
    // Fetching location services every 1 minute
    public static final int LOCATION_UPDATE_INTERVAL = 30 * 1000;
    public static final int DEFAULT_ZOOM = 15;
    public static final String ERROR_MESSAGE = "Something went wrong. Please retry after some time!";
    public final static String INITIAL_LOCATION_BROADCAST = "initial_location_broadcast";
}
