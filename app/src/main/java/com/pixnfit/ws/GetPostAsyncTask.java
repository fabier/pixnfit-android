package com.pixnfit.ws;

import android.content.Context;

import com.pixnfit.common.Post;
import com.pixnfit.ws.tasks.WsGetAsyncTask;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.List;
import java.util.Locale;

/**
 * Created by fabier on 16/02/16.
 */
public class GetPostAsyncTask extends WsGetAsyncTask<Integer, Post, List<Post>> {

    private static final int PAGE_SIZE = 100;

    public GetPostAsyncTask(Context context) {
        super(context);
    }

    @Override
    protected String getUrl(Integer... params) {
        return String.format(Locale.ENGLISH, "/posts/help?max=%d&offset=%d", PAGE_SIZE, params[0] * PAGE_SIZE);
    }

    @Override
    protected List<Post> toResult(String dataAsJSON) throws JSONException {
        JSONArray array = new JSONArray(dataAsJSON);
        return JSONWsParser.parsePostList(array);
    }
}
