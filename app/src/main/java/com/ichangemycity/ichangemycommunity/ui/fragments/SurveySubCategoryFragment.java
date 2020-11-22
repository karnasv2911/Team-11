package com.ichangemycity.ichangemycommunity.ui.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.ichangemycity.ichangemycommunity.R;
import com.ichangemycity.ichangemycommunity.ui.listeners.ReplaceInputContainerListener;
import com.ichangemycity.ichangemycommunity.utils.map.MapInputContainerEnum;
import com.ichangemycity.ichangemycommunity.utils.map.UserClient;

public class SurveySubCategoryFragment extends Fragment implements View.OnClickListener {

    private static final String TAG = SurveySubCategoryFragment.class.getSimpleName();

    private UserClient userClient;
    private ReplaceInputContainerListener replaceInputContainerListener;

    private Button mSubmitBtn;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        userClient = (UserClient) getContext().getApplicationContext();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_survey_subcategory, null, false);
        mSubmitBtn = view.findViewById(R.id.submit);
        mSubmitBtn.setOnClickListener(this::onClick);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        replaceInputContainerListener = (ReplaceInputContainerListener) context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        replaceInputContainerListener = null;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.submit:
                showAlert();
                break;
        }

    }

    private void showAlert() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        LayoutInflater factory = LayoutInflater.from(getContext());
        View view = factory.inflate(R.layout.layout_submit_survey_confirmation, null);
        Button mOkBtn = view.findViewById(R.id.ok);

        builder.setView(view)
                .setCancelable(true);
        AlertDialog alert = builder.create();
        mOkBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alert.dismiss();
                userClient.setMapInputContainerEnum(MapInputContainerEnum.SurveyFormFragment);
                replaceInputContainerListener.onReplaceInputContainer();
            }
        });
        alert.show();
    }
}
