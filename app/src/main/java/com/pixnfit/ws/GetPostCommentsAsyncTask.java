package com.pixnfit.ws;

import android.content.Context;
import android.util.Log;

import com.pixnfit.common.Post;
import com.pixnfit.common.PostComment;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.List;
import java.util.Locale;

/**
 * Created by fabier on 16/02/16.
 */
public class GetPostCommentsAsyncTask extends WsAsyncTask<Post, PostComment, List<PostComment>> {

    private static final String TAG = GetPostCommentsAsyncTask.class.getSimpleName();

    public GetPostCommentsAsyncTask(Context context) {
        super(context);
    }

    @Override
    protected List<PostComment> doInBackground(Post... posts) {
        Post post = posts[0];
        try {
            String url = String.format(Locale.ENGLISH, "/posts/%d/comments", post.id);
            HttpURLConnection connection = initConnection(url);
            connection.connect();

            int responseCode = connection.getResponseCode();
            if (responseCode == 200) {
                // Listing successful
                Log.i(TAG, "GET /posts/:id/comments: success, HTTP " + responseCode);
                String dataAsJSON = readConnection(connection);
                JSONArray array = new JSONArray(dataAsJSON);
                return JSONWsParser.parsePostCommentList(array);
            } else {
                // Error
                Log.e(TAG, "GET /posts/:id/comments: failed, error HTTP " + responseCode);
                return null;
            }
        } catch (IOException e) {
            // writing exception to log
            Log.e(TAG, "GET /posts/help: IOException", e);
            return null;
        } catch (JSONException e) {
            Log.e(TAG, "GET /posts/help: JSONException", e);
            return null;
        }
    }
}
