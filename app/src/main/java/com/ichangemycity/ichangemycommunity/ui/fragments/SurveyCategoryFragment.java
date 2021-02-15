package com.ichangemycity.ichangemycommunity.ui.fragments;
import android.content.Context;
import android.os.Bundle;
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
import com.ichangemycity.ichangemycommunity.utils.map.SurveyDropDownEnum;
import com.ichangemycity.ichangemycommunity.utils.map.UserClient;
public class SurveyCategoryFragment extends Fragment implements View.OnClickListener {
    private static final String TAG = SurveyCategoryFragment.class.getSimpleName();
    private UserClient userClient;
    private ReplaceInputContainerListener replaceInputContainerListener;
    private Button walkabilityButton;
    private Button visibilityButton;
    private Button drivabilityButton;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        userClient = (UserClient) getContext().getApplicationContext();
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_survey_category, null, false);
        walkabilityButton = view.findViewById(R.id.walkability);
        visibilityButton = view.findViewById(R.id.visibility);
        drivabilityButton = view.findViewById(R.id.drivability);
        walkabilityButton.setOnClickListener(this::onClick);
        visibilityButton.setOnClickListener(this::onClick);
        drivabilityButton.setOnClickListener(this::onClick);
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
            case R.id.walkability:
                userClient.setSurveyCategory(SurveyDropDownEnum.Walkability);
                userClient.setMapInputContainerEnum(MapInputContainerEnum.WalkabilityFragment);
                replaceInputContainerListener.onReplaceInputContainer();
                break;
            case R.id.drivability:
                userClient.setSurveyCategory(SurveyDropDownEnum.Drivability);
                userClient.setMapInputContainerEnum(MapInputContainerEnum.DrivabilityFragment);
                replaceInputContainerListener.onReplaceInputContainer();
                break;
            case R.id.visibility:
                userClient.setSurveyCategory(SurveyDropDownEnum.Visibility);
                userClient.setMapInputContainerEnum(MapInputContainerEnum.VisibilityFragment);
                replaceInputContainerListener.onReplaceInputContainer();
                break;
        }
    }
}