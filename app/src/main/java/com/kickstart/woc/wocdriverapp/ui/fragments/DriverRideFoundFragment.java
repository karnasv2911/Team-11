package com.kickstart.woc.wocdriverapp.ui.fragments;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.kickstart.woc.wocdriverapp.R;
import com.kickstart.woc.wocdriverapp.model.User;
import com.kickstart.woc.wocdriverapp.ui.listeners.PhoneCallListener;
import com.kickstart.woc.wocdriverapp.ui.listeners.ReplaceInputContainerListener;
import com.kickstart.woc.wocdriverapp.utils.WocConstants;
import com.kickstart.woc.wocdriverapp.utils.map.MapInputContainerEnum;
import com.kickstart.woc.wocdriverapp.utils.map.UserClient;

public class DriverRideFoundFragment extends Fragment implements View.OnClickListener {

    private static final String TAG = DriverRideFoundFragment.class.getSimpleName();

    private User driver;
    private User rider;
    private UserClient userClient = new UserClient();
    private ReplaceInputContainerListener replaceInputContainerListener;
    private PhoneCallListener phoneCallListener;

    private Button mNavigateToRiderButton;
    private Button mCallRiderButton;
    private Button mStartTripButton;
    private Button mCancelRideButton;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        driver = userClient.getDriverDetails();
        rider = userClient.getRiderDetails();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_driver_ride_found, null, true);
        mNavigateToRiderButton = view.findViewById(R.id.dNavigateToRider);
        mCallRiderButton = view.findViewById(R.id.dCallRider);
        mStartTripButton = view.findViewById(R.id.startTrip);
        mCancelRideButton = view.findViewById(R.id.cancelRide);
        mNavigateToRiderButton.setOnClickListener(this::onClick);
        mCallRiderButton.setOnClickListener(this::onClick);
        mStartTripButton.setOnClickListener(this::onClick);
        mCancelRideButton.setOnClickListener(this::onClick);
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        replaceInputContainerListener = (ReplaceInputContainerListener) context;
        phoneCallListener = (PhoneCallListener) context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        replaceInputContainerListener = null;
        phoneCallListener = null;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.dNavigateToRider:
                navigateToGoogleMaps();
                break;
            case R.id.dCallRider:
                phoneCallListener.onMakePhoneCall(MapInputContainerEnum.DriverRideFoundFragment, rider.getPhone());
                break;
            case R.id.startTrip:
                replaceInputContainerListener.onReplaceInputContainer(MapInputContainerEnum.DriverEnterRiderOTPFragment);
                break;
            case R.id.cancelRide:
                replaceInputContainerListener.onReplaceInputContainer(MapInputContainerEnum.DriverAvailabilityFragment);
                break;
        }
    }

    private void navigateToGoogleMaps() {
        Address source = rider.getSourceAddress();
        Address destination = rider.getDestinationAddress();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                String uri = "http://maps.google.com/maps?saddr=" + source.getLatitude() + "," + source.getLongitude() + "&daddr=" + destination.getLatitude() + "," + destination.getLongitude();
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                mapIntent.setPackage("com.google.android.apps.maps");
                startActivity(mapIntent);
            }
        }, 1000);
    }
}
