package com.pixnfit.ws;

import android.content.Context;
import android.util.Log;

import com.pixnfit.common.Post;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.Locale;

/**
 * Created by fabier on 19/02/16.
 */
public class AddPostToFavoriteAsyncTask extends WsAsyncTask<Void, Void, Boolean> {

    private static final String TAG = AddPostToFavoriteAsyncTask.class.getSimpleName();

    private Post post;

    public AddPostToFavoriteAsyncTask(Context context, Post post) {
        super(context);
        this.post = post;
    }

    @Override
    protected Boolean doInBackground(Void... params) {
        try {
            String url = String.format(Locale.ENGLISH, "/posts/%d/favorite", post.id);
            HttpURLConnection connection = initConnection(url, "POST");
            connection.connect();

            int responseCode = connection.getResponseCode();
            if (responseCode == 201) {
                // OK
                Log.i(TAG, "POST /posts/:id/favorite: success, HTTP " + responseCode);
                return true;
            } else {
                // Error
                Log.e(TAG, "POST /posts/:id/favorite: failed, error HTTP " + responseCode);
                return false;
            }
        } catch (IOException e) {
            // writing exception to log
            Log.e(TAG, "POST /posts/:id/favorite: IOException", e);
            return false;
        }
    }
}
