package com.pixnfit.ws;

import android.content.Context;

import com.pixnfit.common.Image;
import com.pixnfit.common.Post;
import com.pixnfit.utils.JSONWsParser;
import com.pixnfit.ws.tasks.WsPostAsyncTask;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Locale;

/**
 * Created by fabier on 19/02/16.
 */
public class AddImageToPostAsyncTask extends WsPostAsyncTask<Image, Post, Post> {

    private Post post;

    public AddImageToPostAsyncTask(Context context, Post post) {
        super(context);
        this.post = post;
    }

    @Override
    protected String getUrl(Image... params) {
        return String.format(Locale.ENGLISH, "/posts/%d/images", post.id);
    }

    @Override
    protected void writeToHTTP(JSONObject jsonObject, Image... params) throws JSONException {
        Image image = params[0];
        jsonObject.put("imageId", image.id);
    }

    @Override
    protected Post toResult(String dataAsJSON) throws JSONException {
        JSONObject object = new JSONObject(dataAsJSON);
        return JSONWsParser.parsePost(object);
    }
}
