package com.kickstart.woc.wocdriverapp.ui.fragments;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.kickstart.woc.wocdriverapp.R;
import com.kickstart.woc.wocdriverapp.ui.listeners.ReplaceInputContainerListener;
import com.kickstart.woc.wocdriverapp.utils.map.MapInputContainerEnum;
import com.kickstart.woc.wocdriverapp.utils.map.UserClient;

public class DriverOnTripFragment extends Fragment implements View.OnClickListener {

    private static final String TAG = DriverOnTripFragment.class.getSimpleName();

    private UserClient userClient;
    private ReplaceInputContainerListener replaceInputContainerListener;

    private Button mEndRideButton;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        userClient = (UserClient) getContext().getApplicationContext();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_driver_on_trip, container, false);
        TextView mSource = view.findViewById(R.id.sourceTV);
        TextView mDestination = view.findViewById(R.id.destinationTV);
        mSource.setText(userClient.getSource());
        mDestination.setText(userClient.getDestination());
        mEndRideButton = view.findViewById(R.id.endRide);
        mEndRideButton.setOnClickListener(this::onClick);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navigateToGoogleMaps();
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
                alert.dismiss();
                replaceInputContainerListener.onReplaceInputContainer(MapInputContainerEnum.DriverTripSummaryFragment);
            }
        });
        alert.show();
    }

    private void navigateToGoogleMaps() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
                        Uri.parse("google.navigation:q=" + userClient.getDestination()));
                startActivity(intent);
            }
        }, 1000);
    }
}
