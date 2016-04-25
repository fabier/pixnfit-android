package com.pixnfit.wizard.choosable;

import android.support.v4.app.Fragment;

import com.tech.freak.wizardpager.model.ModelCallbacks;
import com.tech.freak.wizardpager.model.Page;
import com.tech.freak.wizardpager.model.ReviewItem;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by fabier on 24/04/16.
 */
public class SingleFixedChoiceForChoosablePage extends Page {
    protected ArrayList<Choosable> mChoices = new ArrayList<>();

    public SingleFixedChoiceForChoosablePage(ModelCallbacks callbacks, String title) {
        super(callbacks, title);
    }

    @Override
    public Fragment createFragment() {
        return SingleChoiceForChoosableFragment.create(getKey());
    }

    public Choosable getOptionAt(int position) {
        return mChoices.get(position);
    }

    public int getOptionCount() {
        return mChoices.size();
    }

    @Override
    public void getReviewItems(ArrayList<ReviewItem> dest) {
        Choosable choosable = (Choosable) mData.getSerializable(SIMPLE_DATA_KEY);
        if (choosable != null) {
            dest.add(new ReviewItem(getTitle(), choosable.getChoiceAsString(), getKey()));
        }
    }

    @Override
    public boolean isCompleted() {
        return mData.getLong(SIMPLE_DATA_KEY) > 0;
    }

    public SingleFixedChoiceForChoosablePage setChoices(Choosable... choices) {
        mChoices.addAll(Arrays.asList(choices));
        return this;
    }

    public SingleFixedChoiceForChoosablePage setValue(Choosable value) {
        mData.putSerializable(SIMPLE_DATA_KEY, value);
        return this;
    }
}