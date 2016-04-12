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
import com.pixnfit.adapter.UserListAdapter;
import com.pixnfit.common.User;
import com.pixnfit.utils.ThreadPools;
import com.pixnfit.ws.GetUserFollowingAsyncTask;

import org.apache.commons.collections4.ListUtils;

import java.util.List;

/**
 * Created by fabier on 31/03/16.
 */
public class ProfileFollowingFragment extends Fragment {

    private User user;
    private GridView gridView;
    private UserListAdapter userListAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_profile_pix, container, false);

        gridView = (GridView) rootView.findViewById(R.id.gridView);
        userListAdapter = new UserListAdapter(getActivity());
        userListAdapter.setUserImagePlaceHolder(BitmapFactory.decodeResource(getResources(), R.drawable.profile));
        gridView.setAdapter(userListAdapter);

        return rootView;
    }

    private void refreshPosts() {
        if (userListAdapter != null) {
            userListAdapter.setUsers(null);
            GetUserFollowingAsyncTask getUserFollowingAsyncTask = new GetUserFollowingAsyncTask(getActivity()) {
                @Override
                protected void onPostExecute(List<User> users) {
                    super.onPostExecute(users);
                    if (!isCancelled()) {
                        if (ListUtils.isEqualList(users, userListAdapter.getUsers())) {
                            // Les deux listes sont identiques, on ne change pas la valeur
                        } else {
                            userListAdapter.addUsersUnique(users);
                            userListAdapter.notifyDataSetChanged();
                        }
                    }
                }
            };
            getUserFollowingAsyncTask.executeOnExecutor(ThreadPools.POST_THREADPOOL, getUser());
        }
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
