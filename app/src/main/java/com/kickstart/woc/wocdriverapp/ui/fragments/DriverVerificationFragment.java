package com.kickstart.woc.wocdriverapp.ui.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.kickstart.woc.wocdriverapp.R;
import com.kickstart.woc.wocdriverapp.ui.listeners.PhoneCallListener;
import com.kickstart.woc.wocdriverapp.utils.map.MapInputContainerEnum;
import com.kickstart.woc.wocdriverapp.utils.map.UserClient;

public class DriverVerificationFragment extends Fragment {

    private PhoneCallListener phoneCallListener;
    private UserClient userClient;
    private Button mContactSupportButton;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        userClient = (UserClient) getContext().getApplicationContext();
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
        TextView tv = view.findViewById(R.id.verfPendingText);
        tv.setText(getResources().getText(R.string.verfText));
        mContactSupportButton = view.findViewById(R.id.btnContactSupport);

        mContactSupportButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                phoneCallListener.onMakePhoneCall(MapInputContainerEnum.DriverVerificationFragment, userClient.getContactSupport());
            }
        });
        return view;
    }
}
