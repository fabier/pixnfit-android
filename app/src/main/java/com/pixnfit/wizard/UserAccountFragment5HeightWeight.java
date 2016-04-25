package com.pixnfit.wizard;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.NumberPicker;
import android.widget.TextView;

import com.pixnfit.R;
import com.tech.freak.wizardpager.ui.PageFragmentCallbacks;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Locale;

/**
 * Created by TechFreak on 04/09/2014.
 */
public class UserAccountFragment5HeightWeight extends Fragment implements NumberPicker.OnValueChangeListener {
    private static final String ARG_KEY = "key";

    private PageFragmentCallbacks mCallbacks;
    private String mKey;
    private UserAccountPage5HeightWeight mPage;
    private NumberPicker mHeightSpinner;
    private NumberPicker mWeightSpinner;

    public static UserAccountFragment5HeightWeight create(String key) {
        Bundle args = new Bundle();
        args.putString(ARG_KEY, key);

        UserAccountFragment5HeightWeight fragment = new UserAccountFragment5HeightWeight();
        fragment.setArguments(args);
        return fragment;
    }

    public UserAccountFragment5HeightWeight() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle args = getArguments();
        mKey = args.getString(ARG_KEY);
        mPage = (UserAccountPage5HeightWeight) mCallbacks.onGetPage(mKey);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_wizardpage_user_account_5_height_weight, container, false);
        ((TextView) rootView.findViewById(android.R.id.title)).setText(mPage.getTitle());

        mHeightSpinner = ((NumberPicker) rootView.findViewById(R.id.heightSpinner));
        mHeightSpinner.setMinValue(50);
        mHeightSpinner.setMaxValue(250);
        mHeightSpinner.setFormatter(new NumberPicker.Formatter() {
            @Override
            public String format(int value) {
                return String.format(Locale.ENGLISH, "%.2fm", value / 100d);
            }
        });
        hackFormatCorrectlyOnFirstRendering(mHeightSpinner);

        mWeightSpinner = ((NumberPicker) rootView.findViewById(R.id.weightSpinner));
        mWeightSpinner.setMinValue(40);
        mWeightSpinner.setMaxValue(200);
        mWeightSpinner.setFormatter(new NumberPicker.Formatter() {
            @Override
            public String format(int value) {
                return String.format(Locale.ENGLISH, "%dkg", value);
            }
        });
        hackFormatCorrectlyOnFirstRendering(mWeightSpinner);

        return rootView;
    }

    /**
     * Due to this bug
     * http://stackoverflow.com/questions/17708325/android-numberpicker-with-formatter-does-not-format-on-first-rendering
     */
    private void hackFormatCorrectlyOnFirstRendering(NumberPicker numberPicker) {
        try {
            Method method = numberPicker.getClass().getDeclaredMethod("changeValueByOne", boolean.class);
            method.setAccessible(true);
            method.invoke(numberPicker, true);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
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

        mHeightSpinner.setOnValueChangedListener(this);
        mWeightSpinner.setOnValueChangedListener(this);

        int height = mPage.getData().getInt(UserAccountPage5HeightWeight.HEIGHT_DATA_KEY, 180);
        mPage.getData().putInt(UserAccountPage5HeightWeight.HEIGHT_DATA_KEY, height);
        mHeightSpinner.setValue(height);

        int weight = mPage.getData().getInt(UserAccountPage5HeightWeight.WEIGHT_DATA_KEY, 70);
        mPage.getData().putInt(UserAccountPage5HeightWeight.WEIGHT_DATA_KEY, weight);
        mWeightSpinner.setValue(weight);
    }

    @Override
    public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
        switch (picker.getId()) {
            case R.id.heightSpinner:
                mPage.getData().putInt(UserAccountPage5HeightWeight.HEIGHT_DATA_KEY, newVal);
                mPage.notifyDataChanged();
                break;
            case R.id.weightSpinner:
                mPage.getData().putInt(UserAccountPage5HeightWeight.WEIGHT_DATA_KEY, newVal);
                mPage.notifyDataChanged();
                break;
            default:
                break;
        }
    }
}
