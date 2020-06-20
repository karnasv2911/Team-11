package com.kickstart.woc.wocdriverapp.ui.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.kickstart.woc.wocdriverapp.R;
import com.kickstart.woc.wocdriverapp.model.User;
import com.kickstart.woc.wocdriverapp.ui.listeners.ReplaceInputContainerListener;
import com.kickstart.woc.wocdriverapp.utils.FragmentUtils;
import com.kickstart.woc.wocdriverapp.utils.map.MapInputContainerEnum;
import com.kickstart.woc.wocdriverapp.utils.map.UserClient;

import java.util.Map;

public class DriverTripSummaryFragment extends Fragment implements View.OnClickListener {

    private static final String TAG = DriverTripSummaryFragment.class.getSimpleName();

    private User driver;
    private UserClient userClient = new UserClient();
    private FragmentUtils fragmentUtils = new FragmentUtils();
    private ReplaceInputContainerListener replaceInputContainerListener;

    private Button mContactSupportButton;
    private TextView mSourceTV;
    private TextView mDestinationTV;
    private TextView mDistanceTV;
    private TextView mTimeTV;
    private TextView mAmountTV;
    private Button mRateRider;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        driver = userClient.getDriverDetails();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_driver_trip_summary, null, false);
        mContactSupportButton = view.findViewById(R.id.btnContactSupport);
        mContactSupportButton.setOnClickListener(this::onClick);
        mRateRider = view.findViewById(R.id.rateRider);
        mRateRider.setOnClickListener(this::onClick);
        mSourceTV = view.findViewById(R.id.sourceTV);
        mDestinationTV = view.findViewById(R.id.destinationTV);
        mDistanceTV = view.findViewById(R.id.distanceTV);
        mTimeTV = view.findViewById(R.id.timeTV);
        mAmountTV = view.findViewById(R.id.amountTV);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        populateTripSummary();
    }

    private void populateTripSummary() {
        Map<String, String> map = userClient.getTripSummary();
        mSourceTV.setText(map.get("source"));
        mDestinationTV.setText(map.get("destination"));
        mDistanceTV.setText(map.get("distance"));
        mTimeTV.setText(map.get("time"));
        mAmountTV.setText(getString(R.string.Rs) + map.get("amount"));
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
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnContactSupport:
                Toast.makeText(getContext(), "Call Contact Support", Toast.LENGTH_LONG).show();
                break;
            case R.id.rateRider:
                showRateTripAlert();
                Toast.makeText(getContext(), "Call Contact Support", Toast.LENGTH_LONG).show();
                break;
        }

    }

    private void showRateTripAlert() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        LayoutInflater factory = LayoutInflater.from(getContext());
        View view = factory.inflate(R.layout.layout_rate_trip, null);
        Button mDone = view.findViewById(R.id.done);
        EditText mComments = view.findViewById(R.id.commentsET);
        RatingBar ratingBar = view.findViewById(R.id.rateRider);

        builder.setView(view)
                .setCancelable(true);
        AlertDialog alert = builder.create();
        mDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String rating = String.valueOf(ratingBar.getRating());
                String comments = mComments.getText().toString();
                userClient.rateTrip(rating, comments);
                alert.dismiss();
                Toast.makeText(getContext(), "Rating: " + rating + " Comments: " + comments , Toast.LENGTH_LONG).show();
                replaceInputContainerListener.onReplaceInputContainer(MapInputContainerEnum.DriverAvailabilityFragment);
            }
        });
        alert.show();
    }
}
