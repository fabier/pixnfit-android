package com.pixnfit.common;

/**
 * Created by fabier on 17/02/16.
 */
public class Gender extends BaseEntity {
    public String name;

    @Override
    public String getChoiceAsString() {
        return this.name;
    }
}
