package com.kickstart.woc.wocdriverapp.ui.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.kickstart.woc.wocdriverapp.R;
import com.kickstart.woc.wocdriverapp.model.User;
import com.kickstart.woc.wocdriverapp.ui.listeners.ReplaceInputContainerListener;
import com.kickstart.woc.wocdriverapp.utils.FragmentUtils;
import com.kickstart.woc.wocdriverapp.utils.map.MapInputContainerEnum;
import com.kickstart.woc.wocdriverapp.utils.map.UserClient;

public class DriverOnTripFragment extends Fragment implements View.OnClickListener {

    private static final String TAG = DriverOnTripFragment.class.getSimpleName();

    private User driver;
    private User rider;
    private UserClient userClient = new UserClient();
    private FragmentUtils fragmentUtils = new FragmentUtils();
    private ReplaceInputContainerListener replaceInputContainerListener;

    private Button mEndRideButton;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        driver = userClient.getDriverDetails();
        rider = userClient.getRiderDetails();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_driver_on_trip, container, false);
        EditText mSource = view.findViewById(R.id.sourceET);
        EditText mDestination = view.findViewById(R.id.destinationET);
        mSource.setCompoundDrawables(null, null, null, null);
        mDestination.setCompoundDrawables(null, null, null, null);
        mEndRideButton = view.findViewById(R.id.endRide);
        mEndRideButton.setOnClickListener(this::onClick);
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
            case R.id.endRide:
                showEndRideAlert();
                break;
        }
    }

    private void showEndRideAlert() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        LayoutInflater factory = LayoutInflater.from(getContext());
        View view = factory.inflate(R.layout.layout_end_ride, null);
        Button mCancelRideAlert = view.findViewById(R.id.cancelRideAlert);
        Button mEndRideAlert = view.findViewById(R.id.endRideAlert);
        builder.setView(view)
                .setCancelable(true);
        AlertDialog alert = builder.create();
        mCancelRideAlert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alert.dismiss();
            }
        });
        mEndRideAlert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userClient.endRide();
                Toast.makeText(getContext(), "Accept Ride", Toast.LENGTH_LONG).show();
                alert.dismiss();
                replaceInputContainerListener.onReplaceInputContainer(MapInputContainerEnum.DriverTripSummaryFragment);
            }
        });
        alert.show();
    }
}
