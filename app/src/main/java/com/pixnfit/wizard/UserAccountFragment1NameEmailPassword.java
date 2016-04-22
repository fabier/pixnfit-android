package com.pixnfit.wizard;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.pixnfit.R;
import com.pixnfit.utils.TextWatcherAdapter;
import com.tech.freak.wizardpager.ui.PageFragmentCallbacks;

/**
 * Created by TechFreak on 04/09/2014.
 */
public class UserAccountFragment1NameEmailPassword extends Fragment {
    private static final String ARG_KEY = "key";

    private PageFragmentCallbacks mCallbacks;
    private String mKey;
    private UserAccountPage1NameEmailPassword mPage;
    private TextView mNameView;
//    private TextView mEmailView;
//    private TextView mPasswordView;
//    private TextView mPassword2View;

    public static UserAccountFragment1NameEmailPassword create(String key) {
        Bundle args = new Bundle();
        args.putString(ARG_KEY, key);

        UserAccountFragment1NameEmailPassword fragment = new UserAccountFragment1NameEmailPassword();
        fragment.setArguments(args);
        return fragment;
    }

    public UserAccountFragment1NameEmailPassword() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle args = getArguments();
        mKey = args.getString(ARG_KEY);
        mPage = (UserAccountPage1NameEmailPassword) mCallbacks.onGetPage(mKey);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_wizardpage_user_account_1_name_email_password, container, false);
        ((TextView) rootView.findViewById(android.R.id.title)).setText(mPage.getTitle());

        mNameView = ((TextView) rootView.findViewById(R.id.usernameText));
        mNameView.setText(mPage.getData().getString(UserAccountPage1NameEmailPassword.USERNAME_DATA_KEY));

//        mEmailView = ((TextView) rootView.findViewById(R.id.emailText));
//        mEmailView.setText(mPage.getData().getString(UserAccountPage1NameEmailPassword.EMAIL_DATA_KEY));

//        mPasswordView = ((TextView) rootView.findViewById(R.id.passwordText));
//        mPasswordView.setText(mPage.getData().getString(UserAccountPage1NameEmailPassword.PASSWORD_DATA_KEY));

//        mPassword2View = ((TextView) rootView.findViewById(R.id.password2Text));
//        mPassword2View.setText(mPage.getData().getString(UserAccountPage1NameEmailPassword.PASSWORD2_DATA_KEY));

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

        mNameView.addTextChangedListener(new TextWatcherAdapter() {
            @Override
            public void afterTextChanged(Editable editable) {
                mPage.getData().putString(UserAccountPage1NameEmailPassword.USERNAME_DATA_KEY, (editable != null) ? editable.toString() : null);
                mPage.notifyDataChanged();
            }
        });
//        mEmailView.addTextChangedListener(new TextWatcherAdapter() {
//            @Override
//            public void afterTextChanged(Editable editable) {
//                mPage.getData().putString(UserAccountPage1NameEmailPassword.EMAIL_DATA_KEY, (editable != null) ? editable.toString() : null);
//                mPage.notifyDataChanged();
//        }
//        });
//        mPasswordView.addTextChangedListener(new TextWatcherAdapter() {
//            @Override
//            public void afterTextChanged(Editable editable) {
//                mPage.getData().putString(UserAccountPage1NameEmailPassword.PASSWORD_DATA_KEY, (editable != null) ? editable.toString() : null);
//                mPage.notifyDataChanged();
//            }
//        });
//        mPassword2View.addTextChangedListener(new TextWatcherAdapter() {
//            @Override
//            public void afterTextChanged(Editable editable) {
//                mPage.getData().putString(UserAccountPage1NameEmailPassword.PASSWORD2_DATA_KEY, (editable != null) ? editable.toString() : null);
//                mPage.notifyDataChanged();
//            }
//        });
    }
}
