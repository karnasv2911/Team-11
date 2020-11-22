package com.ichangemycity.ichangemycommunity.ui.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.ichangemycity.ichangemycommunity.R;
import com.ichangemycity.ichangemycommunity.utils.FragmentUtils;
import com.ichangemycity.ichangemycommunity.utils.map.MapInputContainerEnum;
import com.ichangemycity.ichangemycommunity.utils.map.UserClient;

import java.io.IOException;

public class SurveyHomeFragment extends Fragment {

    private static final String TAG = SurveyHomeFragment.class.getSimpleName();

    private FragmentUtils fragmentUtils = new FragmentUtils();
    private MapInputContainerEnum mapInputContainerEnum;
    private UserClient userClient;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        userClient = (UserClient) getContext().getApplicationContext();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_survey_home, container, false);
        renderInputViewContainer();
        return view;
    }

    private void renderInputViewContainer() {
        mapInputContainerEnum = userClient.getMapInputContainerEnum();
        switch (mapInputContainerEnum) {
            case LoaderFragment:
                fragmentUtils.replaceFragment(R.id.surveyHomeContainer, TAG, getFragmentManager(), new LoaderFragment());
                break;
            case SurveyCategoryFragment:
                fragmentUtils.replaceFragment(R.id.surveyHomeContainer, TAG, getFragmentManager(), new SurveyCategoryFragment());
                break;
            case SurveySubCategoryFragment:
                fragmentUtils.replaceFragment(R.id.surveyHomeContainer, TAG, getFragmentManager(), new SurveySubCategoryFragment());
                break;
            case SurveyFormFragment:
                fragmentUtils.replaceFragment(R.id.map_view_container, TAG, getFragmentManager(), new MapViewFragment());
                fragmentUtils.replaceFragment(R.id.input_view_container, TAG, getFragmentManager(), new SurveyFormFragment());
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
