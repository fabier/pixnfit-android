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
public class UserAccountPage4BodyType extends Page {
    public static final String BODYTYPE_DATA_KEY = "bodytype";

    public UserAccountPage4BodyType(ModelCallbacks callbacks, String title) {
        super(callbacks, title);
    }

    @Override
    public Fragment createFragment() {
        return UserAccountFragment4BodyType.create(getKey());
    }

    @Override
    public void getReviewItems(ArrayList<ReviewItem> dest) {
        dest.add(new ReviewItem("BodyType", Integer.toString(mData.getInt(BODYTYPE_DATA_KEY)), getKey(), -1));
    }

    @Override
    public boolean isCompleted() {
        return !TextUtils.isEmpty(mData.getString(BODYTYPE_DATA_KEY));
    }
}
