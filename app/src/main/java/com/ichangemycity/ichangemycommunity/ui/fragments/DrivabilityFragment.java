package com.ichangemycity.ichangemycommunity.ui.fragments;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import com.ichangemycity.ichangemycommunity.R;
import com.ichangemycity.ichangemycommunity.ui.listeners.ReplaceInputContainerListener;
import com.ichangemycity.ichangemycommunity.utils.map.MapInputContainerEnum;
import com.ichangemycity.ichangemycommunity.utils.map.UserClient;

public class DrivabilityFragment extends Fragment implements View.OnClickListener {
    private static final String TAG = DrivabilityFragment.class.getSimpleName();
    private UserClient userClient;
    private ReplaceInputContainerListener replaceInputContainerListener;
    private RadioGroup mRadioGroup;
    private String subcategorySelection = null;
    private Button mSubmitBtn;
    private TextView mQualityScore;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        userClient = (UserClient) getContext().getApplicationContext();
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_drivability, null, false);
        mRadioGroup = view.findViewById(R.id.radio_group);
        mRadioGroup.setOnCheckedChangeListener((group, checkedId) -> {
            RadioButton rb = view.findViewById(checkedId);
            subcategorySelection = rb.getText().toString();
        });
        mSubmitBtn = view.findViewById(R.id.submit);
        mSubmitBtn.setOnClickListener(this::onClick);
        mQualityScore = view.findViewById(R.id.quality_score);
        SeekBar seekBar = view.findViewById(R.id.seekBar);
        seekBar.setOnSeekBarChangeListener(seekBarChangeListener);
        return view;
    }
    SeekBar.OnSeekBarChangeListener seekBarChangeListener = new SeekBar.OnSeekBarChangeListener() {
        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            mQualityScore.setText("Quality Score: " + progress + "%");
        }
        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {
            // called when the user first touches the SeekBar
        }
        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
            // called after the user finishes moving the SeekBar
        }
    };
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
                userClient.submitSurvey(subcategorySelection, null, null, (String) mQualityScore.getText());
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