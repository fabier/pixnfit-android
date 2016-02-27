package com.pixnfit;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.pixnfit.adapter.PostCommentsListAdapter;
import com.pixnfit.common.Post;
import com.pixnfit.common.PostComment;
import com.pixnfit.common.PostMe;
import com.pixnfit.ws.GetPostCommentsAsyncTask;
import com.pixnfit.ws.GetPostMeAsyncTask;
import com.pixnfit.ws.SubmitPostVoteAsyncTask;

import java.util.List;

public class PostActivity extends AppCompatActivity  {

    ImageView postImageView;
    ImageButton postButtonHeart;
    ImageButton postButtonComments;
    ImageButton postButtonShare;
    ImageButton postButtonHanger;
    ImageButton postButtonMoreOptions;
    TextView postTitleTextView;
    TextView postTitleViewCountTextView;
    PostCommentsListAdapter postCommentsListAdapter;

    Post post;

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
        post = (Post) getIntent().getSerializableExtra("post");
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
