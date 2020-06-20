package com.kickstart.woc.wocdriverapp.ui.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.kickstart.woc.wocdriverapp.R;
import com.kickstart.woc.wocdriverapp.model.User;
import com.kickstart.woc.wocdriverapp.ui.listeners.ReplaceInputContainerListener;
import com.kickstart.woc.wocdriverapp.utils.map.MapInputContainerEnum;
import com.kickstart.woc.wocdriverapp.utils.map.UserClient;

public class DriverRideFoundFragment extends Fragment implements View.OnClickListener {

    private static final String TAG = DriverRideFoundFragment.class.getSimpleName();

    private User driver;
    private User rider;
    private UserClient userClient = new UserClient();
    private ReplaceInputContainerListener replaceInputContainerListener;

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
        View view = inflater.inflate(R.layout.layout_driver_ride_found, null, true);
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
        if (context instanceof ReplaceInputContainerListener) {
            //init the listener
            replaceInputContainerListener = (ReplaceInputContainerListener) context;
        } else {
            throw new IllegalArgumentException(context.toString()
                    + " must implement ReplaceFullViewFragmentListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        replaceInputContainerListener = null;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.dNavigateToRider:
                Toast.makeText(getContext(), "navigate to rider", Toast.LENGTH_LONG).show();
                break;
            case R.id.dCallRider:
                Toast.makeText(getContext(), "Call Rider", Toast.LENGTH_LONG).show();
                break;
            case R.id.startTrip:
                Toast.makeText(getContext(), "Start Trip", Toast.LENGTH_LONG).show();
                replaceInputContainerListener.onReplaceInputContainer(MapInputContainerEnum.DriverEnterRiderOTPFragment);
                break;
            case R.id.cancelRide:
                Toast.makeText(getContext(), "Cancel Ride", Toast.LENGTH_LONG).show();
                replaceInputContainerListener.onReplaceInputContainer(MapInputContainerEnum.DriverAvailabilityFragment);
                break;
        }
    }
}
