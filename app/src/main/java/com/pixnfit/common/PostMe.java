package com.pixnfit.common;

import java.util.List;

/**
 * Created by fabier on 26/02/16.
 */
public class PostMe {
    public boolean isCreator;
    public boolean isFavorite;
    public boolean isFollowingUser;
    public PostVote vote;
    public List<PostComment> comments;
}
