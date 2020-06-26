package com.kickstart.woc.wocdriverapp.ui.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.kickstart.woc.wocdriverapp.R;
import com.kickstart.woc.wocdriverapp.utils.FragmentUtils;
import com.kickstart.woc.wocdriverapp.utils.map.MapInputContainerEnum;
import com.kickstart.woc.wocdriverapp.utils.map.UserClient;

import java.io.IOException;

public class DriverHomeFragment extends Fragment {

    private static final String TAG = DriverHomeFragment.class.getSimpleName();

    private FragmentUtils fragmentUtils = new FragmentUtils();
    private UserClient userClient;
    private MapInputContainerEnum mapInputContainerEnum;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        userClient = (UserClient) getContext().getApplicationContext();
        if (getArguments() != null) {
            String enumKey = (String) getArguments().get(getString(R.string.intent_input_container));
            mapInputContainerEnum = Enum.valueOf(MapInputContainerEnum.class, enumKey);
        }
        if (mapInputContainerEnum.compareTo(MapInputContainerEnum.Unknown) == 0) {
            if (userClient.isDriverVerified()) {
                mapInputContainerEnum = MapInputContainerEnum.DriverAvailabilityFragment;
            } else {
                mapInputContainerEnum = MapInputContainerEnum.DriverVerificationFragment;
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_driver_home, container, false);
        renderInputViewContainer();
        return view;
    }

    private void renderInputViewContainer() {
        switch (mapInputContainerEnum) {
            case DriverVerificationFragment:
                fragmentUtils.replaceFragment(R.id.map_view_container, TAG, getFragmentManager(), new MapViewFragment());
                fragmentUtils.replaceFragment(R.id.input_view_container, TAG, getFragmentManager(), new DriverVerificationFragment());
                break;
            case DriverAvailabilityFragment:
                fragmentUtils.replaceFragment(R.id.map_view_container, TAG, getFragmentManager(), new MapViewFragment());
                fragmentUtils.replaceFragment(R.id.input_view_container, TAG, getFragmentManager(), new DriverAvailabilityFragment());
                break;
            case DriverRideFoundFragment:
                fragmentUtils.replaceFragment(R.id.map_view_container, TAG, getFragmentManager(), new MapViewFragment());
                fragmentUtils.replaceFragment(R.id.input_view_container, TAG, getFragmentManager(), new DriverRideFoundFragment());
                break;
            case DriverEnterRiderPinFragment:
                fragmentUtils.replaceFragment(R.id.map_view_container, TAG, getFragmentManager(), new MapViewFragment());
                fragmentUtils.replaceFragment(R.id.input_view_container, TAG, getFragmentManager(), new DriverEnterRiderPinFragment());
                break;
            case DriverOnTripFragment:
                fragmentUtils.replaceFragment(R.id.map_view_container, TAG, getFragmentManager(), new MapViewFragment());
                fragmentUtils.replaceFragment(R.id.input_view_container, TAG, getFragmentManager(), new DriverOnTripFragment());
                break;
            case DriverTripSummaryFragment:
                fragmentUtils.replaceFragment(R.id.driverHomeContainer, TAG, getFragmentManager(), new DriverTripSummaryFragment());
                break;
            case Unknown:
                try {
                    throw new IOException();
                } catch (IOException e) {
                    Log.d(TAG, "IOException: " + e.getMessage());
                }
                break;
        }
    }
}
