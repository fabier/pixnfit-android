package com.pixnfit.common;

/**
 * Created by fabier on 16/02/16.
 */
public class PostType extends BaseEntity {
    public String name;

    @Override
    public String getChoiceAsString() {
        return this.name;
    }
}
