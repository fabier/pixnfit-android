package com.pixnfit.common;

import java.util.Date;
import java.util.List;

/**
 * Created by fabier on 16/02/16.
 */
public class Post extends BaseEntity {
    public String name;
    public String description;
    public List<Image> images;
    public User creator;
    public Date dateCreated;
    public PostType postType;
    public Visibility visibility;
    public State state;
    public int viewCount;
}
