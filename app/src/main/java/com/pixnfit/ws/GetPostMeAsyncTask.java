package com.pixnfit.ws;

import android.content.Context;

import com.pixnfit.common.Post;
import com.pixnfit.common.PostMe;
import com.pixnfit.ws.tasks.WsGetAsyncTask;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Locale;

/**
 * Created by fabier on 16/02/16.
 */
public class GetPostMeAsyncTask extends WsGetAsyncTask<Post, PostMe, PostMe> {

    public GetPostMeAsyncTask(Context context) {
        super(context);
    }

    @Override
    protected String getUrl(Post... params) {
        Post post = params[0];
        return String.format(Locale.ENGLISH, "/posts/%d/me", post.id);
    }

    @Override
    protected PostMe toResult(String dataAsJSON) throws JSONException {
        JSONObject jsonObject = new JSONObject(dataAsJSON);
        return JSONWsParser.parsePostMe(jsonObject);
    }
}
