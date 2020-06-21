package com.kickstart.woc.wocdriverapp.ui.fragments;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.kickstart.woc.wocdriverapp.R;
import com.kickstart.woc.wocdriverapp.ui.listeners.PhoneCallListener;
import com.kickstart.woc.wocdriverapp.ui.listeners.ReplaceInputContainerListener;
import com.kickstart.woc.wocdriverapp.utils.WocConstants;
import com.kickstart.woc.wocdriverapp.utils.map.MapInputContainerEnum;

public class DriverVerificationFragment extends Fragment {

    private static final String TAG = DriverVerificationFragment.class.getSimpleName();

    private PhoneCallListener phoneCallListener;
    private Button mContactSupportButton;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        phoneCallListener = (PhoneCallListener) context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        phoneCallListener = null;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_driver_verification, null, false);
//        Log.d(TAG, "onCreateView: rider location: " + driver.toString());
        mContactSupportButton = view.findViewById(R.id.btnContactSupport);

        mContactSupportButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                phoneCallListener.onMakePhoneCall(MapInputContainerEnum.DriverVerificationFragment, WocConstants.CONTACT_SUPPORT);
            }
        });
        return view;
    }
}
