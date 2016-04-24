package com.pixnfit.wizard.entity;

import android.support.v4.app.Fragment;

import com.pixnfit.common.BaseEntity;
import com.tech.freak.wizardpager.model.ModelCallbacks;
import com.tech.freak.wizardpager.model.ReviewItem;

import java.util.ArrayList;
import java.util.List;

/**
 * A page offering the user a number of non-mutually exclusive choices.
 */
public class MultipleFixedEntityChoicePage extends SingleFixedEntityChoicePage {
    public MultipleFixedEntityChoicePage(ModelCallbacks callbacks, String title) {
        super(callbacks, title);
    }

    @Override
    public Fragment createFragment() {
        return MultipleEntityChoiceFragment.create(getKey());
    }

    @Override
    public void getReviewItems(ArrayList<ReviewItem> dest) {
        StringBuilder sb = new StringBuilder();

        List<BaseEntity> selections = (List<BaseEntity>) mData.getSerializable(SIMPLE_DATA_KEY);
        if (selections != null && selections.size() > 0) {
            for (BaseEntity selection : selections) {
                if (sb.length() > 0) {
                    sb.append(", ");
                }
                sb.append(selection.toString());
            }
        }

        dest.add(new ReviewItem(getTitle(), sb.toString(), getKey()));
    }

    @Override
    public boolean isCompleted() {
        List<BaseEntity> selections = (List<BaseEntity>) mData.getSerializable(SIMPLE_DATA_KEY);
        return selections != null && selections.size() > 0;
    }
}
