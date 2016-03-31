package com.pixnfit.ws;

import android.content.Context;
import android.util.Log;

import com.pixnfit.common.Post;
import com.pixnfit.common.User;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.List;
import java.util.Locale;

/**
 * Created by fabier on 16/02/16.
 */
public class GetUserPostAsyncTask extends WsAsyncTask<User, Post, List<Post>> {

    private static final String TAG = GetUserPostAsyncTask.class.getSimpleName();

    private static final int PAGE_SIZE = 40;

    public GetUserPostAsyncTask(Context context) {
        super(context);
    }

    @Override
    protected List<Post> doInBackground(User... params) {
        User user = params[0];
        String url = String.format(Locale.ENGLISH, "/users/%d/posts", user.id);
        try {
            HttpURLConnection httpURLConnection = initConnection(url);
            httpURLConnection.connect();

            int responseCode = httpURLConnection.getResponseCode();
            if (responseCode == 200) {
                // Listing successful
                Log.i(TAG, "GET " + url + ": success, HTTP " + responseCode);
                String dataAsJSON = readConnection(httpURLConnection);
                JSONArray array = new JSONArray(dataAsJSON);
                return JSONWsParser.parsePostList(array);
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
