package com.pixnfit.ws;

import android.content.Context;

import com.pixnfit.common.User;
import com.pixnfit.ws.tasks.WsDeleteAsyncTask;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Locale;

/**
 * Created by fabier on 19/02/16.
 */
public class UnfollowUserAsyncTask extends WsDeleteAsyncTask<Void, Void, Boolean> {

    private User user;

    public UnfollowUserAsyncTask(Context context, User user) {
        super(context);
        this.user = user;
    }

    @Override
    protected String getUrl(Void... params) {
        return String.format(Locale.ENGLISH, "/users/%d/follow", user.id);
    }

    @Override
    protected Boolean toResult(String dataAsJSON) throws JSONException {
        return true;
    }

    @Override
    protected void writeToHTTP(JSONObject jsonObject, Void... params) throws JSONException {
    }
}
