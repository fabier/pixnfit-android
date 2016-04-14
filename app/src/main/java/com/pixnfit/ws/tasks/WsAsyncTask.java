package com.pixnfit.ws.tasks;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Base64;
import android.util.Log;

import com.pixnfit.ws.WsConstants;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by fabier on 16/02/16.
 */
public abstract class WsAsyncTask<Params, Progress, Result> extends AsyncTask<Params, Progress, Result> implements WsConstants {

    public static final String TAG = WsAsyncTask.class.getSimpleName();

    public static final String HTTP_METHOD_GET = "GET";
    public static final String HTTP_METHOD_POST = "POST";
    public static final String HTTP_METHOD_PUT = "PUT";
    public static final String HTTP_METHOD_DELETE = "DELETE";

    private Context context;

    public WsAsyncTask(Context context) {
        this.context = context;
    }

    public Context getContext() {
        return context;
    }

    protected Result doInBackground(Params... params) {
        String url = getUrl(params);
        try {
            String method = getHTTPMethod();
            HttpURLConnection httpURLConnection = initConnection(url, method);
            httpURLConnection.connect();

            if (HTTP_METHOD_POST.equals(method)
                    || HTTP_METHOD_PUT.equals(method)
                    || HTTP_METHOD_DELETE.equals(method)) {
                OutputStreamWriter wr = new OutputStreamWriter(httpURLConnection.getOutputStream());
                JSONObject json = new JSONObject();
                writeToHTTP(json, params);
                if (json.length() > 0) {
                    wr.write(json.toString());
                }
                wr.close();
            }

            int responseCode = httpURLConnection.getResponseCode();
            if (responseCode == getExpectedHTTPResponseCode()) {
                // Listing successful
                Log.i(TAG, "GET " + url + ": success, HTTP " + responseCode);
                String dataAsJSON = readConnection(httpURLConnection);
                return toResult(dataAsJSON);
            } else {
                // Error
                Log.e(TAG, "GET " + url + ": failed, error HTTP " + responseCode);
                return null;
            }
        } catch (IOException e) {
            // writing exception to log
            Log.e(TAG, "GET " + url + ": IOException", e);
            return null;
        } catch (JSONException e) {
            Log.e(TAG, "GET " + url + ": JSONException", e);
            return null;
        }
    }

    protected abstract String getUrl(Params... params);

    protected abstract Result toResult(String dataAsJSON) throws JSONException;

    protected abstract int getExpectedHTTPResponseCode();

    protected abstract String getHTTPMethod();

    protected abstract void writeToHTTP(JSONObject jsonObject, Params... params) throws JSONException;

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

    protected HttpURLConnection initConnection(String path, String method) throws IOException {
        method = method == null ? "GET" : method;
        URL url = new URL(BASE_URL + path);
        Log.i(TAG, method + " " + url.toString());
        HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
        httpURLConnection.setRequestProperty("Authorization", getAuthorization());
        httpURLConnection.setRequestProperty("Accept", "application/json");
        httpURLConnection.setConnectTimeout(getConnectTimeout());
        httpURLConnection.setReadTimeout(getReadTimeout());
        httpURLConnection.setRequestMethod(method);

        if ("POST".equals(method) || "PUT".equals(method) || "DELETE".equals(method)) {
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setUseCaches(false);
            httpURLConnection.setRequestProperty("Content-Type", "application/json");
        }

        return httpURLConnection;
    }

    protected int getConnectTimeout() {
        return 30000;
    }

    protected int getReadTimeout() {
        return 30000;
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
