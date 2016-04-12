package com.pixnfit.ws;

import android.content.Context;

import com.pixnfit.common.User;
import com.pixnfit.ws.tasks.WsGetAsyncTask;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.List;
import java.util.Locale;

/**
 * Created by fabier on 16/02/16.
 */
public class GetUserFollowersAsyncTask extends WsGetAsyncTask<User, User, List<User>> {

    public GetUserFollowersAsyncTask(Context context) {
        super(context);
    }

    @Override
    protected String getUrl(User... params) {
        User user = params[0];
        return String.format(Locale.ENGLISH, "/users/%d/followers", user.id);
    }

    @Override
    protected List<User> toResult(String dataAsJSON) throws JSONException {
        JSONArray array = new JSONArray(dataAsJSON);
        return JSONWsParser.parseUserList(array);
    }
}
