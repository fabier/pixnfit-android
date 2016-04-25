package com.pixnfit.common;

/**
 * Created by fabier on 26/02/16.
 */
public class VoteReason extends BaseEntity {
    public String name;

    @Override
    public String getChoiceAsString() {
        return this.name;
    }
}
