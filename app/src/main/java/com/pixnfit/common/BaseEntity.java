package com.pixnfit.common;

import com.pixnfit.wizard.entity.ReviewableElement;

import org.apache.commons.lang3.reflect.FieldUtils;

import java.io.Serializable;

/**
 * Created by fabier on 16/02/16.
 */
public class BaseEntity implements Serializable, ReviewableElement {
    public long id;

    @Override
    public boolean equals(Object o) {
        if (o instanceof BaseEntity) {
            long otherId = ((BaseEntity) o).id;
            if (otherId == 0 || this.id == 0) {
                return false;
            } else {
                return this.id == otherId;
            }
        } else {
            return false;
        }
    }

    public String toReviewString() {
        try {
            return (String) FieldUtils.readField(this, "name");
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            return null;
        }
    }
}
