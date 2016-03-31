package com.pixnfit.fragment;

import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import com.pixnfit.R;
import com.pixnfit.adapter.PostListAdapter;
import com.pixnfit.common.Post;
import com.pixnfit.common.User;
import com.pixnfit.utils.ThreadPools;
import com.pixnfit.ws.GetUserPostAsyncTask;

import org.apache.commons.collections4.ListUtils;

import java.util.List;

/**
 * Created by fabier on 31/03/16.
 */
public class ProfilePixFragment extends Fragment {

    private User user;
    private GridView gridView;
    //    private EndlessScrollListener endlessScrollListener;
    private PostListAdapter postListAdapter;
//    private boolean isLoading;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_profile_pix, container, false);

        gridView = (GridView) rootView.findViewById(R.id.gridView);
        postListAdapter = new PostListAdapter(getActivity());
        postListAdapter.setPostImagePlaceHolder(BitmapFactory.decodeResource(getResources(), R.drawable.camera_transparent));
        gridView.setAdapter(postListAdapter);

        return rootView;
    }

    private void refreshPosts() {
        postListAdapter.setPosts(null);
        GetUserPostAsyncTask getUserPostAsyncTask = new GetUserPostAsyncTask(getActivity()) {
            @Override
            protected void onPostExecute(List<Post> posts) {
                super.onPostExecute(posts);
                if (!isCancelled()) {
                    if (ListUtils.isEqualList(posts, postListAdapter.getPosts())) {
                        // Les deux listes sont identiques, on ne change pas la valeur
                    } else {
                        postListAdapter.addPostsUnique(posts);
                        postListAdapter.notifyDataSetChanged();
                    }
                }
            }
        };
        getUserPostAsyncTask.executeOnExecutor(ThreadPools.POST_THREADPOOL, getUser());
    }

    public void setUser(User user) {
        this.user = user;
        updateView();
    }

    @Override
    public void onResume() {
        super.onResume();
        updateView();
    }

    private void updateView() {
        if (user == null) {
        } else {
            refreshPosts();
        }
    }

    public User getUser() {
        return user;
    }
}
