package com.pixnfit.wizard;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.DatePicker;
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
public class UserAccountFragment2Birthdate extends Fragment implements CalendarView.OnDateChangeListener {
    private static final String ARG_KEY = "key";

    private PageFragmentCallbacks mCallbacks;
    private String mKey;
    private UserAccountPage2Birthdate mPage;
    private DatePicker mBirthdateDatePicker;

    public static UserAccountFragment2Birthdate create(String key) {
        Bundle args = new Bundle();
        args.putString(ARG_KEY, key);

        UserAccountFragment2Birthdate fragment = new UserAccountFragment2Birthdate();
        fragment.setArguments(args);
        return fragment;
    }

    public UserAccountFragment2Birthdate() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle args = getArguments();
        mKey = args.getString(ARG_KEY);
        mPage = (UserAccountPage2Birthdate) mCallbacks.onGetPage(mKey);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_wizardpage_user_account_2_birthdate, container, false);
        ((TextView) rootView.findViewById(android.R.id.title)).setText(mPage.getTitle());

        mBirthdateDatePicker = ((DatePicker) rootView.findViewById(R.id.birthdateDatePicker));
        mBirthdateDatePicker.setMaxDate(System.currentTimeMillis());
        String sDate = mPage.getData().getString(UserAccountPage2Birthdate.BIRTHDATE_DATA_KEY);
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
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.YEAR, -20);
            mBirthdateDatePicker.updateDate(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
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
        mBirthdateDatePicker.getCalendarView().setOnDateChangeListener(this);
    }

    @Override
    public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {
        String sDate = new SimpleDateFormat("yyyy/MM/dd").format(new Date(view.getDate()));
        mPage.getData().putString(UserAccountPage2Birthdate.BIRTHDATE_DATA_KEY, sDate);
        mPage.notifyDataChanged();
    }
}
