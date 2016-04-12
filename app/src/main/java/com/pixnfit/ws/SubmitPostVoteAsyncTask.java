package com.pixnfit.ws;

import android.content.Context;

import com.pixnfit.common.Post;
import com.pixnfit.ws.tasks.WsPostAsyncTask;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Locale;

/**
 * Created by fabier on 19/02/16.
 */
public class SubmitPostVoteAsyncTask extends WsPostAsyncTask<Void, Void, Boolean> {

    private Post post;
    private boolean vote;

    public SubmitPostVoteAsyncTask(Context context, Post post, boolean vote) {
        super(context);
        this.post = post;
        this.vote = vote;
    }

    @Override
    protected String getUrl(Void... params) {
        return String.format(Locale.ENGLISH, "/posts/%d/votes", post.id);
    }

    @Override
    protected Boolean toResult(String dataAsJSON) throws JSONException {
        return true;
    }

    @Override
    protected void writeToHTTP(JSONObject jsonObject, Void... params) throws JSONException {
        jsonObject.put("vote", vote);
    }
}
