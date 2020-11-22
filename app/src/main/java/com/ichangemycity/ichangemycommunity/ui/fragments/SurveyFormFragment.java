package com.ichangemycity.ichangemycommunity.ui.fragments;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.ichangemycity.ichangemycommunity.R;
import com.ichangemycity.ichangemycommunity.ui.listeners.ReplaceInputContainerListener;
import com.ichangemycity.ichangemycommunity.utils.map.MapInputContainerEnum;
import com.ichangemycity.ichangemycommunity.utils.map.UserClient;

public class SurveyFormFragment extends Fragment implements View.OnClickListener {

    private static final String TAG = SurveyFormFragment.class.getSimpleName();

    private UserClient userClient;
    private ReplaceInputContainerListener replaceInputContainerListener;
    private Button mTakeSurveyBtn;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        userClient = (UserClient) getContext().getApplicationContext();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_survey_form, null, true);
        mTakeSurveyBtn = view.findViewById(R.id.takeSurvey);
        mTakeSurveyBtn.setOnClickListener(this::onClick);
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
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.takeSurvey:
                userClient.setMapInputContainerEnum(MapInputContainerEnum.SurveyCategoryFragment);
                replaceInputContainerListener.onReplaceInputContainer();
                break;
        }
    }
}
