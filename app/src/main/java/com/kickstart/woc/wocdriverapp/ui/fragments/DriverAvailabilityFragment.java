package com.kickstart.woc.wocdriverapp.ui.fragments;

import android.content.Context;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.SwitchCompat;
import androidx.fragment.app.Fragment;

import com.kickstart.woc.wocdriverapp.R;
import com.kickstart.woc.wocdriverapp.model.User;
import com.kickstart.woc.wocdriverapp.ui.listeners.PhoneCallListener;
import com.kickstart.woc.wocdriverapp.ui.listeners.ReplaceInputContainerListener;
import com.kickstart.woc.wocdriverapp.utils.WocConstants;
import com.kickstart.woc.wocdriverapp.utils.map.MapInputContainerEnum;
import com.kickstart.woc.wocdriverapp.utils.map.UserClient;

public class DriverAvailabilityFragment extends Fragment implements View.OnClickListener {

    private static final String TAG = DriverAvailabilityFragment.class.getSimpleName();

    private User driver;
    private User rider;
    private UserClient userClient = new UserClient();
    private ReplaceInputContainerListener replaceInputContainerListener;
    private PhoneCallListener phoneCallListener;

    private Button mContactSupportButton;
    private SwitchCompat mSwitchCompat;
    private TextView mAvailabilityText;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        driver = userClient.getDriverDetails();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_driver_availability, null, true);
//        Log.d(TAG, "onCreateView: rider location: " + driver.toString());
        mContactSupportButton = view.findViewById(R.id.btnContactSupport);
        mSwitchCompat = view.findViewById(R.id.switchButton);
        mAvailabilityText = view.findViewById(R.id.dAvailabilityText);

        mContactSupportButton.setOnClickListener(this::onClick);
        mSwitchCompat.setOnClickListener(this::onClick);

        if (driver.isAvailable()) {
            mSwitchCompat.setChecked(true);
            mAvailabilityText.setText(getString(R.string.dOnDuty));
            mAvailabilityText.setBackground(getResources().getDrawable(R.drawable.blue_border_on));
            mAvailabilityText.setTextColor(getResources().getColor(R.color.white));
        }
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (driver.isAvailable()) {
            rider = userClient.notifyRiderRequest();
            if (rider != null) {
                showAlertToAcceptRide();
            }
        }
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
            case R.id.switchButton:
                if (mSwitchCompat.isChecked()) {
                    mAvailabilityText.setText(getString(R.string.dOnDuty));
                    mAvailabilityText.setBackground(getResources().getDrawable(R.drawable.blue_border_on));
                    mAvailabilityText.setTextColor(getResources().getColor(R.color.white));
                } else {
                    mAvailabilityText.setText(getString(R.string.dOffDuty));
                    mAvailabilityText.setBackground(getResources().getDrawable(R.drawable.blue_border_off));
                    mAvailabilityText.setTextColor(getResources().getColor(R.color.blueTrack));
                }
                break;
            case R.id.btnContactSupport:
                phoneCallListener.onMakePhoneCall(MapInputContainerEnum.DriverAvailabilityFragment, WocConstants.CONTACT_SUPPORT);
                break;
        }
    }

    private void showAlertToAcceptRide() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        LayoutInflater factory = LayoutInflater.from(getContext());
        View view = factory.inflate(R.layout.layout_ride_accept_timer_details, null);
        final TextView tvTimer = view.findViewById(R.id.timer);
        CountDownTimer timer = new CountDownTimer(90000, 1000) {
            @Override
            public void onTick(long milliSecondsLeftToAccept) {
                String time = String.valueOf(milliSecondsLeftToAccept / 1000);
                tvTimer.setText(time);
            }

            @Override
            public void onFinish() {
                tvTimer.setText("0");
                userClient.cancelRideRequest();
            }
        };
        timer.start();
        Button mAcceptButton = view.findViewById(R.id.acceptRide);
        Button mRejectButton = view.findViewById(R.id.rejectRide);
        builder.setView(view)
                .setCancelable(true);
        AlertDialog alert = builder.create();
        mAcceptButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alert.dismiss();
                replaceInputContainerListener.onReplaceInputContainer(MapInputContainerEnum.DriverRideFoundFragment);
            }
        });
        mRejectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userClient.cancelRideRequest();
                alert.dismiss();
            }
        });
        alert.show();
    }
}
