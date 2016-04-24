package com.pixnfit.wizard.entity;

import android.support.v4.app.Fragment;

import com.pixnfit.common.BaseEntity;
import com.tech.freak.wizardpager.model.ModelCallbacks;
import com.tech.freak.wizardpager.model.Page;
import com.tech.freak.wizardpager.model.ReviewItem;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by fabier on 24/04/16.
 */
public class SingleFixedEntityChoicePage extends Page {
    protected ArrayList<BaseEntity> mChoices = new ArrayList<>();

    public SingleFixedEntityChoicePage(ModelCallbacks callbacks, String title) {
        super(callbacks, title);
    }

    @Override
    public Fragment createFragment() {
        return SingleEntityChoiceFragment.create(getKey());
    }

    public BaseEntity getOptionAt(int position) {
        return mChoices.get(position);
    }

    public int getOptionCount() {
        return mChoices.size();
    }

    @Override
    public void getReviewItems(ArrayList<ReviewItem> dest) {
        BaseEntity entity = (BaseEntity) mData.getSerializable(SIMPLE_DATA_KEY);
        if (entity != null) {
            dest.add(new ReviewItem(getTitle(), entity.toReviewString(), getKey()));
        }
    }

    @Override
    public boolean isCompleted() {
        return mData.getLong(SIMPLE_DATA_KEY) > 0;
    }

    public SingleFixedEntityChoicePage setChoices(BaseEntity... choices) {
        mChoices.addAll(Arrays.asList(choices));
        return this;
    }

    public SingleFixedEntityChoicePage setValue(BaseEntity value) {
        mData.putSerializable(SIMPLE_DATA_KEY, value);
        return this;
    }
}