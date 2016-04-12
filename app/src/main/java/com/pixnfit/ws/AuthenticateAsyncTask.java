package com.pixnfit.ws;

import android.content.Context;

import com.pixnfit.ws.tasks.WsPostAsyncTask;

import org.json.JSONException;
import org.json.JSONObject;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by fabier on 16/02/16.
 */
public class AuthenticateAsyncTask extends WsPostAsyncTask<Void, Void, JSONObject> {

    private String login;
    private String password;

    public AuthenticateAsyncTask(Context context, String login, String password) {
        super(context);
        this.login = login;
        this.password = password;
    }

    @Override
    protected String getLogin() {
        return this.login;
    }

    @Override
    protected String getPassword() {
        return this.password;
    }

    @Override
    protected String getUrl(Void... params) {
        return "/auth";
    }

    @Override
    protected int getExpectedHTTPResponseCode() {
        return HttpsURLConnection.HTTP_OK;
    }

    @Override
    protected JSONObject toResult(String dataAsJSON) throws JSONException {
        return new JSONObject(dataAsJSON);
    }

    @Override
    protected void writeToHTTP(JSONObject jsonObject, Void... params) throws JSONException {
    }
}





