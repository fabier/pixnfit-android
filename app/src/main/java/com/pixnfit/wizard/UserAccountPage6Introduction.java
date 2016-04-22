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
public class UserAccountPage6Introduction extends Page {
    public static final String INTRODUCTION_DATA_KEY = "introduction";

    public UserAccountPage6Introduction(ModelCallbacks callbacks, String title) {
        super(callbacks, title);
    }

    @Override
    public Fragment createFragment() {
        return UserAccountFragment6Introduction.create(getKey());
    }

    @Override
    public void getReviewItems(ArrayList<ReviewItem> dest) {
        dest.add(new ReviewItem("Introduction", mData.getString(INTRODUCTION_DATA_KEY), getKey(), -1));
    }

    @Override
    public boolean isCompleted() {
        return StringUtils.isNotBlank(mData.getString(INTRODUCTION_DATA_KEY));
    }
}
