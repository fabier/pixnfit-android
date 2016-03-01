package com.pixnfit.ws;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Base64;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by fabier on 16/02/16.
 */
public abstract class WsAsyncTask<Params, Progress, Result> extends AsyncTask<Params, Progress, Result> implements WsConstants {

    public static final String TAG = WsAsyncTask.class.getSimpleName();

    private Context context;

    public WsAsyncTask(Context context) {
        this.context = context;
    }

    public Context getContext() {
        return context;
    }

    protected String getLogin() {
        SharedPreferences sharedPreferences = context.getSharedPreferences("pixnfit", Context.MODE_PRIVATE);
        return sharedPreferences.getString("login", "");
    }

    protected String getPassword() {
        SharedPreferences sharedPreferences = context.getSharedPreferences("pixnfit", Context.MODE_PRIVATE);
        return sharedPreferences.getString("password", "");
    }

    private String getBasicAuthString() {
        String login = getLogin();
        String password = getPassword();
        return Base64.encodeToString(("" + login + ":" + password).getBytes(), Base64.DEFAULT);
    }

    private String getAuthorization() {
        return "Basic " + getBasicAuthString();
    }

    protected HttpURLConnection initConnection(String path) throws IOException {
        return initConnection(path, null);
    }

    protected HttpURLConnection initConnection(String path, String method) throws IOException {
        method = method == null ? "GET" : method;
        URL url = new URL(BASE_URL + path);
        Log.i(TAG, method + " " + url.toString());
        HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
        httpURLConnection.setRequestProperty("Authorization", getAuthorization());
        httpURLConnection.setRequestProperty("Accept", "application/json");
        httpURLConnection.setConnectTimeout(10000);
        httpURLConnection.setReadTimeout(10000);
        if (method != null) {
            httpURLConnection.setRequestMethod(method);
        }

        if ("POST".equals(method) || "PUT".equals(method) || "DELETE".equals(method)) {
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setUseCaches(false);
            httpURLConnection.setRequestProperty("Content-Type", "application/json");
        }

        return httpURLConnection;
    }

    protected String readConnection(HttpURLConnection connection) throws IOException {
        InputStream inputStream = connection.getInputStream();
        BufferedReader rd = new BufferedReader(new InputStreamReader(inputStream));
        String line = "";
        StringBuilder stringBuilder = new StringBuilder();
        while ((line = rd.readLine()) != null) {
            stringBuilder.append(line);
        }
        return stringBuilder.toString();
    }
}
