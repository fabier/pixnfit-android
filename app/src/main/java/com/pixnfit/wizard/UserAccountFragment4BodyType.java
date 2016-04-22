package com.pixnfit.wizard;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.pixnfit.R;
import com.tech.freak.wizardpager.ui.PageFragmentCallbacks;

/**
 * Created by TechFreak on 04/09/2014.
 */
public class UserAccountFragment4BodyType extends Fragment implements RadioGroup.OnCheckedChangeListener {
    private static final String ARG_KEY = "key";
    private static final String ARG_TYPE = "type";

    private PageFragmentCallbacks mCallbacks;
    private String mKey;
    private String mType;
    private UserAccountPage4BodyType mPage;
    private RadioGroup mBodyTypeRadioGroup;
    private View rootView;

    public static UserAccountFragment4BodyType create(String key, String type) {
        Bundle args = new Bundle();
        args.putString(ARG_KEY, key);
        args.putString(ARG_TYPE, type);

        UserAccountFragment4BodyType fragment = new UserAccountFragment4BodyType();
        fragment.setArguments(args);
        return fragment;
    }

    public UserAccountFragment4BodyType() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle args = getArguments();
        mKey = args.getString(ARG_KEY);
        mType = args.getString(ARG_TYPE);
        mPage = (UserAccountPage4BodyType) mCallbacks.onGetPage(mKey);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.rootView = inflater.inflate(R.layout.fragment_wizardpage_user_account_4_bodytype, container, false);
        ((TextView) rootView.findViewById(android.R.id.title)).setText(mPage.getTitle());

        mBodyTypeRadioGroup = ((RadioGroup) rootView.findViewById(R.id.bodyTypeRadioGroup));
        mBodyTypeRadioGroup.clearCheck();

        int bodytypeId = mPage.getData().getInt(UserAccountPage4BodyType.BODYTYPE_DATA_KEY);
        switch (bodytypeId) {
            case 1:
                mBodyTypeRadioGroup.check(R.id.bodytype_1_radioButton);
                break;
            case 2:
                mBodyTypeRadioGroup.check(R.id.bodytype_2_radioButton);
                break;
            case 3:
                mBodyTypeRadioGroup.check(R.id.bodytype_3_radioButton);
                break;
            case 4:
                mBodyTypeRadioGroup.check(R.id.bodytype_4_radioButton);
                break;
            case 5:
                mBodyTypeRadioGroup.check(R.id.bodytype_5_radioButton);
                break;
            case 6:
                mBodyTypeRadioGroup.check(R.id.bodytype_6_radioButton);
                break;
            default:
                break;
        }

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

        mType = getArguments().getString(ARG_TYPE);

        if (UserAccountPage4BodyType.TYPE_MALE.equals(mType)) {
            ((RadioButton) rootView.findViewById(R.id.bodytype_1_radioButton)).setCompoundDrawablesWithIntrinsicBounds(R.drawable.bodytype_male_1, 0, 0, 0);
            ((RadioButton) rootView.findViewById(R.id.bodytype_2_radioButton)).setCompoundDrawablesWithIntrinsicBounds(R.drawable.bodytype_male_2, 0, 0, 0);
            ((RadioButton) rootView.findViewById(R.id.bodytype_3_radioButton)).setCompoundDrawablesWithIntrinsicBounds(R.drawable.bodytype_male_3, 0, 0, 0);
            ((RadioButton) rootView.findViewById(R.id.bodytype_4_radioButton)).setCompoundDrawablesWithIntrinsicBounds(R.drawable.bodytype_male_4, 0, 0, 0);
            ((RadioButton) rootView.findViewById(R.id.bodytype_5_radioButton)).setCompoundDrawablesWithIntrinsicBounds(R.drawable.bodytype_male_5, 0, 0, 0);
            ((RadioButton) rootView.findViewById(R.id.bodytype_6_radioButton)).setCompoundDrawablesWithIntrinsicBounds(R.drawable.bodytype_male_6, 0, 0, 0);
        } else if (UserAccountPage4BodyType.TYPE_FEMALE.equals(mType)) {
            ((RadioButton) rootView.findViewById(R.id.bodytype_1_radioButton)).setCompoundDrawablesWithIntrinsicBounds(R.drawable.bodytype_female_1, 0, 0, 0);
            ((RadioButton) rootView.findViewById(R.id.bodytype_2_radioButton)).setCompoundDrawablesWithIntrinsicBounds(R.drawable.bodytype_female_2, 0, 0, 0);
            ((RadioButton) rootView.findViewById(R.id.bodytype_3_radioButton)).setCompoundDrawablesWithIntrinsicBounds(R.drawable.bodytype_female_3, 0, 0, 0);
            ((RadioButton) rootView.findViewById(R.id.bodytype_4_radioButton)).setCompoundDrawablesWithIntrinsicBounds(R.drawable.bodytype_female_4, 0, 0, 0);
            ((RadioButton) rootView.findViewById(R.id.bodytype_5_radioButton)).setCompoundDrawablesWithIntrinsicBounds(R.drawable.bodytype_female_5, 0, 0, 0);
            ((RadioButton) rootView.findViewById(R.id.bodytype_6_radioButton)).setCompoundDrawablesWithIntrinsicBounds(R.drawable.bodytype_female_6, 0, 0, 0);
        }

        mBodyTypeRadioGroup.setOnCheckedChangeListener(this);
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(ARG_TYPE, mType);
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        int bodytypeId = -1;
        switch (checkedId) {
            case R.id.bodytype_1_radioButton:
                bodytypeId = 1;
                break;
            case R.id.bodytype_2_radioButton:
                bodytypeId = 2;
                break;
            case R.id.bodytype_3_radioButton:
                bodytypeId = 3;
                break;
            case R.id.bodytype_4_radioButton:
                bodytypeId = 4;
                break;
            case R.id.bodytype_5_radioButton:
                bodytypeId = 5;
                break;
            case R.id.bodytype_6_radioButton:
                bodytypeId = 6;
                break;
            default:
                break;
        }
        mPage.getData().putInt(UserAccountPage4BodyType.BODYTYPE_DATA_KEY, bodytypeId);
        mPage.notifyDataChanged();
    }
}
