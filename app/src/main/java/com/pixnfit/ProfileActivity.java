package com.pixnfit;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.pixnfit.adapter.ProfilePagerAdapter;
import com.pixnfit.async.BitmapWorkerTask;
import com.pixnfit.common.User;
import com.pixnfit.common.UserMe;
import com.pixnfit.utils.ThreadPools;
import com.pixnfit.ws.FollowUserAsyncTask;
import com.pixnfit.ws.GetMeAsyncTask;
import com.pixnfit.ws.GetUserAsyncTask;
import com.pixnfit.ws.GetUserMeAsyncTask;
import com.pixnfit.ws.UnfollowUserAsyncTask;

/**
 * Created by fabier on 31/03/16.
 */
public class ProfileActivity extends AppCompatActivity implements View.OnClickListener {
    // When requested, this adapter returns a DemoObjectFragment,
    // representing an object in the collection.
    ProfilePagerAdapter profilePagerAdapter;
    ViewPager profileViewPager;

    private User user;
    private ImageView profileImageView;
    private TextView profileRankingTextView;
    private TextView profilePicturesTextView;
    private TextView profileFollowersTextView;
    private TextView profileFollowingTextView;
    private FloatingActionButton fabFollow;
    private boolean follows;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        this.profileImageView = (ImageView) findViewById(R.id.profileImageView);
        this.profileRankingTextView = (TextView) findViewById(R.id.profileRankingTextView);
        this.profilePicturesTextView = (TextView) findViewById(R.id.profilePicturesTextView);
        this.profileFollowersTextView = (TextView) findViewById(R.id.profileFollowersTextView);
        this.profileFollowingTextView = (TextView) findViewById(R.id.profileFollowingTextView);
        this.fabFollow = (FloatingActionButton) findViewById(R.id.fabFollow);
        this.fabFollow.setVisibility(View.GONE);

        // ViewPager and its adapters use support library
        // fragments, so use getSupportFragmentManager.
        profilePagerAdapter = new ProfilePagerAdapter(getSupportFragmentManager());
        profileViewPager = (ViewPager) findViewById(R.id.profileViewPager);
        profileViewPager.setAdapter(profilePagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);

        for (int index = 0; index < profilePagerAdapter.getCount(); index++) {
            tabLayout.addTab(tabLayout.newTab().setText(profilePagerAdapter.getPageTitle(index)));
        }
        tabLayout.setTabGravity(TabLayout.GRAVITY_CENTER);
        tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);

        profileViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                profileViewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        User user = (User) getIntent().getSerializableExtra("user");

        if (user != null) {
            GetUserAsyncTask getUserAsyncTask = new GetUserAsyncTask(getApplication()) {
                @Override
                protected void onPostExecute(User user) {
                    super.onPostExecute(user);
                    if (!isCancelled()) {
                        setUser(user);
                    }
                }
            };
            getUserAsyncTask.executeOnExecutor(ThreadPools.METADATA_THREADPOOL, user);
            loadUserMe(user);
        } else {
            GetMeAsyncTask getMeAsyncTask = new GetMeAsyncTask(getApplication()) {
                @Override
                protected void onPostExecute(User user) {
                    super.onPostExecute(user);
                    if (!isCancelled()) {
                        setUser(user);
                    }
                }
            };
            getMeAsyncTask.executeOnExecutor(ThreadPools.METADATA_THREADPOOL);
        }
    }

    protected void setUser(User user) {
        this.user = user;
        loadProfileImageView(user, this.profileImageView);

        if (this.user == null) {
            this.getSupportActionBar().setTitle("User");
            this.profileRankingTextView.setText("0 pts");
            this.profilePicturesTextView.setText("0");
            this.profileFollowersTextView.setText("0");
            this.profileFollowingTextView.setText("0");

            // On désactive le bouton pour suivre l'utilisateur
            this.fabFollow.setVisibility(View.GONE);
            this.fabFollow.setOnClickListener(null);
        } else {
            this.getSupportActionBar().setTitle(user.username);
            this.profileRankingTextView.setText(Integer.toString(user.points) + " pts");
            this.profilePicturesTextView.setText(Integer.toString(user.postCount));
            this.profileFollowersTextView.setText(Integer.toString(user.followersCount));
            this.profileFollowingTextView.setText(Integer.toString(user.followedCount));

            // On active le bouton pour suivre l'utilisateur
            this.fabFollow.setVisibility(View.VISIBLE);
            this.fabFollow.setOnClickListener(this);
        }

        this.profilePagerAdapter.setUser(user);
    }

    private void loadUserMe(User user) {
        GetUserMeAsyncTask getUserMeAsyncTask = new GetUserMeAsyncTask(this) {
            @Override
            protected void onPostExecute(UserMe userMe) {
                super.onPostExecute(userMe);
                if (!isCancelled() && userMe != null) {
                    if (userMe.meFollows != null) {
                        setFollows(userMe.meFollows);
                    }
                }
            }
        };
        getUserMeAsyncTask.execute(user);
    }

    private void loadProfileImageView(User user, ImageView profileImageView) {
        if (user == null || user.image == null) {
            profileImageView.setTag(R.id.tagImageUrl, null);
            profileImageView.setImageResource(R.drawable.profile);
        } else {
            profileImageView.setTag(R.id.tagImageUrl, user.image.imageUrl);
            BitmapWorkerTask bitmapWorkerTask = new BitmapWorkerTask(profileImageView, 64, 64);
            bitmapWorkerTask.execute(user.image.imageUrl);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fabFollow:
                if (this.follows) {
                    new UnfollowUserAsyncTask(this, user).execute();
                    user.followersCount--;
                    this.profileFollowersTextView.setText(Integer.toString(user.followersCount));
                    Snackbar.make(v, "You don't follow this user anymore", Snackbar.LENGTH_LONG).show();
                    setFollows(true);
                } else {
                    new FollowUserAsyncTask(this, user).execute();
                    user.followersCount++;
                    this.profileFollowersTextView.setText(Integer.toString(user.followersCount));
                    Snackbar.make(v, "Now you follow this user", Snackbar.LENGTH_LONG).show();
                }
                setFollows(!this.follows);
                break;
            default:
                break;
        }
    }

    public void setFollows(boolean follows) {
        this.follows = follows;
        if (follows) {
            this.fabFollow.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_done_white_24dp));
            this.fabFollow.setBackgroundTintList(getResources().getColorStateList(R.color.green));
        } else {
            this.fabFollow.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_add_white_24dp));
            this.fabFollow.setBackgroundTintList(getResources().getColorStateList(R.color.red));
        }
    }
}
