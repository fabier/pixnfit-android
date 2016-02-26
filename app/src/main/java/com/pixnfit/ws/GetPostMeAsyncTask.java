package com.pixnfit.ws;

import android.content.Context;
import android.util.Log;

import com.pixnfit.common.Post;
import com.pixnfit.common.PostMe;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.Locale;

/**
 * Created by fabier on 16/02/16.
 */
public class GetPostMeAsyncTask extends WsAsyncTask<Post, Void, PostMe> {

    private static final String TAG = GetPostMeAsyncTask.class.getSimpleName();

    public GetPostMeAsyncTask(Context context) {
        super(context);
    }

    @Override
    protected PostMe doInBackground(Post... posts) {
        Post post = posts[0];
        try {
            String url = String.format(Locale.ENGLISH, "/posts/%d/me", post.id);
            HttpURLConnection connection = initConnection(url);
            connection.connect();

            int responseCode = connection.getResponseCode();
            if (responseCode == 200) {
                // Listing successful
                Log.i(TAG, "/posts/:id/me: success");
                String dataAsJSON = readConnection(connection);
                JSONObject jsonObject = new JSONObject(dataAsJSON);
                return JSONWsParser.parsePostMe(jsonObject);
            } else {
                // Error
                Log.e(TAG, "/posts/:id/me: failed");
                return null;
            }
        } catch (IOException e) {
            // writing exception to log
            Log.e(TAG, "/posts/:id/me: IOException", e);
            return null;
        } catch (JSONException e) {
            Log.e(TAG, "/posts/:id/me: JSONException", e);
            return null;
        }
    }
}
