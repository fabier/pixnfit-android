package com.pixnfit.ws;

import android.content.Context;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
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
        try {
            HttpURLConnection connection = initConnection("/auth", "POST");
            connection.connect();

            int responseCode = connection.getResponseCode();
            if (responseCode == 200) {
                // Authentication successful
                Log.e(TAG, "/auth: success");
                String dataAsJSON = readConnection(connection);
                return new JSONObject(dataAsJSON);
            } else {
                // Error
                Log.e(TAG, "/auth: failed");
                return null;
            }
        } catch (IOException e) {
            // writing exception to log
            Log.e(TAG, "/auth: IOException", e);
            return null;
        } catch (JSONException e) {
            Log.e(TAG, "/auth: JSONException", e);
            return null;
        }
    }
}





