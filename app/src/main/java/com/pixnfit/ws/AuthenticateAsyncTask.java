package com.pixnfit.ws;

import android.content.Context;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.HttpURLConnection;

/**
 * Created by fabier on 16/02/16.
 */
public class AuthenticateAsyncTask extends WsAsyncTask<Void, Void, JSONObject> {

    private static final String TAG = AuthenticateAsyncTask.class.getSimpleName();
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
    protected JSONObject doInBackground(Void... params) {
        String url = "/auth";
        try {
            HttpURLConnection httpURLConnection = initConnection(url, "POST");
            httpURLConnection.connect();

            int responseCode = httpURLConnection.getResponseCode();
            if (responseCode == 200) {
                // Authentication successful
                Log.i(TAG, "POST " + url + ": success, HTTP " + responseCode);
                String dataAsJSON = readConnection(httpURLConnection);
                return new JSONObject(dataAsJSON);
            } else {
                // Error
                Log.e(TAG, "POST " + url + ": failed, error HTTP " + responseCode);
                return null;
            }
        } catch (IOException e) {
            // writing exception to log
            Log.e(TAG, "POST " + url + ": IOException", e);
            return null;
        } catch (JSONException e) {
            Log.e(TAG, "POST " + url + ": JSONException", e);
            return null;
        }
    }
}





