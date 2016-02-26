package com.pixnfit.ws;

import android.content.Context;
import android.util.Log;

import com.pixnfit.common.Post;

import org.json.JSONArray;
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
        try {
            String url = String.format(Locale.ENGLISH, "/posts/%d/votes", post.id);
            HttpURLConnection connection = initConnection(url, "POST");
            connection.connect();

            OutputStreamWriter wr = new OutputStreamWriter(connection.getOutputStream());
            JSONObject json = new JSONObject();
            json.put("vote", vote);
            wr.write(json.toString());
            wr.close();

            int responseCode = connection.getResponseCode();
            if (responseCode == 200) {
                // Value updated !
                Log.i(TAG, "POST /posts/:id/votes: success");
                return true;
            } else {
                // Error
                Log.e(TAG, "POST /posts/:id/votes: failed");
                return false;
            }
        } catch (IOException e) {
            // writing exception to log
            Log.e(TAG, "POST /posts/:id/votes: IOException", e);
            return false;
        } catch (JSONException e) {
            Log.e(TAG, "POST /posts/:id/votes: JSONException", e);
            return false;
        }
    }
}
