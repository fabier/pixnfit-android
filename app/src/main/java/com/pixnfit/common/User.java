package com.pixnfit.common;

import java.util.Date;

/**
 * Created by fabier on 16/02/16.
 */
public class User extends BaseEntity {
    public String username;
    public Image image;
    public String description;
    public BodyType bodyType;
    public Gender gender;
    public Date birthdate;
    public Integer height;
    public Integer weight;
    public Country country;
    public Language language;
    public Integer points;
    public Integer postCount;
    public Integer followersCount;
    public Integer followedCount;
}
