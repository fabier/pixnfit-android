package com.pixnfit.ws;

import android.content.Context;

import com.pixnfit.common.User;
import com.pixnfit.ws.tasks.WsPostAsyncTask;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.HttpURLConnection;
import java.util.Locale;

/**
 * Created by fabier on 19/02/16.
 */
public class FollowUserAsyncTask extends WsPostAsyncTask<Void, Void, Boolean> {

    private User user;

    public FollowUserAsyncTask(Context context, User user) {
        super(context);
        this.user = user;
    }

    @Override
    protected String getUrl(Void... params) {
        return String.format(Locale.ENGLISH, "/users/%d/follow", user.id);
    }

    @Override
    protected int getExpectedHTTPResponseCode() {
        return HttpURLConnection.HTTP_OK;
    }

    @Override
    protected Boolean toResult(String dataAsJSON) throws JSONException {
        return true;
    }

    @Override
    protected void writeToHTTP(JSONObject jsonObject, Void... params) throws JSONException {
    }
}
