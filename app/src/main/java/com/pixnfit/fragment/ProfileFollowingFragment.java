package com.pixnfit.fragment;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.pixnfit.ProfileActivity;
import com.pixnfit.R;
import com.pixnfit.adapter.UserListAdapter;
import com.pixnfit.common.User;
import com.pixnfit.utils.ThreadPools;
import com.pixnfit.ws.GetUserFollowingAsyncTask;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.ListUtils;

import java.util.List;

/**
 * Created by fabier on 31/03/16.
 */
public class ProfileFollowingFragment extends Fragment implements AdapterView.OnItemClickListener {

    private User user;
    private GridView gridView;
    private UserListAdapter userListAdapter;
    private LinearLayout pleaseWaitLayout;
    private LinearLayout noResultsLayout;
    private TextView noResultsTextView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_profile_follows, container, false);

        gridView = (GridView) rootView.findViewById(R.id.gridView);
        pleaseWaitLayout = (LinearLayout) rootView.findViewById(R.id.pleaseWaitLayout);
        noResultsLayout = (LinearLayout) rootView.findViewById(R.id.noResultsLayout);
        noResultsTextView = (TextView) rootView.findViewById(R.id.noResultsTextView);
        noResultsTextView.setText(R.string.this_user_doesnt_follow_anyone);

        gridView.setVisibility(View.GONE);
        pleaseWaitLayout.setVisibility(View.GONE);
        noResultsLayout.setVisibility(View.GONE);

        userListAdapter = new UserListAdapter(getActivity());
        userListAdapter.setUserImagePlaceHolder(BitmapFactory.decodeResource(getResources(), R.drawable.profile));
        gridView.setAdapter(userListAdapter);

        gridView.setOnItemClickListener(this);

        return rootView;
    }

    private void refresh() {
        if (userListAdapter != null && user != null) {
            userListAdapter.setUsers(null);

            // On montre qu'on est en train de charger des données...
            gridView.setVisibility(View.GONE);
            pleaseWaitLayout.setVisibility(View.VISIBLE);
            noResultsLayout.setVisibility(View.GONE);

            GetUserFollowingAsyncTask getUserFollowingAsyncTask = new GetUserFollowingAsyncTask(getActivity()) {
                @Override
                protected void onPostExecute(List<User> users) {
                    super.onPostExecute(users);

                    // Recherche terminée...
                    pleaseWaitLayout.setVisibility(View.GONE);

                    if (!isCancelled()) {
                        if (ListUtils.isEqualList(users, userListAdapter.getUsers())) {
                            // Les deux listes sont identiques, on ne change pas la valeur
                        } else {
                            userListAdapter.addUsersUnique(users);
                            userListAdapter.notifyDataSetChanged();
                        }

                        if (CollectionUtils.isEmpty(users)) {
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
            getUserFollowingAsyncTask.executeOnExecutor(ThreadPools.POST_THREADPOOL, getUser());
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

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent profileIntent = new Intent(getActivity(), ProfileActivity.class);
        profileIntent.putExtra("user", userListAdapter.getUser(position));
        startActivity(profileIntent);
    }
}
