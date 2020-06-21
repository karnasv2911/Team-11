package com.kickstart.woc.wocdriverapp.ui.fragments;

import android.content.Context;
import android.location.Address;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.maps.GeoApiContext;
import com.google.maps.android.clustering.ClusterManager;
import com.kickstart.woc.wocdriverapp.R;
import com.kickstart.woc.wocdriverapp.model.ClusterMarker;
import com.kickstart.woc.wocdriverapp.model.PolylineData;
import com.kickstart.woc.wocdriverapp.model.User;
import com.kickstart.woc.wocdriverapp.utils.map.UserClient;
import com.kickstart.woc.wocdriverapp.utils.map.WocClusterManagerRenderer;

import java.util.ArrayList;
import java.util.List;

import static com.kickstart.woc.wocdriverapp.utils.WocConstants.MAPVIEW_BUNDLE_KEY;

public class MapViewFragment extends Fragment implements OnMapReadyCallback,
        View.OnClickListener {
//        , GoogleMap.OnPolylineClickListener {

    private static final String TAG = MapViewFragment.class.getSimpleName();

    // Fetching location services every 3 minutes
    private static final int LOCATION_UPDATE_INTERVAL = 180000;

    private static int flag = 0;

    private MapView mMapView;

    private User rider;
    private User driver;
    private GoogleMap mGoogleMap;
    private LatLngBounds mMapBoundary;
    private ClusterManager mClusterManager;
    private MapViewSizeListener mMapViewSizeListener;
    private ArrayList<ClusterMarker> mClusterMarkers = new ArrayList<>();
    private WocClusterManagerRenderer wocClusterManagerRenderer;
    private Handler mHandler = new Handler();
    private Runnable mRunnable;
    private UserClient userClient = new UserClient();
    private GeoApiContext mGeoApiContext = null;
    private boolean isLocationServicesOn = false;
    private List<PolylineData> mPolylineData = new ArrayList<>();
    private List<Marker> mTripMarkers = new ArrayList<>();

    public interface MapViewSizeListener {
        void onMapViewSizeChange();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        driver = userClient.getDriverDetails();
        if (userClient.isRideAccepted()) {
            rider = userClient.getRiderDetails();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_map_view, null, false);
        mMapView = view.findViewById(R.id.map);
//        Log.d(TAG, "onCreateView: rider location: " + rider.toString());
//        if (isDriverMatched) {
//            Log.d(TAG, "onCreateView: driver location: " + driver.toString());
//        }

        view.findViewById(R.id.btn_full_screen_map).setOnClickListener(this);
        view.findViewById(R.id.btn_reset_map).setOnClickListener(this);

        // TODO: Used in driver app
        /*
        ((NavigationScreenActivity) getActivity()).onStartNavigation(new NavigationListener() {
            @Override
            public void onStartNavigation(boolean isRideStarted) {
                removeTripMarkers();
                calculateDirections(isRideStarted);
            }
        });
         */
        initGoogleMap(savedInstanceState);
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof MapViewSizeListener) {
            //init the listener
            mMapViewSizeListener = (MapViewSizeListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement MapViewSizeListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mMapViewSizeListener = null;
    }

    private void initGoogleMap(Bundle savedInstanceState) {
        // *** IMPORTANT ***
        // MapView requires that the Bundle you pass contain _ONLY_ MapView SDK
        // objects or sub-Bundles.
        Bundle mapViewBundle = null;
        if (savedInstanceState != null) {
            mapViewBundle = savedInstanceState.getBundle(MAPVIEW_BUNDLE_KEY);
        }
        mMapView.onCreate(mapViewBundle);

        mMapView.getMapAsync(this);

        if (mGeoApiContext == null) {
            mGeoApiContext = new GeoApiContext.Builder().apiKey(getString(R.string.google_maps_key)).build();
        }
    }

    // TODO: Used in driver app
    /*
    private void calculateDirections(boolean isRideStarted) {
        Log.d(TAG, "calculateDirections: calculating directions.");

        com.google.maps.model.LatLng destination;
        if (isRideStarted) {
            destination = new com.google.maps.model.LatLng(
                    rider.getDestinationAddress().getLatitude(),
                    rider.getDestinationAddress().getLongitude()
            );
        } else {
            destination = new com.google.maps.model.LatLng(
                    rider.getSourceAddress().getLatitude(),
                    rider.getSourceAddress().getLongitude()
            );
        }
        DirectionsApiRequest directions = new DirectionsApiRequest(mGeoApiContext);

        directions.alternatives(true);
        directions.origin(
                new com.google.maps.model.LatLng(
                        driver.getSourceAddress().getLatitude(),
                        driver.getSourceAddress().getLongitude()
                )
        );
        Log.d(TAG, "calculateDirections: destination: " + destination.toString());
        directions.destination(destination).setCallback(new PendingResult.Callback<DirectionsResult>() {
            @Override
            public void onResult(DirectionsResult result) {
                Log.d(TAG, "calculateDirections: routes: " + result.routes[0].toString());
                Log.d(TAG, "calculateDirections: duration: " + result.routes[0].legs[0].duration);
                Log.d(TAG, "calculateDirections: distance: " + result.routes[0].legs[0].distance);
                Log.d(TAG, "calculateDirections: geocodedWayPoints: " + result.geocodedWaypoints[0].toString());

                addPolylinesToMap(result);
            }

            @Override
            public void onFailure(Throwable e) {
                Log.e(TAG, "calculateDirections: Failed to get directions: " + e.getMessage());

            }
        });
    }
    */

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        Bundle mapViewBundle = outState.getBundle(MAPVIEW_BUNDLE_KEY);
        if (mapViewBundle == null) {
            mapViewBundle = new Bundle();
            outState.putBundle(MAPVIEW_BUNDLE_KEY, mapViewBundle);
        }

        mMapView.onSaveInstanceState(mapViewBundle);
    }

    @Override
    public void onResume() {
        super.onResume();
        mMapView.onResume();
        if (isLocationServicesOn) {
            startUserLocationsRunnable();
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        mMapView.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
        mMapView.onStop();
    }

    @Override
    public void onMapReady(GoogleMap map) {
        mGoogleMap = map;

        mGoogleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        mGoogleMap.getUiSettings().setZoomControlsEnabled(true);
        mGoogleMap.getUiSettings().setCompassEnabled(true);
        mGoogleMap.getUiSettings().setRotateGesturesEnabled(true);
        mGoogleMap.getUiSettings().setScrollGesturesEnabled(true);
        mGoogleMap.getUiSettings().setTiltGesturesEnabled(true);
        mGoogleMap.getUiSettings().setZoomGesturesEnabled(true);

        // TODO: Used in driver app
        /*
        mGoogleMap.setOnPolylineClickListener(this);
         */
        addMapMarkers();
    }

    private void resetMap(){
        if(mGoogleMap != null) {
            mGoogleMap.clear();

            if(mClusterManager != null){
                mClusterManager.clearItems();
            }

            if (mClusterMarkers.size() > 0) {
                mClusterMarkers.clear();
                mClusterMarkers = new ArrayList<>();
            }

            if(mPolylineData.size() > 0){
                mPolylineData.clear();
                mPolylineData = new ArrayList<>();
            }
        }
    }

    private void addMapMarkers() {
        if (mGoogleMap != null) {
            resetMap();
            if (mClusterManager == null) {
                mClusterManager = new ClusterManager<ClusterMarker>(getActivity().getApplicationContext(), mGoogleMap);
            }
            if (wocClusterManagerRenderer == null) {
                wocClusterManagerRenderer = new WocClusterManagerRenderer(
                        getActivity(),
                        mGoogleMap,
                        mClusterManager
                );
                mClusterManager.setRenderer(wocClusterManagerRenderer);
                mGoogleMap.setOnMarkerClickListener(mClusterManager);
            }
            addUsersToClusterManager();
            mClusterManager.cluster();
            setCameraView();
        }
    }

    private void addUsersToClusterManager() {
        ClusterMarker driverClusterMarker = userClient.getUserClusterMarker(driver);
        mClusterManager.addItem(driverClusterMarker);
        mClusterMarkers.add(driverClusterMarker);

        if (userClient.isRideAccepted() && rider != null) {
            ClusterMarker riderClusterMarker = userClient.getUserClusterMarker(rider);
            mClusterManager.addItem(riderClusterMarker);
            mClusterMarkers.add(riderClusterMarker);
        }
    }

    private void setCameraView() {
        Address driverAddress = driver.getSourceAddress();
        // overall map view window: 0.2 * 0.2 = 0.04
        double bottomBoundary = driverAddress.getLatitude() - 0.1;
        double leftBoundary = driverAddress.getLongitude() - 0.1;
        double topBoundary = driverAddress.getLatitude() + 0.1;
        double rightBoundary = driverAddress.getLongitude() + 0.1;

        mMapBoundary = new LatLngBounds(
                new LatLng(bottomBoundary, leftBoundary),
                new LatLng(topBoundary, rightBoundary)
        );

        int width = getResources().getDisplayMetrics().widthPixels;
        int height = getResources().getDisplayMetrics().heightPixels;
        int padding = (int) (width * 0.12); // offset from edges of the map 12% of screen

        mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngBounds(mMapBoundary, width, height, padding));
    }

    @Override
    public void onPause() {
        mMapView.onPause();
        super.onPause();
    }

    @Override
    public void onDestroy() {
        mMapView.onDestroy();
        super.onDestroy();
        stopLocationUpdates();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mMapView.onLowMemory();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_full_screen_map: {
                mMapViewSizeListener.onMapViewSizeChange();
                break;
            }
            case R.id.btn_reset_map: {
                addMapMarkers();
                break;
            }
        }
    }

    private void startUserLocationsRunnable() {
        Log.d(TAG, "startUserLocationsRunnable: starting runnable for retrieving updated locations.");
        mHandler.postDelayed(mRunnable = new Runnable() {
            @Override
            public void run() {
                flag++;
                retrieveUserLocations(flag);
                mHandler.postDelayed(mRunnable, LOCATION_UPDATE_INTERVAL);
            }
        }, LOCATION_UPDATE_INTERVAL);
    }

    private void stopLocationUpdates() {
        mHandler.removeCallbacks(mRunnable);
    }

    // Used to update markers
    private void retrieveUserLocations(int flag) {
        mClusterMarkers.get(0).setPosition(userClient.getUpdatedDriverLatLng(flag));
        wocClusterManagerRenderer.setUpdateMarker(mClusterMarkers.get(0));
        if (mClusterMarkers.size() > 1) {
            mClusterMarkers.get(1).setPosition(userClient.getUpdatedRiderLatLng(flag));
            wocClusterManagerRenderer.setUpdateMarker(mClusterMarkers.get(1));
        }
    }

     // TODO: Used in driver app
    /*
    private void addPolylinesToMap(final DirectionsResult result){
        // posting DirectionsResult to main thread
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                Log.d(TAG, "run: result routes: " + result.routes.length);

                if (mPolylineData.size() > 0) {
                    for (PolylineData polylineData : mPolylineData) {
                        polylineData.getPolyline().remove();
                    }
                    mPolylineData.clear();
                    mPolylineData = new ArrayList<>();
                }

                Double maxDuration = Double.MAX_VALUE;
                for(DirectionsRoute route: result.routes){
                    Log.d(TAG, "run: leg: " + route.legs[0].toString());
                    List<com.google.maps.model.LatLng> decodedPath = PolylineEncoding.decode(route.overviewPolyline.getEncodedPath());

                    List<LatLng> newDecodedPath = new ArrayList<>();

                    // This loops through all the LatLng coordinates of ONE polyline.
                    for(com.google.maps.model.LatLng latLng: decodedPath){

//                        Log.d(TAG, "run: latlng: " + latLng.toString());

                        newDecodedPath.add(new LatLng(
                                latLng.lat,
                                latLng.lng
                        ));
                    }
                    Polyline polyline = mGoogleMap.addPolyline(new PolylineOptions().addAll(newDecodedPath));
                    polyline.setColor(ContextCompat.getColor(getActivity(), R.color.darkGrey));
                    polyline.setClickable(true);
                    mPolylineData.add(new PolylineData(polyline, route.legs[0]));

                    double duration = route.legs[0].duration.inSeconds;
                    if (duration < maxDuration) {
                        maxDuration = duration;
                        onPolylineClick(polyline);
                        zoomRoute(polyline.getPoints());
                    }
                }
            }
        });
    }

    @Override
    public void onPolylineClick(Polyline polyline) {

        int index = 0;
        for(PolylineData polylineData: mPolylineData){
            index++;
            Log.d(TAG, "onPolylineClick: toString: " + polylineData.toString());
            if(polyline.getId().equals(polylineData.getPolyline().getId())){
                polylineData.getPolyline().setColor(ContextCompat.getColor(getActivity(), R.color.blue1));
                // ZIndex shows selected poyline map in case of overlap with unselected polylines
                polylineData.getPolyline().setZIndex(1);


                // Highlight end location
                LatLng endLocation = new LatLng(
                  polylineData.getLeg().endLocation.lat,
                  polylineData.getLeg().endLocation.lng
                );

                Marker marker = mGoogleMap.addMarker(new MarkerOptions()
                        .position(endLocation)
                        .title("Trip: #" + index)
                        .snippet("Duration: " + polylineData.getLeg().duration)

                );
                marker.showInfoWindow();
                mTripMarkers.add(marker);
            }
            else{
                polylineData.getPolyline().setColor(ContextCompat.getColor(getActivity(), R.color.darkGrey));
                polylineData.getPolyline().setZIndex(0);
            }
        }
    }

    private void removeTripMarkers() {
        for (Marker marker : mTripMarkers) {
            marker.remove();
        }
    }

    public void zoomRoute(List<LatLng> lstLatLngRoute) {

        if (mGoogleMap == null || lstLatLngRoute == null || lstLatLngRoute.isEmpty()) return;

        LatLngBounds.Builder boundsBuilder = new LatLngBounds.Builder();
        for (LatLng latLngPoint : lstLatLngRoute)
            boundsBuilder.include(latLngPoint);

        int routePadding = 120;
        LatLngBounds latLngBounds = boundsBuilder.build();

        mGoogleMap.animateCamera(
                CameraUpdateFactory.newLatLngBounds(latLngBounds, routePadding),
                600,
                null
        );
    }
     */
}
