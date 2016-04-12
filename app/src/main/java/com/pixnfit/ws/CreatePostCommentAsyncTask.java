package com.pixnfit.ws;

import android.content.Context;

import com.pixnfit.common.Post;
import com.pixnfit.common.PostComment;
import com.pixnfit.ws.tasks.WsPostAsyncTask;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Locale;

/**
 * Created by fabier on 19/02/16.
 */
public class CreatePostCommentAsyncTask extends WsPostAsyncTask<String, PostComment, PostComment> {

    private Post post;

    public CreatePostCommentAsyncTask(Context context, Post post) {
        super(context);
        this.post = post;
    }

    @Override
    protected String getUrl(String... params) {
        return String.format(Locale.ENGLISH, "/posts/%d/comments", post.id);
    }

    @Override
    protected void writeToHTTP(JSONObject jsonObject, String... params) throws JSONException {
        String comment = params[0];
        jsonObject.put("description", comment);
    }

    @Override
    protected PostComment toResult(String dataAsJSON) throws JSONException {
        JSONObject object = new JSONObject(dataAsJSON);
        return JSONWsParser.parsePostComment(object);
    }
}
