package com.kickstart.woc.wocdriverapp.ui.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.kickstart.woc.wocdriverapp.R;
import com.kickstart.woc.wocdriverapp.model.User;
import com.kickstart.woc.wocdriverapp.ui.listeners.ReplaceInputContainerListener;
import com.kickstart.woc.wocdriverapp.utils.FragmentUtils;
import com.kickstart.woc.wocdriverapp.utils.map.MapInputContainerEnum;
import com.kickstart.woc.wocdriverapp.utils.map.UserClient;


public class DriverEnterRiderOTPFragment extends Fragment implements View.OnClickListener {

    private static final String TAG = DriverEnterRiderOTPFragment.class.getSimpleName();

    private User driver;
    private User rider;
    private UserClient userClient = new UserClient();
    private FragmentUtils fragmentUtils = new FragmentUtils();
    private ReplaceInputContainerListener replaceInputContainerListener;

    private EditText mETOTP1;
    private EditText mETOTP2;
    private EditText mETOTP3;
    private EditText mETOTP4;
    private Button mConfirmButton;
    private Button mContactSupportButton;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        driver = userClient.getDriverDetails();
        rider = userClient.getRiderDetails();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_driver_enter_rider_otp, container, false);
        mETOTP1 = view.findViewById(R.id.etOPT1);
        mETOTP1.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                changeEditTextBorder(v);
            }
        });
        mETOTP2 = view.findViewById(R.id.etOPT2);
        mETOTP2.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                changeEditTextBorder(v);
            }
        });
        mETOTP3 = view.findViewById(R.id.etOPT3);
        mETOTP3.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                changeEditTextBorder(v);
            }
        });
        mETOTP4 = view.findViewById(R.id.etOPT4);
        mETOTP4.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                changeEditTextBorder(v);
            }
        });
        mConfirmButton = view.findViewById(R.id.btnConfirmOTP);
        mContactSupportButton = view.findViewById(R.id.btnContactSupport);
        mConfirmButton.setOnClickListener(this::onClick);
        mContactSupportButton.setOnClickListener(this::onClick);
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

    private void changeEditTextBorder(View view) {
        if (view.hasFocus()) {
            view.setBackgroundResource(R.drawable.bg_rounded_highlighted_edittext);
        } else {
            view.setBackgroundResource(R.drawable.bg_rounded_edittext);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.etOPT1:
                mETOTP1.setBackgroundResource(0);
                mETOTP1.setBackgroundResource(R.drawable.bg_rounded_highlighted_edittext);
                break;
            case R.id.etOPT2:
                mETOTP2.setBackground(getResources().getDrawable(R.drawable.bg_rounded_highlighted_edittext));
                break;
            case R.id.etOPT3:
                mETOTP3.setBackground(getResources().getDrawable(R.drawable.bg_rounded_highlighted_edittext));
                break;
            case R.id.etOPT4:
                mETOTP4.setBackground(getResources().getDrawable(R.drawable.bg_rounded_highlighted_edittext));
                break;
            case R.id.btnConfirmOTP:
                boolean isValidOTP = userClient.sendOTPDetails(mETOTP1.getText().toString(), mETOTP2.getText().toString(),
                        mETOTP3.getText().toString(), mETOTP4.getText().toString());
                Toast.makeText(getContext(), "Confirm OTP" + isValidOTP, Toast.LENGTH_LONG).show();
                replaceInputContainerListener.onReplaceInputContainer(MapInputContainerEnum.DriverOnTripFragment);
                break;
            case R.id.btnContactSupport:
                Toast.makeText(getContext(), "Contact Support", Toast.LENGTH_LONG).show();
                break;
        }
    }
}
