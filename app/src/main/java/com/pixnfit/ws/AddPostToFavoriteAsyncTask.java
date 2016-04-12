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
public class AddPostToFavoriteAsyncTask extends WsPostAsyncTask<Void, Void, Boolean> {

    private Post post;

    public AddPostToFavoriteAsyncTask(Context context, Post post) {
        super(context);
        this.post = post;
    }

    @Override
    protected String getUrl(Void... params) {
        return String.format(Locale.ENGLISH, "/posts/%d/favorite", post.id);
    }

    @Override
    protected Boolean toResult(String dataAsJSON) throws JSONException {
        return dataAsJSON != null;
    }

    @Override
    protected void writeToHTTP(JSONObject jsonObject, Void... params) {
    }
}
