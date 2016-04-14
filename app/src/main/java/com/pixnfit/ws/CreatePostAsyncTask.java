package com.pixnfit.ws;

import android.content.Context;

import com.pixnfit.common.Post;
import com.pixnfit.utils.JSONWsParser;
import com.pixnfit.ws.tasks.WsPostAsyncTask;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by fabier on 19/02/16.
 */
public class CreatePostAsyncTask extends WsPostAsyncTask<Post, Post, Post> {

    public CreatePostAsyncTask(Context context) {
        super(context);
    }

    @Override
    protected String getUrl(Post... params) {
        return "/posts";
    }

    @Override
    protected void writeToHTTP(JSONObject jsonObject, Post... posts) throws JSONException {
        Post post = posts[0];
        jsonObject.put("name", post.name);
        jsonObject.put("description", post.description);
    }

    @Override
    protected Post toResult(String dataAsJSON) throws JSONException {
        JSONObject object = new JSONObject(dataAsJSON);
        return JSONWsParser.parsePost(object);
    }
}
