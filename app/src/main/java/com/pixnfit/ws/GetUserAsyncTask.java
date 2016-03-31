package com.pixnfit.ws;

import android.content.Context;
import android.util.Log;

import com.pixnfit.common.User;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.Locale;

/**
 * Created by fabier on 16/02/16.
 */
public class GetUserAsyncTask extends WsAsyncTask<User, User, User> {

    private static final String TAG = GetUserAsyncTask.class.getSimpleName();

    public GetUserAsyncTask(Context context) {
        super(context);
    }

    @Override
    protected User doInBackground(User... params) {
        User user = params[0];
        String url = String.format(Locale.ENGLISH, "/users/%d", user.id);
        try {
            HttpURLConnection httpURLConnection = initConnection(url);
            httpURLConnection.connect();

            int responseCode = httpURLConnection.getResponseCode();
            if (responseCode == 200) {
                // OK
                Log.i(TAG, "GET " + url + ": success, HTTP " + responseCode);
                String dataAsJSON = readConnection(httpURLConnection);
                JSONObject jsonObject = new JSONObject(dataAsJSON);
                return JSONWsParser.parseUser(jsonObject);
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
}
