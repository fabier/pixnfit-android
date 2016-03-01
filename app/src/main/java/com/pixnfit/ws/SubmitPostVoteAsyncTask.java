package com.pixnfit.ws;

import android.content.Context;
import android.util.Log;

import com.pixnfit.common.Post;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.util.Locale;

/**
 * Created by fabier on 19/02/16.
 */
public class SubmitPostVoteAsyncTask extends WsAsyncTask<Void, Void, Boolean> {

    private static final String TAG = SubmitPostVoteAsyncTask.class.getSimpleName();

    private Post post;
    private boolean vote;

    public SubmitPostVoteAsyncTask(Context context, Post post, boolean vote) {
        super(context);
        this.post = post;
        this.vote = vote;
    }

    @Override
    protected Boolean doInBackground(Void... params) {
        String url = String.format(Locale.ENGLISH, "/posts/%d/votes", post.id);
        try {
            HttpURLConnection httpURLConnection = initConnection(url, "POST");
            httpURLConnection.connect();

            OutputStreamWriter wr = new OutputStreamWriter(httpURLConnection.getOutputStream());
            JSONObject json = new JSONObject();
            json.put("vote", vote);
            wr.write(json.toString());
            wr.close();

            int responseCode = httpURLConnection.getResponseCode();
            if (responseCode == 201) {
                // Value updated !
                Log.i(TAG, "POST " + url + ": success, HTTP " + responseCode);
                return true;
            } else {
                // Error
                Log.e(TAG, "POST " + url + ": failed, error HTTP " + responseCode);
                return false;
            }
        } catch (IOException e) {
            // writing exception to log
            Log.e(TAG, "POST " + url + ": IOException", e);
            return false;
        } catch (JSONException e) {
            Log.e(TAG, "POST " + url + ": JSONException", e);
            return false;
        }
    }
}
