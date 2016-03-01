package com.pixnfit.ws;

import android.content.Context;
import android.util.Log;

import com.pixnfit.common.Post;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;
import java.net.HttpURLConnection;
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
        String url = "/posts/help?max=64";
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
