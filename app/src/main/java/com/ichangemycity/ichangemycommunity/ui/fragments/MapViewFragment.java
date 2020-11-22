package com.ichangemycity.ichangemycommunity.ui.fragments;

import android.content.Context;
import android.os.Bundle;
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
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.GeoApiContext;
import com.ichangemycity.ichangemycommunity.R;
import com.ichangemycity.ichangemycommunity.model.PolylineData;
import com.ichangemycity.ichangemycommunity.utils.IChangeMyCommunityConstants;
import com.ichangemycity.ichangemycommunity.utils.map.UserClient;

import java.util.ArrayList;
import java.util.List;

import static com.ichangemycity.ichangemycommunity.utils.IChangeMyCommunityConstants.MAPVIEW_BUNDLE_KEY;

public class MapViewFragment extends Fragment implements OnMapReadyCallback,
        View.OnClickListener {

    private static final String TAG = MapViewFragment.class.getSimpleName();

    private MapView mMapView;
    private GoogleMap mGoogleMap;
    private MapViewSizeListener mMapViewSizeListener;
    private UserClient userClient;
    private GeoApiContext mGeoApiContext = null;
    private LatLng userLocation;
    private List<Marker> markers = new ArrayList<>();

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
            mapViewBundle = savedInstanceState.getBundle(MAPVIEW_BUNDLE_KEY);
        }
        mMapView.onCreate(mapViewBundle);
        mMapView.getMapAsync(this);
        if (mGeoApiContext == null) {
            mGeoApiContext = new GeoApiContext.Builder().apiKey(getString(R.string.google_maps_key)).build();
        }
        userLocation = userClient.getUserLatLng();
    }

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
        addMapMarker();
        setCameraView();
    }

    private void addMapMarker() {
        resetMap();
        Marker mLiveMarker = mGoogleMap.addMarker(new MarkerOptions()
                .position(userLocation)
                .title(userClient.fetchUser().getName()));
        mLiveMarker.setTag("live");
        mLiveMarker.showInfoWindow();
        markers.add(mLiveMarker);
    }

    private void setCameraView() {
        mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                userLocation, IChangeMyCommunityConstants.DEFAULT_ZOOM));
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
        }
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
        }
    }
}
