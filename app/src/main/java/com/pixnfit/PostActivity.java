package com.pixnfit;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.pixnfit.adapter.BitmapWorkerTask;
import com.pixnfit.adapter.PostCommentsListAdapter;
import com.pixnfit.common.Image;
import com.pixnfit.common.Post;
import com.pixnfit.common.PostComment;
import com.pixnfit.ws.GetPostAsyncTask;
import com.pixnfit.ws.GetPostCommentsAsyncTask;

import java.util.List;

public class PostActivity extends AppCompatActivity {

    ImageView postImageView;
    ImageButton postButtonHeart;
    ImageButton postButtonComments;
    ImageButton postButtonShare;
    ImageButton postButtonHanger;
    ImageButton postButtonMoreOptions;
    TextView postTitleTextView;
    TextView postTitleViewCountTextView;
    PostCommentsListAdapter postCommentsListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);

        ListView postCommentsListView = (ListView) findViewById(R.id.postCommentsListView);
        postCommentsListAdapter = new PostCommentsListAdapter(this);
        postCommentsListView.setAdapter(postCommentsListAdapter);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Post post = (Post) getIntent().getSerializableExtra("post");
        if (post != null) {
            postCommentsListAdapter.setPost(post);
            GetPostCommentsAsyncTask getPostCommentsAsyncTask = new GetPostCommentsAsyncTask(this) {
                @Override
                protected void onPostExecute(List<PostComment> postComments) {
                    postCommentsListAdapter.setPostComments(postComments);
                    postCommentsListAdapter.notifyDataSetChanged();
                }
            };
            getPostCommentsAsyncTask.execute(post);
        }
    }
}
