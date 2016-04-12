package com.pixnfit.fragment;

import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.pixnfit.R;
import com.pixnfit.adapter.PostListAdapter;
import com.pixnfit.common.Post;
import com.pixnfit.common.User;
import com.pixnfit.utils.ThreadPools;
import com.pixnfit.ws.GetUserPostAsyncTask;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.ListUtils;

import java.util.List;

/**
 * Created by fabier on 31/03/16.
 */
public class ProfilePixFragment extends Fragment {

    private User user;
    private GridView gridView;
    private PostListAdapter postListAdapter;
    private LinearLayout pleaseWaitLayout;
    private LinearLayout noResultsLayout;
    private TextView noResultsTextView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_profile_pix, container, false);

        gridView = (GridView) rootView.findViewById(R.id.gridView);
        pleaseWaitLayout = (LinearLayout) rootView.findViewById(R.id.pleaseWaitLayout);
        noResultsLayout = (LinearLayout) rootView.findViewById(R.id.noResultsLayout);
        noResultsTextView = (TextView) rootView.findViewById(R.id.noResultsTextView);
        noResultsTextView.setText(R.string.this_user_hasnt_posted_yet);

        gridView.setVisibility(View.GONE);
        pleaseWaitLayout.setVisibility(View.GONE);
        noResultsLayout.setVisibility(View.GONE);

        postListAdapter = new PostListAdapter(getActivity());
        postListAdapter.setPostImagePlaceHolder(BitmapFactory.decodeResource(getResources(), R.drawable.camera_transparent));
        gridView.setAdapter(postListAdapter);

        return rootView;
    }

    private void refresh() {
        if (postListAdapter != null && user != null) {
            postListAdapter.setPosts(null);

            // On montre qu'on est en train de charger des données...
            gridView.setVisibility(View.GONE);
            pleaseWaitLayout.setVisibility(View.VISIBLE);
            noResultsLayout.setVisibility(View.GONE);

            GetUserPostAsyncTask getUserPostAsyncTask = new GetUserPostAsyncTask(getActivity()) {
                @Override
                protected void onPostExecute(List<Post> posts) {
                    super.onPostExecute(posts);

                    // Recherche terminée...
                    pleaseWaitLayout.setVisibility(View.GONE);

                    if (!isCancelled()) {
                        if (ListUtils.isEqualList(posts, postListAdapter.getPosts())) {
                            // Les deux listes sont identiques, on ne change pas la valeur
                        } else {
                            postListAdapter.addPostsUnique(posts);
                            postListAdapter.notifyDataSetChanged();
                        }

                        if (CollectionUtils.isEmpty(posts)) {
                            // On va afficher qu'il n'y a pas de résultats
                            noResultsLayout.setVisibility(View.VISIBLE);
                        } else {
                            // Il y a des résultats...
                            gridView.setVisibility(View.VISIBLE);
                        }
                    } else {
                        // On a arrêté la recherche...
                        noResultsLayout.setVisibility(View.GONE);
                    }
                }
            };
            getUserPostAsyncTask.executeOnExecutor(ThreadPools.POST_THREADPOOL, getUser());
        }
    }

    public void setUser(User user) {
        this.user = user;
        refresh();
    }

    @Override
    public void onResume() {
        super.onResume();
        refresh();
    }

    public User getUser() {
        return user;
    }
}
