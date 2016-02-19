package com.pixnfit.common;

import java.util.Date;

/**
 * Created by fabier on 19/02/16.
 */
public class PostComment extends BaseEntity {
    public String name;
    public String description;
    public long postId;
    public User creator;
    public Date dateCreated;
}
