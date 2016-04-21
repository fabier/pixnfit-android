package com.pixnfit.wizard;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import com.pixnfit.R;
import com.tech.freak.wizardpager.ui.PageFragmentCallbacks;

/**
 * Created by TechFreak on 04/09/2014.
 */
public class UserAccountFragment4BodyType extends Fragment implements TextWatcher {
    private static final String ARG_KEY = "key";

    private PageFragmentCallbacks mCallbacks;
    private String mKey;
    private UserAccountPage4BodyType mPage;
    private TextView mNameView;
    private TextView mEmailView;
    private TextView mPasswordView;
    private TextView mPassword2View;

    public static UserAccountFragment4BodyType create(String key) {
        Bundle args = new Bundle();
        args.putString(ARG_KEY, key);

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
        mPage = (UserAccountPage4BodyType) mCallbacks.onGetPage(mKey);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_wizardpage_user_account_4_bodytype, container, false);
        ((TextView) rootView.findViewById(android.R.id.title)).setText(mPage.getTitle());

        mNameView = ((TextView) rootView.findViewById(R.id.usernameText));
        mNameView.setText(mPage.getData().getString(UserAccountPage4BodyType.USERNAME_DATA_KEY));

        mEmailView = ((TextView) rootView.findViewById(R.id.emailText));
        mEmailView.setText(mPage.getData().getString(UserAccountPage4BodyType.EMAIL_DATA_KEY));

        mPasswordView = ((TextView) rootView.findViewById(R.id.passwordText));
        mPasswordView.setText(mPage.getData().getString(UserAccountPage4BodyType.PASSWORD_DATA_KEY));

        mPassword2View = ((TextView) rootView.findViewById(R.id.password2Text));
        mPassword2View.setText(mPage.getData().getString(UserAccountPage4BodyType.PASSWORD2_DATA_KEY));

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

        mNameView.addTextChangedListener(this);
        mEmailView.addTextChangedListener(this);
        mPasswordView.addTextChangedListener(this);
        mPassword2View.addTextChangedListener(this);
    }

    @Override
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
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
    }

    @Override
    public void afterTextChanged(Editable editable) {
        String key = null;
        if (editable == mNameView) {
            key = UserAccountPage4BodyType.USERNAME_DATA_KEY;
        } else if (editable == mEmailView) {
            key = UserAccountPage4BodyType.EMAIL_DATA_KEY;
        } else if (editable == mPasswordView) {
            key = UserAccountPage4BodyType.PASSWORD_DATA_KEY;
        } else if (editable == mPassword2View) {
            key = UserAccountPage4BodyType.PASSWORD2_DATA_KEY;
        }

        if (key != null) {
            mPage.getData().putString(key, (editable != null) ? editable.toString() : null);
            mPage.notifyDataChanged();
        }
    }
}
