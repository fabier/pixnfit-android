package com.pixnfit.wizard;

import android.support.v4.app.Fragment;

import com.tech.freak.wizardpager.model.ModelCallbacks;
import com.tech.freak.wizardpager.model.Page;
import com.tech.freak.wizardpager.model.ReviewItem;

import java.util.ArrayList;


/**
 * A page asking for a name and an email.
 */
public class UserAccountPage5HeightWeight extends Page {
    public static final String VISIBILITY_DATA_KEY = "visibility";
    public static final String HEIGHT_DATA_KEY = "height";
    public static final String WEIGHT_DATA_KEY = "weight";

    public UserAccountPage5HeightWeight(ModelCallbacks callbacks, String title) {
        super(callbacks, title);
    }

    @Override
    public Fragment createFragment() {
        return UserAccountFragment5VisibilityHeightWeight.create(getKey());
    }

    @Override
    public void getReviewItems(ArrayList<ReviewItem> dest) {
        dest.add(new ReviewItem("Visibility", Long.toString(mData.getLong(VISIBILITY_DATA_KEY)), getKey(), -1));
        dest.add(new ReviewItem("Height", Integer.toString(mData.getInt(HEIGHT_DATA_KEY)), getKey(), -1));
        dest.add(new ReviewItem("Weight", Integer.toString(mData.getInt(WEIGHT_DATA_KEY)), getKey(), -1));
    }

    @Override
    public boolean isCompleted() {
        return mData.getLong(VISIBILITY_DATA_KEY) > 0
                && mData.getInt(HEIGHT_DATA_KEY) > 0
                && mData.getInt(WEIGHT_DATA_KEY) > 0;
    }
}
