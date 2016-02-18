package com.pixnfit.ws;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Base64;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by fabier on 16/02/16.
 */
public abstract class WsAsyncTask<Params, Progress, Result> extends AsyncTask<Params, Progress, Result> implements WsConstants {

    private final String authorization;

    public WsAsyncTask(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("pixnfit", Context.MODE_PRIVATE);
        String login = sharedPreferences.getString("login", "");
        String password = sharedPreferences.getString("password", "");
        String base64BasicAuth = Base64.encodeToString(new String(login + ":" + password).getBytes(), Base64.DEFAULT);
        this.authorization = "Basic " + base64BasicAuth;
    }

    protected HttpURLConnection initConnection(String path) throws IOException {
        return initConnection(path, null);
    }

    protected HttpURLConnection initConnection(String path, String method) throws IOException {
        URL url = new URL(BASE_URL + path);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestProperty("Authorization", authorization);
        connection.setRequestProperty("Accept", "application/json");
        if (method != null) {
            connection.setRequestMethod(method);
        }
        return connection;
    }
}
