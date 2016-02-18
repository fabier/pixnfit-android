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
        List<Post> posts = new ArrayList<>();
        try {
            HttpURLConnection connection = initConnection("/posts/help?max=32");
            connection.connect();

            int responseCode = connection.getResponseCode();
            if (responseCode == 200) {
                // Listing successful
                Log.e(TAG, "/posts/help: success");
                InputStream inputStream = connection.getInputStream();

                BufferedReader rd = new BufferedReader(new InputStreamReader(inputStream));
                String line = "";
                StringBuilder stringBuilder = new StringBuilder();
                while ((line = rd.readLine()) != null) {
                    stringBuilder.append(line);
                }
                JSONArray array = new JSONArray(stringBuilder.toString());
                for (int i = 0; i < array.length(); i++) {
                    Post post = JSONWsParser.parsePost(array.getJSONObject(i));
                    publishProgress(post);
                    posts.add(post);
                }
            } else {
                // Error
                Log.e(TAG, "/posts/help: failed");
                return null;
            }
        } catch (IOException e) {
            // writing exception to log
            Log.e(TAG, "/posts/help: IOException", e);
            return null;
        } catch (JSONException e) {
            Log.e(TAG, "/posts/help: JSONException", e);
            return null;
        }
        return posts;
    }
}
