package com.pixnfit.wizard;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.DatePicker;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.pixnfit.R;
import com.tech.freak.wizardpager.ui.PageFragmentCallbacks;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by TechFreak on 04/09/2014.
 */
public class UserAccountFragment2SexBirthdate extends Fragment implements RadioGroup.OnCheckedChangeListener, CalendarView.OnDateChangeListener {
    private static final String ARG_KEY = "key";

    private PageFragmentCallbacks mCallbacks;
    private String mKey;
    private UserAccountPage2SexBirthdate mPage;
    private DatePicker mBirthdateDatePicker;
    private RadioGroup mGenderRadioGroup;
    private RadioButton mGenderMaleRadioButton;
    private RadioButton mGenderFemaleRadioButton;

    public static UserAccountFragment2SexBirthdate create(String key) {
        Bundle args = new Bundle();
        args.putString(ARG_KEY, key);

        UserAccountFragment2SexBirthdate fragment = new UserAccountFragment2SexBirthdate();
        fragment.setArguments(args);
        return fragment;
    }

    public UserAccountFragment2SexBirthdate() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle args = getArguments();
        mKey = args.getString(ARG_KEY);
        mPage = (UserAccountPage2SexBirthdate) mCallbacks.onGetPage(mKey);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_wizardpage_user_account_2_sex_birthdate, container, false);
        ((TextView) rootView.findViewById(android.R.id.title)).setText(mPage.getTitle());

        mGenderRadioGroup = ((RadioGroup) rootView.findViewById(R.id.genderRadioGroup));
        mGenderMaleRadioButton = ((RadioButton) rootView.findViewById(R.id.genderMaleRadioButton));
        mGenderFemaleRadioButton = ((RadioButton) rootView.findViewById(R.id.genderFemaleRadioButton));

        String gender = mPage.getData().getString(UserAccountPage2SexBirthdate.GENDER_DATA_KEY);
        if ("male".equals(gender)) {
            mGenderMaleRadioButton.setChecked(true);
            mGenderFemaleRadioButton.setChecked(false);
        } else if ("female".equals(gender)) {
            mGenderMaleRadioButton.setChecked(false);
            mGenderFemaleRadioButton.setChecked(true);
        } else {
            mGenderMaleRadioButton.setChecked(false);
            mGenderFemaleRadioButton.setChecked(false);
        }

        mBirthdateDatePicker = ((DatePicker) rootView.findViewById(R.id.birthdateDatePicker));
        String sDate = mPage.getData().getString(UserAccountPage2SexBirthdate.BIRTHDATE_DATA_KEY);
        if (sDate != null) {
            try {
                Calendar calendar = Calendar.getInstance();
                Date date = new SimpleDateFormat("yyyy/MM/dd").parse(sDate);
                calendar.setTime(date);
                mBirthdateDatePicker.updateDate(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        } else {
            mBirthdateDatePicker.updateDate(2000, 0, 1);
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

        mGenderRadioGroup.setOnCheckedChangeListener(this);
        mBirthdateDatePicker.getCalendarView().setOnDateChangeListener(this);
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId) {
            case R.id.genderMaleRadioButton:
                mPage.getData().putString(UserAccountPage2SexBirthdate.GENDER_DATA_KEY, "male");
                mPage.notifyDataChanged();
                break;
            case R.id.genderFemaleRadioButton:
                mPage.getData().putString(UserAccountPage2SexBirthdate.GENDER_DATA_KEY, "female");
                mPage.notifyDataChanged();
                break;
            default:
                break;
        }
    }

    @Override
    public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {
        String sDate = new SimpleDateFormat("yyyy/MM/dd").format(new Date(view.getDate()));
        mPage.getData().putString(UserAccountPage2SexBirthdate.BIRTHDATE_DATA_KEY, sDate);
        mPage.notifyDataChanged();
    }
}
