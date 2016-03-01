package com.pixnfit.ws;

import android.content.Context;
import android.util.Log;

import com.pixnfit.common.Image;
import com.pixnfit.common.Post;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by fabier on 19/02/16.
 */
public class AddImageToPostAsyncTask extends WsAsyncTask<Void, Post, List<Post>> {

    private static final String TAG = AddImageToPostAsyncTask.class.getSimpleName();

    private Post post;

    public AddImageToPostAsyncTask(Context context, Post post) {
        super(context);
        this.post = post;
    }

    @Override
    protected List<Post> doInBackground(Void... params) {
        String url = String.format(Locale.ENGLISH, "/posts/%d/images", post.id);
        try {
            List<Post> posts = new ArrayList<>();
            for (Image image : post.images) {
                HttpURLConnection httpURLConnection = initConnection(url, "POST");
                httpURLConnection.connect();

                OutputStreamWriter wr = new OutputStreamWriter(httpURLConnection.getOutputStream());
                JSONObject json = new JSONObject();
                json.put("imageId", image.id);
                wr.write(json.toString());
                wr.close();

                int responseCode = httpURLConnection.getResponseCode();
                if (responseCode == 201) {
                    // Value updated !
                    Log.i(TAG, "POST " + url + ": success, HTTP " + responseCode);
                    String dataAsJSON = readConnection(httpURLConnection);
                    JSONObject object = new JSONObject(dataAsJSON);
                    Post post = JSONWsParser.parsePost(object);
                    posts.add(post);
                } else {
                    // Error
                    Log.e(TAG, "POST " + url + ": failed, error HTTP " + responseCode);
                }
            }
            return posts;
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
