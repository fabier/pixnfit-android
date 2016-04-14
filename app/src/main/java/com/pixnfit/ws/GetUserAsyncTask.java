package com.pixnfit.ws;

import android.content.Context;

import com.pixnfit.common.User;
import com.pixnfit.utils.JSONWsParser;
import com.pixnfit.ws.tasks.WsGetAsyncTask;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Locale;

/**
 * Created by fabier on 16/02/16.
 */
public class GetUserAsyncTask extends WsGetAsyncTask<User, User, User> {

    public GetUserAsyncTask(Context context) {
        super(context);
    }

    @Override
    protected String getUrl(User... params) {
        User user = params[0];
        return String.format(Locale.ENGLISH, "/users/%d", user.id);
    }

    @Override
    protected User toResult(String dataAsJSON) throws JSONException {
        JSONObject jsonObject = new JSONObject(dataAsJSON);
        return JSONWsParser.parseUser(jsonObject);
    }
}
