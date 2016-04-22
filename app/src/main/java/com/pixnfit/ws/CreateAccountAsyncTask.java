package com.pixnfit.ws;

import android.content.Context;

import com.pixnfit.common.User;
import com.pixnfit.utils.JSONWsParser;
import com.pixnfit.ws.tasks.WsPostAsyncTask;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by fabier on 19/02/16.
 */
public class CreateAccountAsyncTask extends WsPostAsyncTask<Void, Void, User> {

    private String username;
    private String email;
    private String password;

    public CreateAccountAsyncTask(Context context, String username, String email, String password) {
        super(context);
        this.username = username;
        this.email = email;
        this.password = password;
    }

    @Override
    protected boolean needsAuthentication() {
        return false;
    }

    @Override
    protected String getUrl(Void... params) {
        return "/users";
    }

    @Override
    protected User toResult(String dataAsJSON) throws JSONException {
        return JSONWsParser.parseUser(new JSONObject(dataAsJSON));
    }

    @Override
    protected void writeToHTTP(JSONObject jsonObject, Void... params) throws JSONException {
        jsonObject.put("username", username);
        jsonObject.put("email", email);
        jsonObject.put("password", password);
    }
}
