package com.pixnfit;

import android.content.res.ColorStateList;
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
import com.pixnfit.ws.GetPostCommentsAsyncTask;
import com.pixnfit.ws.SubmitPostVoteAsyncTask;

import java.util.List;

public class PostActivity extends AppCompatActivity implements View.OnClickListener {

    ImageView postImageView;
    ImageButton postButtonHeart;
    ImageButton postButtonComments;
    ImageButton postButtonShare;
    ImageButton postButtonHanger;
    ImageButton postButtonMoreOptions;
    TextView postTitleTextView;
    TextView postTitleViewCountTextView;
    PostCommentsListAdapter postCommentsListAdapter;
    FloatingActionButton likeFloatingActionButton;
    FloatingActionButton dislikeFloatingActionButton;

    Post post;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);

        ListView postCommentsListView = (ListView) findViewById(R.id.postCommentsListView);
        postCommentsListAdapter = new PostCommentsListAdapter(this);
        postCommentsListView.setAdapter(postCommentsListAdapter);

        likeFloatingActionButton = (FloatingActionButton) findViewById(R.id.fabLike);
        likeFloatingActionButton.setOnClickListener(this);
        dislikeFloatingActionButton = (FloatingActionButton) findViewById(R.id.fabDislike);
        dislikeFloatingActionButton.setOnClickListener(this);
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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fabLike:
                new SubmitPostVoteAsyncTask(this, post, true).execute();
                Snackbar.make(v, "Thank you for your vote !", Snackbar.LENGTH_LONG).show();
                dislikeFloatingActionButton.setBackgroundTintList(getResources().getColorStateList(R.color.grey));
//                likeFloatingActionButton.setOnClickListener(null);
//                dislikeFloatingActionButton.setOnClickListener(null);
                break;
            case R.id.fabDislike:
                new SubmitPostVoteAsyncTask(this, post, false).execute();
                Snackbar.make(v, "Thank you for your vote !", Snackbar.LENGTH_LONG).show();
                likeFloatingActionButton.setBackgroundTintList(getResources().getColorStateList(R.color.grey));
//                likeFloatingActionButton.setOnClickListener(null);
//                dislikeFloatingActionButton.setOnClickListener(null);
                break;
            default:
                break;
        }
    }
}
