package com.pixnfit.common;

import java.util.Date;

/**
 * Created by fabier on 26/02/16.
 */
public class PostVote extends BaseEntity {
    public boolean vote;
    public VoteReason voteReason;
    public Post post;
    public User creator;
    public Date dateCreated;
}
