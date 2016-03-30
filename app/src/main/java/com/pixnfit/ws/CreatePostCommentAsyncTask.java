package com.pixnfit.ws;

import android.content.Context;
import android.util.Log;

import com.pixnfit.common.Post;
import com.pixnfit.common.PostComment;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;

/**
 * Created by fabier on 19/02/16.
 */
public class CreatePostCommentAsyncTask extends WsAsyncTask<String, PostComment, PostComment> {

    private static final String TAG = CreatePostCommentAsyncTask.class.getSimpleName();
    private Post post;

    public CreatePostCommentAsyncTask(Context context, Post post) {
        super(context);
        this.post = post;
    }

    @Override
    protected PostComment doInBackground(String... params) {
        String comment = params[0];
        String url = String.format("/posts/%d/comments", post.id);
        try {
            HttpURLConnection httpURLConnection = initConnection(url, "POST");
            httpURLConnection.connect();

            OutputStreamWriter wr = new OutputStreamWriter(httpURLConnection.getOutputStream());
            JSONObject json = new JSONObject();
            json.put("description", comment);
            wr.write(json.toString());
            wr.close();

            int responseCode = httpURLConnection.getResponseCode();
            if (responseCode == 201) {
                // OK
                Log.i(TAG, "POST " + url + ": success, HTTP " + responseCode);
                String dataAsJSON = readConnection(httpURLConnection);
                JSONObject object = new JSONObject(dataAsJSON);
                return JSONWsParser.parsePostComment(object);
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
