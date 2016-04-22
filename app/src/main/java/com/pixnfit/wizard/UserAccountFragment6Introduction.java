package com.pixnfit.wizard;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.pixnfit.R;
import com.tech.freak.wizardpager.ui.PageFragmentCallbacks;

/**
 * Created by TechFreak on 04/09/2014.
 */
public class UserAccountFragment6Introduction extends Fragment implements TextWatcher {
    private static final String ARG_KEY = "key";

    private PageFragmentCallbacks mCallbacks;
    private String mKey;
    private UserAccountPage6Introduction mPage;
    private EditText mIntroductionEditText;

    public static UserAccountFragment6Introduction create(String key) {
        Bundle args = new Bundle();
        args.putString(ARG_KEY, key);

        UserAccountFragment6Introduction fragment = new UserAccountFragment6Introduction();
        fragment.setArguments(args);
        return fragment;
    }

    public UserAccountFragment6Introduction() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle args = getArguments();
        mKey = args.getString(ARG_KEY);
        mPage = (UserAccountPage6Introduction) mCallbacks.onGetPage(mKey);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_wizardpage_user_account_6_introduction, container, false);
        ((TextView) rootView.findViewById(android.R.id.title)).setText(mPage.getTitle());

        mIntroductionEditText = ((EditText) rootView.findViewById(R.id.introductionEditText));

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

        mIntroductionEditText.addTextChangedListener(this);

        String introduction = mPage.getData().getString(UserAccountPage6Introduction.INTRODUCTION_DATA_KEY);
        mIntroductionEditText.setText(introduction);
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
    }

    @Override
    public void afterTextChanged(Editable editable) {
        mPage.getData().putString(UserAccountPage6Introduction.INTRODUCTION_DATA_KEY, (editable != null) ? editable.toString() : null);
        mPage.notifyDataChanged();
    }
}
