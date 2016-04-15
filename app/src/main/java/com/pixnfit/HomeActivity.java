package com.pixnfit;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.pixnfit.adapter.PostListAdapter;
import com.pixnfit.common.Post;
import com.pixnfit.ui.EndlessScrollListener;
import com.pixnfit.utils.ThreadPools;
import com.pixnfit.ws.GetPostAsyncTask;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.ListUtils;

import java.io.Serializable;
import java.util.List;

public class HomeActivity extends AppCompatActivity implements AdapterView.OnItemClickListener, View.OnClickListener {

    private static final int REQUESTCODE_CREATE_POST = 1;

    private PostListAdapter postListAdapter;
    private boolean isLoading = true;
    private EndlessScrollListener endlessScrollListener;
    private GridView gridView;

    private RelativeLayout homeLayout;
    private LinearLayout pleaseWaitLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        homeLayout = (RelativeLayout) findViewById(R.id.homeLayout);
        pleaseWaitLayout = (LinearLayout) findViewById(R.id.pleaseWaitLayout);

        homeLayout.setVisibility(View.GONE);
        pleaseWaitLayout.setVisibility(View.VISIBLE);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ImageButton cameraButton = (ImageButton) findViewById(R.id.cameraButtonBar);
        cameraButton.setOnClickListener(this);
        ImageButton profileButtonBar = (ImageButton) findViewById(R.id.profileButtonBar);
        profileButtonBar.setOnClickListener(this);

        gridView = (GridView) findViewById(R.id.gridView);
        endlessScrollListener = new EndlessScrollListener(4) {
            @Override
            public boolean onLoadMore(int page, int totalItemsCount) {
                return loadPage(page, totalItemsCount);
            }
        };
        gridView.setOnScrollListener(endlessScrollListener);
        postListAdapter = new PostListAdapter(this);
        postListAdapter.setPostImagePlaceHolder(BitmapFactory.decodeResource(getResources(), R.drawable.camera_transparent));
        gridView.setAdapter(postListAdapter);
        gridView.setOnItemClickListener(this);

        refreshPosts();
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
            case R.id.cameraButtonBar:
                Intent createPostIntent = new Intent(this, CreatePostActivity.class);
                startActivityForResult(createPostIntent, REQUESTCODE_CREATE_POST);
                break;
            case R.id.profileButtonBar:
                Intent profileIntent = new Intent(this, ProfileActivity.class);
                startActivity(profileIntent);
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
                    Post post = (Post) data.getSerializableExtra("post");
                    if (post != null) {
                        // On revient en haut...
                        gridView.smoothScrollToPosition(0);
                        // ...et on ajoute le post nouvellement créé
                        postListAdapter.addNewPost(post);
                        postListAdapter.notifyDataSetChanged();
                        // .. puis on affiche ce post en plein écran
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_refresh:
                // User chose the "Refresh" item
                refreshPosts();
                return true;
            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }

    private void refreshPosts() {
        homeLayout.setVisibility(View.GONE);
        pleaseWaitLayout.setVisibility(View.VISIBLE);
        
        isLoading = true;
        endlessScrollListener.reset();
        postListAdapter.setPosts(null);
        loadPage(0, 0);
    }

    private boolean loadPage(int page, int totalItemsCount) {
        GetPostAsyncTask getPostAsyncTask = new GetPostAsyncTask(this) {
            @Override
            protected void onPostExecute(List<Post> posts) {
                super.onPostExecute(posts);
                homeLayout.setVisibility(View.VISIBLE);
                pleaseWaitLayout.setVisibility(View.GONE);
                if (!isCancelled()) {
                    isLoading = CollectionUtils.isNotEmpty(posts);
                    if (ListUtils.isEqualList(posts, postListAdapter.getPosts())) {
                        // Les deux listes sont identiques, on ne change pas la valeur
                    } else {
                        postListAdapter.addPostsUnique(posts);
                        postListAdapter.notifyDataSetChanged();
                    }
                }
            }
        };
        getPostAsyncTask.executeOnExecutor(ThreadPools.POST_THREADPOOL, page);
        return isLoading;
    }
}
