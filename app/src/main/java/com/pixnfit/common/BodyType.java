package com.pixnfit.common;

import com.pixnfit.R;

import org.apache.commons.lang3.reflect.FieldUtils;

/**
 * Created by fabier on 17/02/16.
 */
public class BodyType extends BaseEntity {
    public String name;

    @Override
    public String getChoiceAsString() {
        return this.name;
    }

    @Override
    public int getDrawableId() {
        try {
            return (int) FieldUtils.readStaticField(R.drawable.class, "bodytype_female_" + id);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            return 0;
        }
    }
}
