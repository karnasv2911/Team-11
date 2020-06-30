package com.kickstart.woc.wocdriverapp.ui.fragments;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.kickstart.woc.wocdriverapp.R;
import com.kickstart.woc.wocdriverapp.ui.listeners.PhoneCallListener;
import com.kickstart.woc.wocdriverapp.ui.listeners.ReplaceInputContainerListener;
import com.kickstart.woc.wocdriverapp.utils.WocConstants;
import com.kickstart.woc.wocdriverapp.utils.map.MapInputContainerEnum;
import com.kickstart.woc.wocdriverapp.utils.map.UserClient;


public class DriverEnterRiderPinFragment extends Fragment implements View.OnClickListener {

    private static final String TAG = DriverEnterRiderPinFragment.class.getSimpleName();

    private UserClient userClient;
    private ReplaceInputContainerListener replaceInputContainerListener;
    private PhoneCallListener phoneCallListener;

    private EditText mETPin1;
    private EditText mETPin2;
    private EditText mETPin3;
    private EditText mETPin4;
    private Button mConfirmButton;
    private Button mContactSupportButton;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        userClient = (UserClient) getContext().getApplicationContext();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_driver_enter_rider_pin, container, false);
        mETPin1 = view.findViewById(R.id.etOPT1);
        mETPin1.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                changeEditTextBorder(v);
            }
        });
        mETPin2 = view.findViewById(R.id.etOPT2);
        mETPin2.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                changeEditTextBorder(v);
            }
        });
        mETPin3 = view.findViewById(R.id.etOPT3);
        mETPin3.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                changeEditTextBorder(v);
            }
        });
        mETPin4 = view.findViewById(R.id.etOPT4);
        mETPin4.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                changeEditTextBorder(v);
            }
        });
        mConfirmButton = view.findViewById(R.id.btnConfirmPin);
        mContactSupportButton = view.findViewById(R.id.btnContactSupport);
        mConfirmButton.setOnClickListener(this::onClick);
        mContactSupportButton.setOnClickListener(this::onClick);
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
                mETPin1.setBackgroundResource(0);
                mETPin1.setBackgroundResource(R.drawable.bg_rounded_highlighted_edittext);
                break;
            case R.id.etOPT2:
                mETPin2.setBackground(getResources().getDrawable(R.drawable.bg_rounded_highlighted_edittext));
                break;
            case R.id.etOPT3:
                mETPin3.setBackground(getResources().getDrawable(R.drawable.bg_rounded_highlighted_edittext));
                break;
            case R.id.etOPT4:
                mETPin4.setBackground(getResources().getDrawable(R.drawable.bg_rounded_highlighted_edittext));
                break;
            case R.id.btnConfirmPin:
                boolean isValidPin = userClient.isValidPin(mETPin1.getText().toString(), mETPin2.getText().toString(),
                        mETPin3.getText().toString(), mETPin4.getText().toString());
                if (!isValidPin) {
                    showErrorAlert("Please enter a valid pin.");
                } else {
                    userClient.setMapInputContainerEnum(MapInputContainerEnum.DriverOnTripFragment);
                    replaceInputContainerListener.onReplaceInputContainer(MapInputContainerEnum.DriverOnTripFragment);
                }
                break;
            case R.id.btnContactSupport:
                phoneCallListener.onMakePhoneCall(MapInputContainerEnum.DriverEnterRiderPinFragment, WocConstants.CONTACT_SUPPORT);
                break;
        }
    }

    private void showErrorAlert(String errorMessage) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this.getContext());
        LayoutInflater factory = LayoutInflater.from(getContext());
        final View view = factory.inflate(R.layout.layout_warning, null);
        TextView mErrorText = view.findViewById(R.id.errorMessage);
        if (errorMessage == null || errorMessage.length() == 0) {
            mErrorText.setText(WocConstants.ERROR_MESSAGE);
        } else {
            mErrorText.setText(errorMessage);
        }
        builder.setView(view);
        builder.setCancelable(true)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, final int id) {
                        dialog.dismiss();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }
}
