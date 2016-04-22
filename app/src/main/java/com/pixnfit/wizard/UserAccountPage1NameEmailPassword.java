package com.pixnfit.wizard;

import android.support.v4.app.Fragment;

import com.tech.freak.wizardpager.model.ModelCallbacks;
import com.tech.freak.wizardpager.model.Page;
import com.tech.freak.wizardpager.model.ReviewItem;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;


/**
 * A page asking for a name and an email.
 */
public class UserAccountPage1NameEmailPassword extends Page {
    public static final String USERID_DATA_KEY = "userid";
    public static final String USERNAME_DATA_KEY = "username";

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
    }

    @Override
    public boolean isCompleted() {
        return StringUtils.isNotBlank(mData.getString(USERNAME_DATA_KEY));
    }
}
