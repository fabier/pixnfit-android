package com.pixnfit.ws;

import android.content.Context;

import com.pixnfit.common.Post;
import com.pixnfit.common.User;
import com.pixnfit.utils.JSONWsParser;
import com.pixnfit.ws.tasks.WsGetAsyncTask;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.List;
import java.util.Locale;

/**
 * Created by fabier on 16/02/16.
 */
public class GetUserDressingPostAsyncTask extends WsGetAsyncTask<User, Post, List<Post>> {

    public GetUserDressingPostAsyncTask(Context context) {
        super(context);
    }

    @Override
    protected String getUrl(User... params) {
        User user = params[0];
        return String.format(Locale.ENGLISH, "/users/%d/posts/dressing", user.id);
    }

    @Override
    protected List<Post> toResult(String dataAsJSON) throws JSONException {
        JSONArray array = new JSONArray(dataAsJSON);
        return JSONWsParser.parsePostList(array);
    }
}
