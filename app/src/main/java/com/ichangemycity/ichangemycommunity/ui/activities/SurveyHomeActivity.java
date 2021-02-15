package com.ichangemycity.ichangemycommunity.ui.activities;

import android.Manifest;
import android.app.ActivityManager;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.ichangemycity.ichangemycommunity.R;
import com.ichangemycity.ichangemycommunity.services.LocationService;
import com.ichangemycity.ichangemycommunity.ui.fragments.MapViewFragment;
import com.ichangemycity.ichangemycommunity.ui.fragments.SurveyCategoryFragment;
import com.ichangemycity.ichangemycommunity.ui.fragments.SurveyFormFragment;
import com.ichangemycity.ichangemycommunity.ui.fragments.SurveyHomeFragment;
import com.ichangemycity.ichangemycommunity.ui.fragments.WalkabilityFragment;
import com.ichangemycity.ichangemycommunity.ui.listeners.ReplaceInputContainerListener;
import com.ichangemycity.ichangemycommunity.ui.listeners.SelectSurveyFilterListener;
import com.ichangemycity.ichangemycommunity.ui.listeners.SubmitSurveyListener;
import com.ichangemycity.ichangemycommunity.utils.FragmentUtils;
import com.ichangemycity.ichangemycommunity.utils.map.MapInputContainerEnum;
import com.ichangemycity.ichangemycommunity.utils.map.MapUtils;
import com.ichangemycity.ichangemycommunity.utils.map.UserClient;

import static com.ichangemycity.ichangemycommunity.utils.IChangeMyCommunityConstants.ERROR_DIALOG_REQUEST;
import static com.ichangemycity.ichangemycommunity.utils.IChangeMyCommunityConstants.INITIAL_LOCATION_BROADCAST;
import static com.ichangemycity.ichangemycommunity.utils.IChangeMyCommunityConstants.MAP_LAYOUT_STATE_CONTRACTED;
import static com.ichangemycity.ichangemycommunity.utils.IChangeMyCommunityConstants.MAP_LAYOUT_STATE_EXPANDED;
import static com.ichangemycity.ichangemycommunity.utils.IChangeMyCommunityConstants.PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION;
import static com.ichangemycity.ichangemycommunity.utils.IChangeMyCommunityConstants.PERMISSIONS_REQUEST_ENABLE_GPS;
import static com.ichangemycity.ichangemycommunity.utils.IChangeMyCommunityConstants.UPDATE_INTERVAL;

public class SurveyHomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        MapViewFragment.MapViewSizeListener,
        ReplaceInputContainerListener,
        SelectSurveyFilterListener,
        SubmitSurveyListener {

    private static final String TAG = SurveyHomeActivity.class.getSimpleName();

    private MapUtils mapUtils = new MapUtils();
    private FusedLocationProviderClient mFusedLocationClient;
    private UserClient userClient;
    private FragmentUtils fragmentUtils = new FragmentUtils();
    private MapInputContainerEnum mapInputContainerEnum;
    private Handler mHandler = new Handler();
    private Runnable mRunnable;

    private int mMapLayoutState = 0;
    private boolean mLocationPermissionGranted = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation_screen);
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);

//        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
//        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
//                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
//        drawer.addDrawerListener(toggle);
//        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        userClient = (UserClient) getApplicationContext();
        startDriverRunnable();
    }

    @Override
    protected void onPause() {
        super.onPause();
        // Unregister since the activity is paused.
        LocalBroadcastManager.getInstance(this).unregisterReceiver(
                mMessageReceiver);
    }

    @Override
    protected void onResume() {
        super.onResume();
        LocalBroadcastManager.getInstance(this).registerReceiver(
                mMessageReceiver, new IntentFilter(INITIAL_LOCATION_BROADCAST));
    }

    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (userClient.isInitialLocationBroadcast() && userClient.getMapInputContainerEnum().compareTo(MapInputContainerEnum.LoaderFragment) == 0) {
                Log.d("receiver", "Got message");
                userClient.setInitialLocationBroadcast(false);
                userClient.setMapInputContainerEnum(MapInputContainerEnum.SurveyFormFragment);
                onReplaceInputContainer();
            }
        }
    };

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        Fragment f = getSupportFragmentManager().findFragmentById(R.id.input_view_container);
        Fragment f1 = getSupportFragmentManager().findFragmentById(R.id.surveyHomeContainer);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else if (f1 instanceof SurveyCategoryFragment ||
                f1 instanceof WalkabilityFragment) {
            Log.i("BACK PRESSED", "BACK PRESSED");
        } else if (f instanceof SurveyFormFragment) {//the fragment on which you want to handle your back press
            Log.i("BACK PRESSED", "BACK PRESSED");
        } else {
            super.onBackPressed();
        }
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.navigation_screen, menu);
//        return true;
//    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

//        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
            Intent i = new Intent(SurveyHomeActivity.this,
                    SurveyHomeActivity.class);
            startActivity(i);
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    /*
       Step 0: Check Map Service
       a. Determine if google services can be used on the device
       b. Determine if GPS is enabled on the device, send to settings screen to enable GPS
       c. If GPS is not enabled, explicitly asking for location permissions
    */
    private boolean checkMapServices() {
        if (isServicesOK()) {
            if (isMapsEnabled()) {
                return true;
            }
        }
        return false;
    }

    // Used to determine if google services can be used by device
    public boolean isServicesOK() {
        Log.d(TAG, "isServicesOK: checking google services version");

        int available = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(this);

        if (available == ConnectionResult.SUCCESS) {
            //everything is fine and the user can make map requests
            Log.d(TAG, "isServicesOK: Google Play Services is working");
            return true;
        } else if (GoogleApiAvailability.getInstance().isUserResolvableError(available)) {
            //an error occured but we can resolve it
            Log.d(TAG, "isServicesOK: an error occured but we can fix it");
            Dialog dialog = GoogleApiAvailability.getInstance().getErrorDialog(this, available, ERROR_DIALOG_REQUEST);
            dialog.show();
        } else {
            Toast.makeText(this, "You can't make map requests", Toast.LENGTH_SHORT).show();
            Log.d(TAG, "You can't make map requests");
        }
        return false;
    }

    // Used to determine if maps is enabled on the device
    public boolean isMapsEnabled() {
        final LocationManager manager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            buildAlertMessageNoGps();
            return false;
        }
        return true;
    }

    // This will open Settings on the device to enable GPS
    private void buildAlertMessageNoGps() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("This application requires GPS to work properly, do you want to enable it?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        Intent enableGpsIntent = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        startActivityForResult(enableGpsIntent, PERMISSIONS_REQUEST_ENABLE_GPS);
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }

    // startActivityForResult is triggered to enable GPS
    // If GPS is enabled, we continue with request
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d(TAG, "onActivityResult: called.");
        switch (requestCode) {
            case PERMISSIONS_REQUEST_ENABLE_GPS:
                if (mLocationPermissionGranted) {
                    getLastKnownLocation();
                } else {
                    getLocationPermission();
                }
                break;
        }
    }

    private void getLocationPermission() {
        /*
         * Request location permission, so that we can get the location of the
         * device. The result of the permission request is handled by a callback,
         * onRequestPermissionsResult.
         */
        if (ContextCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mLocationPermissionGranted = true;
            getLastKnownLocation();
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }
    }


    // Run after location permissions are selected
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[],
                                           @NonNull int[] grantResults) {
        mLocationPermissionGranted = false;
        switch (requestCode) {
            case PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION:
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    mLocationPermissionGranted = true;
                    getLastKnownLocation();
                }
                break;
        }
    }

    private void getLastKnownLocation() {
        Log.d(TAG, "getLastKnownLocation: called.");
        mapInputContainerEnum = userClient.getMapInputContainerEnum();
        if (userClient.isInitialLocationBroadcast() && mapInputContainerEnum.compareTo(MapInputContainerEnum.LoaderFragment) == 0) {
            startLocationService();
        } else {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            mFusedLocationClient.getLastLocation().addOnCompleteListener(
                    new OnCompleteListener<Location>() {
                        @Override
                        public void onComplete(@NonNull Task<Location> task) {
                            if (task.isSuccessful()) {
                                Location location = task.getResult();
                                if (location != null) {
                                    userClient.saveUserLocation(location.getLatitude(), location.getLongitude());
                                }
                            }
                        }
                    }
            );
        }
    }

    private void startLocationService() {
        if (!isLocationServiceRunning()) {
            Intent serviceIntent = new Intent(getApplicationContext(), LocationService.class);
            getApplicationContext().startService(serviceIntent);
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                this.startForegroundService(serviceIntent);
            }
        }
    }

    private boolean isLocationServiceRunning() {
        ActivityManager manager = (ActivityManager) this.getSystemService(this.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if ("LocationService".equals(service.service.getClassName())) {
                Log.d(TAG, "isLocationServiceRunning: location service is already running.");
                return true;
            }
        }
        Log.d(TAG, "isLocationServiceRunning: location service is not running.");
        return false;
    }

    @Override
    public void onReplaceInputContainer() {
        SurveyHomeFragment driverHomeFragment = new SurveyHomeFragment();
        fragmentUtils.replaceFragment(R.id.survey_home_fragment, TAG, getSupportFragmentManager(), driverHomeFragment);
    }

    private void startDriverRunnable() {
        mHandler.postDelayed(mRunnable = new Runnable() {
            @Override
            public void run() {
                userClient.setMapInputContainerEnum(MapInputContainerEnum.LoaderFragment);
                onReplaceInputContainer();
                if (checkMapServices()) {
                    if (mLocationPermissionGranted) {
                        getLastKnownLocation();
                    } else {
                        getLocationPermission();
                    }
                }
                mHandler.removeCallbacks(mRunnable);
            }
        }, UPDATE_INTERVAL);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mHandler.removeCallbacks(mRunnable);
        Intent serviceIntent = new Intent(this, LocationService.class);
        Log.d(TAG, "stopping LocationService");
        this.stopService(serviceIntent);
    }

    @Override
    public void onMapViewSizeChange() {
        int mapSize = 70;
        FrameLayout mMapViewContainer = findViewById(R.id.map_view_container);
        FrameLayout mInputViewContainer = findViewById(R.id.input_view_container);
        if (mMapLayoutState == MAP_LAYOUT_STATE_CONTRACTED) {
            mMapLayoutState = MAP_LAYOUT_STATE_EXPANDED;
            mapUtils.expandMapAnimation(mMapViewContainer, mapSize, 100,
                    mInputViewContainer, 100 - mapSize, 0);
        } else if (mMapLayoutState == MAP_LAYOUT_STATE_EXPANDED) {
            mMapLayoutState = MAP_LAYOUT_STATE_CONTRACTED;
            mapUtils.contractMapAnimation(mMapViewContainer, 100, mapSize,
                    mInputViewContainer, 0, 100 - mapSize);
        }
    }

    @Override
    public void onSelectFilter(String filter) {
        MapViewFragment mapViewFragment = new MapViewFragment();
        Bundle args = new Bundle();
        args.putString("SurveyDropDown", filter);
        mapViewFragment.setArguments(args);
        fragmentUtils.replaceFragment(R.id.map_view_container, TAG, getSupportFragmentManager(), mapViewFragment);
    }

    @Override
    public void onSubmitSurvey() {
        MapViewFragment mapViewFragment = new MapViewFragment();
        Bundle args = new Bundle();
        args.putBoolean("submitsurvey", true);
        mapViewFragment.setArguments(args);
        fragmentUtils.replaceFragment(R.id.map_view_container, TAG, getSupportFragmentManager(), mapViewFragment);
    }
}
