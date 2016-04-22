package com.pixnfit.wizard;

import android.support.v4.app.Fragment;
import android.text.TextUtils;

import com.tech.freak.wizardpager.model.ModelCallbacks;
import com.tech.freak.wizardpager.model.Page;
import com.tech.freak.wizardpager.model.ReviewItem;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.validator.routines.EmailValidator;

import java.util.ArrayList;


/**
 * A page asking for a name and an email.
 */
public class UserAccountPage1NameEmailPassword extends Page {
    public static final String USERNAME_DATA_KEY = "username";
    public static final String EMAIL_DATA_KEY = "email";
    public static final String PASSWORD_DATA_KEY = "password";
    public static final String PASSWORD2_DATA_KEY = "password2";

    public UserAccountPage1NameEmailPassword(ModelCallbacks callbacks, String title) {
        super(callbacks, title);
    }

    @Override
    public Fragment createFragment() {
        return UserAccountFragment1NameEmailPassword.create(getKey());
    }

    @Override
    public void getReviewItems(ArrayList<ReviewItem> dest) {
        dest.add(new ReviewItem("Username", mData.getString(USERNAME_DATA_KEY), getKey(), -1));
        dest.add(new ReviewItem("Email", mData.getString(EMAIL_DATA_KEY), getKey(), -1));
        dest.add(new ReviewItem("Password", "***********", getKey(), -1));
    }

    @Override
    public boolean isCompleted() {
        return !TextUtils.isEmpty(mData.getString(USERNAME_DATA_KEY))
                && EmailValidator.getInstance().isValid(mData.getString(EMAIL_DATA_KEY))
                && !TextUtils.isEmpty(mData.getString(PASSWORD_DATA_KEY))
                && !TextUtils.isEmpty(mData.getString(PASSWORD2_DATA_KEY))
                && StringUtils.equals(mData.getString(PASSWORD_DATA_KEY), mData.getString(PASSWORD2_DATA_KEY));
    }
}
