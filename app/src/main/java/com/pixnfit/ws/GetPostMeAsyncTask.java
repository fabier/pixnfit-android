package com.pixnfit.ws;

import android.content.Context;
import android.util.Log;

import com.pixnfit.common.Post;
import com.pixnfit.common.PostMe;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.Locale;

/**
 * Created by fabier on 16/02/16.
 */
public class GetPostMeAsyncTask extends WsAsyncTask<Post, PostMe, PostMe> {

    private static final String TAG = GetPostMeAsyncTask.class.getSimpleName();

    public GetPostMeAsyncTask(Context context) {
        super(context);
    }

    @Override
    protected PostMe doInBackground(Post... posts) {
        Post post = posts[0];
        String url = String.format(Locale.ENGLISH, "/posts/%d/me", post.id);
        try {
            HttpURLConnection httpURLConnection = initConnection(url);
            httpURLConnection.connect();

            int responseCode = httpURLConnection.getResponseCode();
            if (responseCode == 200) {
                // Listing successful
                Log.i(TAG, "GET " + url + ": success, HTTP " + responseCode);
                String dataAsJSON = readConnection(httpURLConnection);
                JSONObject jsonObject = new JSONObject(dataAsJSON);
                return JSONWsParser.parsePostMe(jsonObject);
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
