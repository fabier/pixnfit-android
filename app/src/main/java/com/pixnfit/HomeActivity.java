package com.pixnfit;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import com.pixnfit.adapter.PostListAdapter;
import com.pixnfit.common.Post;
import com.pixnfit.ws.GetPostAsyncTask;

import java.util.List;

public class HomeActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    private PostListAdapter postListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        GridView gridView = (GridView) findViewById(R.id.gridView);
        postListAdapter = new PostListAdapter(this);
        gridView.setAdapter(postListAdapter);
        gridView.setOnItemClickListener(this);

        GetPostAsyncTask getPostAsyncTask = new GetPostAsyncTask(this) {
            @Override
            protected void onPostExecute(List<Post> posts) {
                postListAdapter.setPosts(posts);
                postListAdapter.notifyDataSetChanged();
            }
        };
        getPostAsyncTask.execute();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Post post = postListAdapter.getPost(position);
        Intent intent = new Intent(this, PostActivity.class);
        intent.putExtra("post", post);
        startActivity(intent);
    }
}
