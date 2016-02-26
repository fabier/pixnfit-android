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
        dislikeFloatingActionButton = (FloatingActionButton) findViewById(R.id.fabDislike);
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

            GetPostMeAsyncTask getPostMeAsyncTask = new GetPostMeAsyncTask(this) {
                @Override
                protected void onPostExecute(PostMe postMe) {
                    if (postMe != null && postMe.vote != null) {
                        setHasVoted(postMe.vote.vote);
                    } else {
                        setHasVoted(null);
                    }
                }
            };
            getPostMeAsyncTask.execute(post);
        }
        likeFloatingActionButton.setVisibility(View.GONE);
        dislikeFloatingActionButton.setVisibility(View.GONE);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fabLike:
                new SubmitPostVoteAsyncTask(this, post, true).execute();
                Snackbar.make(v, "Thank you for your vote !", Snackbar.LENGTH_LONG).show();
                setHasVoted(true);
                break;
            case R.id.fabDislike:
                new SubmitPostVoteAsyncTask(this, post, false).execute();
                Snackbar.make(v, "Thank you for your vote !", Snackbar.LENGTH_LONG).show();
                setHasVoted(false);
                break;
            default:
                break;
        }
    }

    private void setHasVoted(Boolean vote) {
        if (vote == null) {
            // L'utilisateur n'a pas voté
            likeFloatingActionButton.setBackgroundTintList(getResources().getColorStateList(R.color.green));
            dislikeFloatingActionButton.setBackgroundTintList(getResources().getColorStateList(R.color.orange));
            likeFloatingActionButton.setOnClickListener(this);
            dislikeFloatingActionButton.setOnClickListener(this);
        } else {
            // L'utilisateur a voté...
            if (vote) {
                // ... positivement
                likeFloatingActionButton.setBackgroundTintList(getResources().getColorStateList(R.color.green));
                dislikeFloatingActionButton.setBackgroundTintList(getResources().getColorStateList(R.color.grey));
            } else {
                // ... négativement
                likeFloatingActionButton.setBackgroundTintList(getResources().getColorStateList(R.color.grey));
                dislikeFloatingActionButton.setBackgroundTintList(getResources().getColorStateList(R.color.orange));
            }
            likeFloatingActionButton.setOnClickListener(null);
            dislikeFloatingActionButton.setOnClickListener(null);
        }
        likeFloatingActionButton.setVisibility(View.VISIBLE);
        dislikeFloatingActionButton.setVisibility(View.VISIBLE);
    }
}
