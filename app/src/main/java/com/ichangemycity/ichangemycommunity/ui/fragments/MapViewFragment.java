package com.ichangemycity.ichangemycommunity.ui.fragments;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.maps.GeoApiContext;
import com.google.maps.android.clustering.ClusterManager;
import com.ichangemycity.ichangemycommunity.R;
import com.ichangemycity.ichangemycommunity.model.ClusterMarker;
import com.ichangemycity.ichangemycommunity.model.Survey;
import com.ichangemycity.ichangemycommunity.utils.IChangeMyCommunityConstants;
import com.ichangemycity.ichangemycommunity.utils.map.ClusterManagerRenderer;
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
    private Handler mHandler = new Handler();
    private Runnable mRunnable;
    private GeoApiContext mGeoApiContext = null;
    private LatLng userLocation;
    private ClusterManager<ClusterMarker> mClusterManager;
    private ClusterManagerRenderer mClusterManagerRenderer;
    private ArrayList<ClusterMarker> mClusterMarkers = new ArrayList<>();
    private List<Survey> mSurveyList = new ArrayList<>();
    private Context mContext;
    private ClusterMarker mSelectedClusterMarker;

    public interface MapViewSizeListener {
        void onMapViewSizeChange();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        userClient = (UserClient) getContext().getApplicationContext();
        mContext = getActivity().getApplicationContext();
        if (mSurveyList.size() == 0) {
            Bundle bundle = getArguments();
            if (bundle != null) {
                if (bundle.containsKey("submitsurvey")) {
                    Log.d(TAG, "submitsurvey not null");
                }
                if (bundle.containsKey("SurveyDropDown")) {
                    String key = (String) getArguments().get("SurveyDropDown");
                    if (key != null) {
                        mSurveyList = userClient.filterSurveyList(key);
                    } else {
                        mSurveyList = userClient.getSurveyList();
                    }
                    Log.d(TAG, "getArguments() not null");
                }
            }
        }
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
        setCameraView();
        addSurveyMapMarkers();
    }

    private void addSurveyMapMarkers() {
        if (mGoogleMap != null) {
            if (mClusterManager == null) {
                mClusterManager = new ClusterManager<ClusterMarker>(mContext, mGoogleMap);
            }
            if (mClusterManagerRenderer == null) {
                mClusterManagerRenderer = new ClusterManagerRenderer(
                        getActivity(),
                        mGoogleMap,
                        mClusterManager
                );
                mClusterManager.setRenderer(mClusterManagerRenderer);
            }

            mClusterManager.setOnClusterItemClickListener(new ClusterManager.OnClusterItemClickListener<ClusterMarker>() {
                @Override
                public boolean onClusterItemClick(ClusterMarker item) {
                    Log.d("cluster item", "clicked");
                    mSelectedClusterMarker = item;
                    return false;
                }
            });

            mClusterManager.getMarkerCollection()
                    .setInfoWindowAdapter(new CustomInfoViewAdapter(LayoutInflater.from(getContext())));
            mGoogleMap.setInfoWindowAdapter(mClusterManager.getMarkerManager());
            mGoogleMap.setOnMarkerClickListener(mClusterManager);

            for (Survey survey : mSurveyList) {
//                Log.d(TAG, "addMapMarkers: survey: " + survey.getPosition().toString());
                try {
                    ClusterMarker newClusterMarker = new ClusterMarker(
                            new LatLng(survey.getPosition().latitude, survey.getPosition().longitude),
                            "title",
                            "",
                            R.drawable.ic_survey,
                            survey
                    );
                    mClusterManager.addItem(newClusterMarker);
                    mClusterMarkers.add(newClusterMarker);

                } catch (NullPointerException e) {
                    Log.e(TAG, "addMapMarkers: NullPointerException: " + e.getMessage());
                }
            }

            mClusterManager.cluster();
        }
    }

    private void setCameraView() {
        mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                userLocation, IChangeMyCommunityConstants.DEFAULT_ZOOM));
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

    public class CustomInfoViewAdapter implements GoogleMap.InfoWindowAdapter {

        private final LayoutInflater mInflater;

        public CustomInfoViewAdapter(LayoutInflater inflater) {
            this.mInflater = inflater;
        }

        @Override
        public View getInfoWindow(Marker marker) {
            final View popup = mInflater.inflate(R.layout.info_window, null);
            return null;
        }

        @Override
        public View getInfoContents(Marker marker) {
            final View popup = mInflater.inflate(R.layout.info_window, null);

            Survey survey = mSelectedClusterMarker.getSurvey();
            ((TextView) popup.findViewById(R.id.txtCategory)).setText(survey.getCategory().toString());
            ((TextView) popup.findViewById(R.id.txtSubCategory)).setText(survey.getSubCategory());
            ((TextView) popup.findViewById(R.id.txtQualityScore)).setText(survey.getQualityScore());
            ((TextView) popup.findViewById(R.id.txtLocation)).setText(survey.getLocation());
            return popup;
        }
    }
}
