package com.pixnfit;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import com.pixnfit.adapter.PostListAdapter;
import com.pixnfit.common.Post;
import com.pixnfit.ws.GetPostAsyncTask;

import org.apache.commons.collections4.CollectionUtils;

import java.io.Serializable;
import java.util.List;

public class HomeActivity extends AppCompatActivity implements AdapterView.OnItemClickListener, View.OnClickListener {

    private static final int REQUESTCODE_CREATE_POST = 1;
    private PostListAdapter postListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fabCamera);
        fab.setOnClickListener(this);

        GridView gridView = (GridView) findViewById(R.id.gridView);
        postListAdapter = new PostListAdapter(this);
        postListAdapter.setPostImagePlaceHolder(BitmapFactory.decodeResource(getResources(), R.drawable.camera_transparent));
        gridView.setAdapter(postListAdapter);
        gridView.setOnItemClickListener(this);

        GetPostAsyncTask getPostAsyncTask = new GetPostAsyncTask(this) {
            @Override
            protected void onPostExecute(List<Post> posts) {
                if (!isCancelled()) {
                    postListAdapter.setPosts(posts);
                    postListAdapter.notifyDataSetChanged();
                }
            }
        };
        getPostAsyncTask.execute();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(this, PostActivity.class);
        intent.putExtra("posts", (Serializable) postListAdapter.getPosts());
        intent.putExtra("position", position);
        startActivity(intent);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fabCamera:
                Intent intent = new Intent(this, CreatePostActivity.class);
                startActivityForResult(intent, REQUESTCODE_CREATE_POST);
                break;
            default:
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case REQUESTCODE_CREATE_POST:
                    List<Post> posts = (List<Post>) data.getSerializableExtra("posts");
                    if (CollectionUtils.isNotEmpty(posts)) {
                        postListAdapter.addNewPosts(posts);
                        postListAdapter.notifyDataSetChanged();
                        Intent intent = new Intent(this, PostActivity.class);
                        intent.putExtra("posts", (Serializable) postListAdapter.getPosts());
                        intent.putExtra("position", 0);
                        startActivity(intent);
                    }
                    break;
                default:
                    break;
            }
        }
    }
}
