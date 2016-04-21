package com.pixnfit.wizard;

import android.support.v4.app.Fragment;
import android.text.TextUtils;

import com.tech.freak.wizardpager.model.ModelCallbacks;
import com.tech.freak.wizardpager.model.Page;
import com.tech.freak.wizardpager.model.ReviewItem;

import java.util.ArrayList;


/**
 * A page asking for a name and an email.
 */
public class UserAccountPage2SexBirthdate extends Page {
    public static final String GENDER_DATA_KEY = "gender";
    public static final String BIRTHDATE_DATA_KEY = "birthdate";

    public UserAccountPage2SexBirthdate(ModelCallbacks callbacks, String title) {
        super(callbacks, title);
    }

    @Override
    public Fragment createFragment() {
        return UserAccountFragment2SexBirthdate.create(getKey());
    }

    @Override
    public void getReviewItems(ArrayList<ReviewItem> dest) {
        dest.add(new ReviewItem("Gender", mData.getString(GENDER_DATA_KEY), getKey(), -1));
        dest.add(new ReviewItem("Birthdate", mData.getString(BIRTHDATE_DATA_KEY), getKey(), -1));
    }

    @Override
    public boolean isCompleted() {
        return !TextUtils.isEmpty(mData.getString(GENDER_DATA_KEY))
                && !TextUtils.isEmpty(mData.getString(BIRTHDATE_DATA_KEY));
    }
}
