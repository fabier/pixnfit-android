package com.pixnfit.common;

import java.io.Serializable;

/**
 * Created by fabier on 16/02/16.
 */
public class BaseEntity implements Serializable {
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
}
