package com.kickstart.woc.wocdriverapp.ui.fragments;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.maps.DirectionsApiRequest;
import com.google.maps.GeoApiContext;
import com.google.maps.PendingResult;
import com.google.maps.internal.PolylineEncoding;
import com.google.maps.model.DirectionsResult;
import com.google.maps.model.DirectionsRoute;
import com.kickstart.woc.wocdriverapp.R;
import com.kickstart.woc.wocdriverapp.model.PolylineData;
import com.kickstart.woc.wocdriverapp.utils.WocConstants;
import com.kickstart.woc.wocdriverapp.utils.map.MapInputContainerEnum;
import com.kickstart.woc.wocdriverapp.utils.map.UserClient;

import java.util.ArrayList;
import java.util.List;

public class MapViewFragment extends Fragment implements OnMapReadyCallback,
        View.OnClickListener {

    private static final String TAG = MapViewFragment.class.getSimpleName();

    private MapView mMapView;
    private GoogleMap mGoogleMap;
    private MapViewSizeListener mMapViewSizeListener;
    private Handler mHandler = new Handler();
    private Runnable mRunnable;
    private MapInputContainerEnum mapInputContainerEnum;
    private UserClient userClient;
    private GeoApiContext mGeoApiContext = null;
    private List<PolylineData> mPolylineData = new ArrayList<>();
    private LatLng driverLocation;
    private boolean shouldGetLiveDirections;
    private String distance;
    private String time;
    private List<Marker> markers = new ArrayList<>();
    private String startMarkerTitle;
    private String endMarkerTitle;

    public interface MapViewSizeListener {
        void onMapViewSizeChange();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        userClient = (UserClient) getContext().getApplicationContext();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_map_view, container, false);
        mMapView = view.findViewById(R.id.map);
        view.findViewById(R.id.btn_full_screen_map).setOnClickListener(this);
        view.findViewById(R.id.btn_reset_map).setOnClickListener(this);
        initGoogleMap(savedInstanceState);
        startMapInputContainerEnumRunnable();
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mMapViewSizeListener = (MapViewSizeListener) context;
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
            mapViewBundle = savedInstanceState.getBundle(WocConstants.MAPVIEW_BUNDLE_KEY);
        }
        mMapView.onCreate(mapViewBundle);
        mMapView.getMapAsync(this);
        if (mGeoApiContext == null) {
            mGeoApiContext = new GeoApiContext.Builder().apiKey(getString(R.string.google_maps_key)).build();
        }
        driverLocation = userClient.getDriverLatLng();
    }

    private void startMapInputContainerEnumRunnable() {
        mHandler.postDelayed(mRunnable = new Runnable() {
            @Override
            public void run() {
                if (!userClient.isDriverAvailable()) {
                    mHandler.removeCallbacks(mRunnable);
                } else {
                    Log.d(TAG, "startMapInputContainerEnumRunnable: starting runnable to check screen");
                    mapInputContainerEnum = userClient.getMapInputContainerEnum();
                    if (isDriverLocationChanged()) {
                        driverLocation = userClient.getDriverLatLng();
                        drawMapRoute();
                    }
                    mHandler.postDelayed(mRunnable, WocConstants.LOCATION_UPDATE_INTERVAL);
                }
            }
        }, WocConstants.UPDATE_INTERVAL);
    }

    private boolean isDriverLocationChanged() {
        LatLng oldLocation = driverLocation;
        LatLng newLocation = userClient.getDriverLatLng();
        boolean flag = !oldLocation.equals(newLocation);
        Log.d(TAG, "isDriverLocationChanged: " + flag + ", oldLocation: " + oldLocation + ", newLocation: " + newLocation);
        return flag;
    }

    private void drawMapRoute() {
        mapInputContainerEnum = userClient.getMapInputContainerEnum();
        Log.d(TAG, "drawMapRoute");
        switch (mapInputContainerEnum) {
            case DriverRideFoundFragment:
                calculateDirections(userClient.getSource());
                break;
            case DriverEnterRiderPinFragment:
            case DriverOnTripFragment:
                calculateDirections(userClient.getDestination());
                break;
            default:
                addMapMarker();
        }
    }

    private void calculateDirections(String destinationAddress) {
        resetMap();
        shouldGetLiveDirections = true;
        startMarkerTitle = userClient.getDriverDetails().getName();
        endMarkerTitle = destinationAddress;
        DirectionsApiRequest directions = new DirectionsApiRequest(mGeoApiContext);
        Log.d(TAG, "calculateDirections, live location: " + driverLocation.latitude + ", " + driverLocation.longitude);
        com.google.maps.model.LatLng latLng = new com.google.maps.model.LatLng(driverLocation.latitude, driverLocation.longitude);
        directions.origin(latLng);
        Log.d(TAG, "calculateDirections, destination location: " + destinationAddress);
        directions.destination(destinationAddress).setCallback(new PendingResult.Callback<DirectionsResult>() {
            @Override
            public void onResult(DirectionsResult result) {
                addPolylinesToMap(result);
            }

            @Override
            public void onFailure(Throwable e) {
                Log.e(TAG, "calculateDirections: Failed to get directions: " + e.getMessage());
                showErrorAlert(null);
            }
        });
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Bundle mapViewBundle = outState.getBundle(WocConstants.MAPVIEW_BUNDLE_KEY);
        if (mapViewBundle == null) {
            mapViewBundle = new Bundle();
            outState.putBundle(WocConstants.MAPVIEW_BUNDLE_KEY, mapViewBundle);
        }
        mMapView.onSaveInstanceState(mapViewBundle);
    }

    @Override
    public void onResume() {
        super.onResume();
        mMapView.onResume();
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
        mGoogleMap.getUiSettings().setRotateGesturesEnabled(true);
        mGoogleMap.getUiSettings().setScrollGesturesEnabled(true);
        mGoogleMap.getUiSettings().setTiltGesturesEnabled(true);
        mGoogleMap.getUiSettings().setZoomGesturesEnabled(true);
        Log.d(TAG, "onMapReady");
        setCameraView();
        drawMapRoute();
    }

    private void addMapMarker() {
        resetMap();
        Marker mLiveMarker = mGoogleMap.addMarker(new MarkerOptions()
                .position(driverLocation)
                .title(userClient.getDriverDetails().getName()));
        mLiveMarker.setTag("live");
        mLiveMarker.showInfoWindow();
        markers.add(mLiveMarker);
        setCameraView();
    }

    private void resetMap() {
        if (mGoogleMap != null) {
            Log.d(TAG, "resetMap");
            mGoogleMap.clear();
            for (Marker m : markers) {
                m.remove();
            }
            if (markers.size() > 0) {
                markers.clear();
                markers = new ArrayList<>();
            }
            if (mPolylineData.size() > 0) {
                mPolylineData.clear();
                mPolylineData = new ArrayList<>();
            }
        }
    }

    private void setCameraView() {
        mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                driverLocation, WocConstants.DEFAULT_ZOOM));
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
                drawMapRoute();
                break;
            }
        }
    }

    private void addPolylinesToMap(final DirectionsResult result) {
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
                for (DirectionsRoute route : result.routes) {
                    Log.d(TAG, "run: leg: " + route.legs[0].toString());
                    List<com.google.maps.model.LatLng> decodedPath = PolylineEncoding.decode(route.overviewPolyline.getEncodedPath());
                    List<LatLng> newDecodedPath = new ArrayList<>();
                    // This loops through all the LatLng coordinates of ONE polyline.
                    for (com.google.maps.model.LatLng latLng : decodedPath) {
                        newDecodedPath.add(new LatLng(
                                latLng.lat,
                                latLng.lng
                        ));
                    }
                    Polyline polyline = mGoogleMap.addPolyline(new PolylineOptions().addAll(newDecodedPath));
                    polyline.setColor(ContextCompat.getColor(getActivity(), R.color.blue));
                    polyline.setClickable(true);
                    mPolylineData.add(new PolylineData(polyline, route.legs[0]));

                    double duration = route.legs[0].duration.inSeconds;
                    if (duration < maxDuration) {
                        maxDuration = duration;
                        populateMarkerAlert(mPolylineData.get(0));
                        zoomRoute(polyline.getPoints());
                    }
                }
            }
        });
    }

    private void populateMarkerAlert(PolylineData polylineData) {
        distance = polylineData.getLeg().distance.humanReadable;
        time = polylineData.getLeg().duration.humanReadable;
        userClient.setDistance(distance);
        userClient.setTime(time);

        // Highlight start location
        LatLng startLocation = new LatLng(
                polylineData.getLeg().startLocation.lat,
                polylineData.getLeg().startLocation.lng
        );
        Log.d(TAG, "Marker start: " + startMarkerTitle);
        Marker startMarker = mGoogleMap.addMarker(new MarkerOptions()
                .position(startLocation)
                .title(startMarkerTitle)
        );
        startMarker.setTag("start");
        startMarker.showInfoWindow();

        // Highlight end location
        LatLng endLocation = new LatLng(
                polylineData.getLeg().endLocation.lat,
                polylineData.getLeg().endLocation.lng
        );
        Log.d(TAG, "Marker end: " + endMarkerTitle);
        Marker endMarker = mGoogleMap.addMarker(new MarkerOptions()
                .position(endLocation)
                .title(endMarkerTitle)
        );
        endMarker.setTag("end");
        endMarker.showInfoWindow();
    }

    public void zoomRoute(List<LatLng> lstLatLngRoute) {
        if (mGoogleMap == null || lstLatLngRoute == null || lstLatLngRoute.isEmpty()) return;
        LatLngBounds.Builder boundsBuilder = new LatLngBounds.Builder();
        for (LatLng latLngPoint : lstLatLngRoute) {
            boundsBuilder.include(latLngPoint);
        }
        int routePadding = 120;
        LatLngBounds latLngBounds = boundsBuilder.build();
        mGoogleMap.animateCamera(
                CameraUpdateFactory.newLatLngBounds(latLngBounds, routePadding),
                600,
                null
        );
    }

    private void showErrorAlert(String errorMessage) {
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                LayoutInflater factory = LayoutInflater.from(getContext());
                final View view = factory.inflate(R.layout.layout_warning, null);
                TextView mErrorText = view.findViewById(R.id.errorMessage);
                if (errorMessage == null || errorMessage.length() == 0) {
                    mErrorText.setText(getResources().getText(R.string.error));
                } else {
                    mErrorText.setText(errorMessage);
                }
                builder.setView(view);
                builder.setCancelable(true)
                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, final int id) {
                                dialog.dismiss();
                            }
                        });
                AlertDialog alert = builder.create();
                alert.show();
            }
        });
    }
}
