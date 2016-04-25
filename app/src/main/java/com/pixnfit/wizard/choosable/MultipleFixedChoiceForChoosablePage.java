package com.pixnfit.wizard.choosable;

import android.support.v4.app.Fragment;

import com.tech.freak.wizardpager.model.ModelCallbacks;
import com.tech.freak.wizardpager.model.ReviewItem;

import java.util.ArrayList;
import java.util.List;

/**
 * A page offering the user a number of non-mutually exclusive choices.
 */
public class MultipleFixedChoiceForChoosablePage extends SingleFixedChoiceForChoosablePage {
    public MultipleFixedChoiceForChoosablePage(ModelCallbacks callbacks, String title) {
        super(callbacks, title);
    }

    @Override
    public Fragment createFragment() {
        return MultipleChoiceForChoosableFragment.create(getKey());
    }

    @Override
    public void getReviewItems(ArrayList<ReviewItem> dest) {
        StringBuilder sb = new StringBuilder();

        List<Choosable> selections = (List<Choosable>) mData.getSerializable(SIMPLE_DATA_KEY);
        if (selections != null && selections.size() > 0) {
            for (Choosable choosable : selections) {
                if (sb.length() > 0) {
                    sb.append(", ");
                }
                sb.append(choosable.getChoiceAsString());
            }
        }

        dest.add(new ReviewItem(getTitle(), sb.toString(), getKey()));
    }

    @Override
    public boolean isCompleted() {
        List<Choosable> selections = (List<Choosable>) mData.getSerializable(SIMPLE_DATA_KEY);
        return selections != null && selections.size() > 0;
    }
}
