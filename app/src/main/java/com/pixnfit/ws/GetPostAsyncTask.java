package com.pixnfit.ws;

import android.content.Context;
import android.util.Log;

import com.pixnfit.common.Post;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by fabier on 16/02/16.
 */
public class GetPostAsyncTask extends WsAsyncTask<Void, Post, List<Post>> {

    private static final String TAG = GetPostAsyncTask.class.getSimpleName();

    public GetPostAsyncTask(Context context) {
        super(context);
    }

    @Override
    protected List<Post> doInBackground(Void... params) {
        try {
            HttpURLConnection connection = initConnection("/posts/help?max=64");
            connection.connect();

            int responseCode = connection.getResponseCode();
            if (responseCode == 200) {
                // Listing successful
                Log.i(TAG, "GET /posts/help: success");
                String dataAsJSON = readConnection(connection);
                JSONArray array = new JSONArray(dataAsJSON);
                return JSONWsParser.parsePostList(array);
            } else {
                // Error
                Log.e(TAG, "GET /posts/help: failed");
                return null;
            }
        } catch (IOException e) {
            // writing exception to log
            Log.e(TAG, "GET /posts/help: IOException", e);
            return null;
        } catch (JSONException e) {
            Log.e(TAG, "GET /posts/help: JSONException", e);
            return null;
        }
    }
}
