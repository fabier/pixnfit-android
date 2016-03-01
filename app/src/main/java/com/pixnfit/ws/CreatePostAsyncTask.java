package com.pixnfit.ws;

import android.content.Context;
import android.util.Log;

import com.pixnfit.common.Post;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;

/**
 * Created by fabier on 19/02/16.
 */
public class CreatePostAsyncTask extends WsAsyncTask<Post, Post, Post> {

    private static final String TAG = CreatePostAsyncTask.class.getSimpleName();

    public CreatePostAsyncTask(Context context) {
        super(context);
    }

    @Override
    protected Post doInBackground(Post... posts) {
        Post post = posts[0];
        String url = "/posts";
        try {
            HttpURLConnection httpURLConnection = initConnection(url, "POST");
            httpURLConnection.connect();
            initConnection(url, "POST");
            httpURLConnection.connect();

            OutputStreamWriter wr = new OutputStreamWriter(httpURLConnection.getOutputStream());
            JSONObject json = new JSONObject();
            json.put("name", post.name);
            json.put("description", post.description);
            wr.write(json.toString());
            wr.close();

            int responseCode = httpURLConnection.getResponseCode();
            if (responseCode == 201) {
                // OK
                Log.i(TAG, "POST " + url + ": success, HTTP " + responseCode);
                String dataAsJSON = readConnection(httpURLConnection);
                JSONObject object = new JSONObject(dataAsJSON);
                return JSONWsParser.parsePost(object);
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
            // writing exception to log
            Log.e(TAG, "POST " + url + ": IOException", e);
            return null;
        }
    }
}
