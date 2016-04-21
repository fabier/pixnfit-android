package com.pixnfit.wizard;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.NumberPicker;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.pixnfit.R;
import com.tech.freak.wizardpager.ui.PageFragmentCallbacks;

/**
 * Created by TechFreak on 04/09/2014.
 */
public class UserAccountFragment5VisibilityHeightWeight extends Fragment implements NumberPicker.OnValueChangeListener, RadioGroup.OnCheckedChangeListener {
    private static final String ARG_KEY = "key";

    private PageFragmentCallbacks mCallbacks;
    private String mKey;
    private UserAccountPage5VisibilityHeightWeight mPage;
    private RadioGroup mVisibilityRadioGroup;
    private NumberPicker mHeightSpinner;
    private NumberPicker mWeightSpinner;

    public static UserAccountFragment5VisibilityHeightWeight create(String key) {
        Bundle args = new Bundle();
        args.putString(ARG_KEY, key);

        UserAccountFragment5VisibilityHeightWeight fragment = new UserAccountFragment5VisibilityHeightWeight();
        fragment.setArguments(args);
        return fragment;
    }

    public UserAccountFragment5VisibilityHeightWeight() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle args = getArguments();
        mKey = args.getString(ARG_KEY);
        mPage = (UserAccountPage5VisibilityHeightWeight) mCallbacks.onGetPage(mKey);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_wizardpage_user_account_5_visibility_height_weight, container, false);
        ((TextView) rootView.findViewById(android.R.id.title)).setText(mPage.getTitle());

        mVisibilityRadioGroup = ((RadioGroup) rootView.findViewById(R.id.visibilityRadioGroup));
        int visibilityId = mPage.getData().getInt(UserAccountPage5VisibilityHeightWeight.VISIBILITY_DATA_KEY);
        switch (visibilityId) {
            case 1:
                mVisibilityRadioGroup.check(R.id.visibilityPublicRadioButton);
                break;
            case 2:
                mVisibilityRadioGroup.check(R.id.visibilityFollowersRadioButton);
                break;
            case 3:
                mVisibilityRadioGroup.check(R.id.visibilityPrivateRadioButton);
                break;
            default:
                break;
        }

        mHeightSpinner = ((NumberPicker) rootView.findViewById(R.id.heightSpinner));
        mHeightSpinner.setMinValue(50);
        mHeightSpinner.setMaxValue(250);
        int height = mPage.getData().getInt(UserAccountPage5VisibilityHeightWeight.HEIGHT_DATA_KEY, 170);
        mHeightSpinner.setValue(height);

        mWeightSpinner = ((NumberPicker) rootView.findViewById(R.id.weightSpinner));
        mWeightSpinner.setMinValue(40);
        mWeightSpinner.setMaxValue(200);
        int weight = mPage.getData().getInt(UserAccountPage5VisibilityHeightWeight.WEIGHT_DATA_KEY, 70);
        mWeightSpinner.setValue(weight);

        return rootView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        if (!(activity instanceof PageFragmentCallbacks)) {
            throw new ClassCastException("Activity must implement PageFragmentCallbacks");
        }

        mCallbacks = (PageFragmentCallbacks) activity;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCallbacks = null;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mVisibilityRadioGroup.setOnCheckedChangeListener(this);
        mHeightSpinner.setOnValueChangedListener(this);
        mWeightSpinner.setOnValueChangedListener(this);
    }

    @Override
    public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
        switch (picker.getId()) {
            case R.id.heightSpinner:
                mPage.getData().putInt(UserAccountPage5VisibilityHeightWeight.HEIGHT_DATA_KEY, newVal);
                mPage.notifyDataChanged();
                break;
            case R.id.weightSpinner:
                mPage.getData().putInt(UserAccountPage5VisibilityHeightWeight.WEIGHT_DATA_KEY, newVal);
                mPage.notifyDataChanged();
                break;
            default:
                break;
        }
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        int visibilityId = -1;
        switch (checkedId) {
            case R.id.visibilityPublicRadioButton:
                visibilityId = 1;
                break;
            case R.id.visibilityFollowersRadioButton:
                visibilityId = 2;
                break;
            case R.id.visibilityPrivateRadioButton:
                visibilityId = 3;
                break;
            default:
                break;
        }
        mPage.getData().putInt(UserAccountPage5VisibilityHeightWeight.VISIBILITY_DATA_KEY, visibilityId);
        mPage.notifyDataChanged();
    }
}
