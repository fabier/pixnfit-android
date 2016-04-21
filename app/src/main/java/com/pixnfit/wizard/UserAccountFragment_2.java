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
public class UserAccountFragment_2 extends Fragment implements RadioGroup.OnCheckedChangeListener, CalendarView.OnDateChangeListener {
    private static final String ARG_KEY = "key";

    private PageFragmentCallbacks mCallbacks;
    private String mKey;
    private UserAccountPage_2 mPage;
    //    private TextView mBirthdateText;
    private DatePicker mBirthdateDatePicker;
    //    private CalendarView mBirthdateCalendarView;
    private RadioGroup mGenreRadioGroup;
    private RadioButton mGenreMaleRadioButton;
    private RadioButton mGenreFemaleRadioButton;

    public static UserAccountFragment_2 create(String key) {
        Bundle args = new Bundle();
        args.putString(ARG_KEY, key);

        UserAccountFragment_2 fragment = new UserAccountFragment_2();
        fragment.setArguments(args);
        return fragment;
    }

    public UserAccountFragment_2() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle args = getArguments();
        mKey = args.getString(ARG_KEY);
        mPage = (UserAccountPage_2) mCallbacks.onGetPage(mKey);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_wizardpage_user_account_2, container, false);
        ((TextView) rootView.findViewById(android.R.id.title)).setText(mPage.getTitle());

        mGenreRadioGroup = ((RadioGroup) rootView.findViewById(R.id.genreRadioGroup));
        mGenreMaleRadioButton = ((RadioButton) rootView.findViewById(R.id.genreMaleRadioButton));
        mGenreFemaleRadioButton = ((RadioButton) rootView.findViewById(R.id.genreFemaleRadioButton));

        String genre = mPage.getData().getString(UserAccountPage_2.GENRE_DATA_KEY);
        if ("male".equals(genre)) {
            mGenreMaleRadioButton.setChecked(true);
            mGenreFemaleRadioButton.setChecked(false);
        } else if ("female".equals(genre)) {
            mGenreMaleRadioButton.setChecked(false);
            mGenreFemaleRadioButton.setChecked(true);
        } else {
            mGenreMaleRadioButton.setChecked(false);
            mGenreFemaleRadioButton.setChecked(false);
        }

        mBirthdateDatePicker = ((DatePicker) rootView.findViewById(R.id.birthdateDatePicker));
        String sDate = mPage.getData().getString(UserAccountPage_2.BIRTHDATE_DATA_KEY);
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

        mGenreRadioGroup.setOnCheckedChangeListener(this);
        mBirthdateDatePicker.getCalendarView().setOnDateChangeListener(this);
//        mBirthdateDatePicker.setOnClickListener(this);
    }

    /*@Override
    public void setMenuVisibility(boolean menuVisible) {
        super.setMenuVisibility(menuVisible);

        // In a future update to the support library, this should override setUserVisibleHint
        // instead of setMenuVisibility.
        if (mNameView != null) {
            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            if (!menuVisible) {
                imm.hideSoftInputFromWindow(getView().getWindowToken(), 0);
            }
        }
    }*/

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId) {
            case R.id.genreMaleRadioButton:
                mPage.getData().putString(UserAccountPage_2.GENRE_DATA_KEY, "male");
                mPage.notifyDataChanged();
                break;
            case R.id.genreFemaleRadioButton:
                mPage.getData().putString(UserAccountPage_2.GENRE_DATA_KEY, "female");
                mPage.notifyDataChanged();
                break;
            default:
                break;
        }
    }

    @Override
    public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {
        String sDate = new SimpleDateFormat("yyyy/MM/dd").format(new Date(view.getDate()));
        mPage.getData().putString(UserAccountPage_2.BIRTHDATE_DATA_KEY, sDate);
        mPage.notifyDataChanged();
    }

    /*
        public void showDatePicker(View v) {
            FragmentManager fragmentManager = getFragmentManager();
            DatePickerFragment datePickerFragment = (DatePickerFragment) fragmentManager.findFragmentByTag("datePicker");
            if (datePickerFragment == null) {
                datePickerFragment = new DatePickerFragment() {
                    @Override
                    public void onDateSet(Date date) {
                        String sDate = new SimpleDateFormat("yyyy/MM/dd").format(date);
                        mBirthdateText.setText(sDate);
                        mPage.getData().putString(UserAccountPage_2.BIRTHDATE_DATA_KEY, sDate);
                        mPage.notifyDataChanged();
                    }
                };
            }
            String sDate = (String) mBirthdateText.getText();
            if (sDate != null) {
                try {
                    datePickerFragment.setDate(new SimpleDateFormat("yyyy/MM/dd").parse(sDate));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
            datePickerFragment.show(fragmentManager, "datePicker");
        }

        @Override
        public void onClick(View v) {
            if (v.equals(mBirthdateText)) {
                showDatePicker(v);
            }
        }
    */
//    public static abstract class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {
//
//        private Date date;
//
//        DatePickerFragment() {
//        }
//
//        @Override
//        public Dialog onCreateDialog(Bundle savedInstanceState) {
//             Use the current date as the default date in the picker
////            final Calendar calendar = Calendar.getInstance();
//            if (this.date != null) {
//                calendar.setTime(this.date);
//            }
//            int year = calendar.get(Calendar.YEAR);
//            int month = calendar.get(Calendar.MONTH);
//            int day = calendar.get(Calendar.DAY_OF_MONTH);
////             Create a new instance of DatePickerDialog and return it
//            return new DatePickerDialog(getActivity(), this, year, month, day);
//        }
//
//        public void onDateSet(DatePicker view, int year, int month, int day) {
//            Calendar calendar = Calendar.getInstance();
//            calendar.set(Calendar.YEAR, year);
//            calendar.set(Calendar.MONTH, month);
//            calendar.set(Calendar.DAY_OF_MONTH, day);
//            onDateSet(calendar.getTime());
//        }
//
//        public abstract void onDateSet(Date date);
//
//        public void setDate(Date date) {
//            this.date = date;
//        }
//    }
}
