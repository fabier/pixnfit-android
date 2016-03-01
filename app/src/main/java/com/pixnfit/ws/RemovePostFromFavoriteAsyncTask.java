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
public class RemovePostFromFavoriteAsyncTask extends WsAsyncTask<Void, Void, Boolean> {

    private static final String TAG = RemovePostFromFavoriteAsyncTask.class.getSimpleName();

    private Post post;

    public RemovePostFromFavoriteAsyncTask(Context context, Post post) {
        super(context);
        this.post = post;
    }

    @Override
    protected Boolean doInBackground(Void... params) {
        String url = String.format(Locale.ENGLISH, "/posts/%d/favorite", post.id);
        try {
            HttpURLConnection httpURLConnection = initConnection(url, "DELETE");
            httpURLConnection.connect();

            int responseCode = httpURLConnection.getResponseCode();
            if (responseCode == 200) {
                // OK
                Log.i(TAG, "DELETE " + url + ": success, HTTP " + responseCode);
                return true;
            } else {
                // Error
                Log.e(TAG, "DELETE " + url + ": failed, error HTTP " + responseCode);
                return false;
            }
        } catch (IOException e) {
            // writing exception to log
            Log.e(TAG, "DELETE " + url + ": IOException", e);
            return false;
        }
    }
}
