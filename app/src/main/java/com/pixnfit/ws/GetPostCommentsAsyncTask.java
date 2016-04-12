package com.pixnfit.ws;

import android.content.Context;

import com.pixnfit.common.Post;
import com.pixnfit.common.PostComment;
import com.pixnfit.ws.tasks.WsGetAsyncTask;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.List;
import java.util.Locale;

/**
 * Created by fabier on 16/02/16.
 */
public class GetPostCommentsAsyncTask extends WsGetAsyncTask<Post, PostComment, List<PostComment>> {

    public GetPostCommentsAsyncTask(Context context) {
        super(context);
    }

    @Override
    protected String getUrl(Post... params) {
        Post post = params[0];
        return String.format(Locale.ENGLISH, "/posts/%d/comments", post.id);
    }

    @Override
    protected List<PostComment> toResult(String dataAsJSON) throws JSONException {
        JSONArray array = new JSONArray(dataAsJSON);
        return JSONWsParser.parsePostCommentList(array);
    }
}
