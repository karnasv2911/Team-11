package com.kickstart.woc.wocdriverapp.ui.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.kickstart.woc.wocdriverapp.R;
import com.kickstart.woc.wocdriverapp.model.User;

public class DriverVerificationFragment extends Fragment {

    private static final String TAG = DriverVerificationFragment.class.getSimpleName();

    private Button mContactSupportButton;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_driver_verification, null, false);
//        Log.d(TAG, "onCreateView: rider location: " + driver.toString());
        mContactSupportButton = view.findViewById(R.id.btnContactSupport);

        mContactSupportButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getContext(), "Call Contact Support", Toast.LENGTH_LONG).show();
            }
        });
        return view;
    }
}
