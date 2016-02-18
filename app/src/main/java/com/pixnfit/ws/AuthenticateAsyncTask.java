package com.pixnfit.ws;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Base64;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by fabier on 16/02/16.
 */
public class AuthenticateAsyncTask extends WsAsyncTask<Void, Void, JSONObject> {

    private static final String TAG = AuthenticateAsyncTask.class.getSimpleName();

    public AuthenticateAsyncTask(Context context) {
        super(context);
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
                InputStream inputStream = connection.getInputStream();

                BufferedReader rd = new BufferedReader(new InputStreamReader(inputStream));
                String line = "";
                StringBuilder stringBuilder = new StringBuilder();
                while ((line = rd.readLine()) != null) {
                    stringBuilder.append(line);
                }
                return new JSONObject(stringBuilder.toString());
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





