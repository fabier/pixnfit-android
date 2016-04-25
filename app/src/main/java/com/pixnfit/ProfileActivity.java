package com.pixnfit;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.pixnfit.adapter.ProfilePagerAdapter;
import com.pixnfit.async.BitmapWorkerTask;
import com.pixnfit.common.Post;
import com.pixnfit.common.User;
import com.pixnfit.common.UserMe;
import com.pixnfit.utils.ThreadPools;
import com.pixnfit.ws.FollowUserAsyncTask;
import com.pixnfit.ws.GetMeAsyncTask;
import com.pixnfit.ws.GetUserAsyncTask;
import com.pixnfit.ws.GetUserMeAsyncTask;
import com.pixnfit.ws.UnfollowUserAsyncTask;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Locale;

/**
 * Created by fabier on 31/03/16.
 */
public class ProfileActivity extends AppCompatActivity implements View.OnClickListener {

    private static final int REQUESTCODE_CREATE_POST = 1;

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

        initFooterButtonBar();
    }

    private void initFooterButtonBar() {
        ImageButton homeButtonBar = (ImageButton) findViewById(R.id.homeButtonBar);
        homeButtonBar.setOnClickListener(this);
        ImageButton followersButtonBar = (ImageButton) findViewById(R.id.followersButtonBar);
        followersButtonBar.setOnClickListener(this);
        ImageButton cameraButtonBar = (ImageButton) findViewById(R.id.cameraButtonBar);
        cameraButtonBar.setOnClickListener(this);
        ImageButton inboxButtonBar = (ImageButton) findViewById(R.id.inboxButtonBar);
        inboxButtonBar.setOnClickListener(this);
        ImageButton profileButtonBar = (ImageButton) findViewById(R.id.profileButtonBar);
        profileButtonBar.setOnClickListener(this);

        // Set none as selected
        profileButtonBar.setBackgroundColor(getResources().getColor(R.color.colorAccent));
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
    public void onClick(final View v) {
        switch (v.getId()) {
            case R.id.fabFollow:
                if (this.follows) {
                    new UnfollowUserAsyncTask(this, user) {
                        @Override
                        protected void onPostExecute(Boolean result) {
                            super.onPostExecute(result);
                            if (result != null && result) {
                                Snackbar.make(v, "You don't follow this user anymore", Snackbar.LENGTH_LONG).show();
                                user.followersCount--;
                                profileFollowersTextView.setText(String.format(Locale.ENGLISH, "%d", user.followersCount));
                                setFollows(!follows);
                            } else {
                                Snackbar.make(v, "An error occured", Snackbar.LENGTH_LONG).show();
                            }
                        }
                    }.execute();
                } else {
                    new FollowUserAsyncTask(this, user) {
                        @Override
                        protected void onPostExecute(Boolean result) {
                            super.onPostExecute(result);
                            if (result != null && result) {
                                Snackbar.make(v, "Now you follow this user", Snackbar.LENGTH_LONG).show();
                                user.followersCount++;
                                profileFollowersTextView.setText(String.format(Locale.ENGLISH, "%d", user.followersCount));
                                setFollows(!follows);
                            } else {
                                Snackbar.make(v, "An error occured", Snackbar.LENGTH_LONG).show();
                            }
                        }
                    }.execute();
                }
                break;
            case R.id.homeButtonBar:
                Intent homeIntent = new Intent(this, HomeActivity.class);
                startActivity(homeIntent);
                break;
            case R.id.followersButtonBar:
                Intent followersIntent = new Intent(this, FollowersActivity.class);
                startActivity(followersIntent);
                break;
            case R.id.cameraButtonBar:
                Intent createPostIntent = new Intent(this, CreatePostActivity.class);
                startActivityForResult(createPostIntent, REQUESTCODE_CREATE_POST);
                break;
            case R.id.inboxButtonBar:
                Intent inboxIntent = new Intent(this, InboxActivity.class);
                startActivity(inboxIntent);
                break;
            case R.id.profileButtonBar:
                // already there
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
                        // .. puis on affiche ce post en plein écran
                        Intent intent = new Intent(this, PostActivity.class);
                        intent.putExtra("posts", (Serializable) Arrays.asList(post));
                        intent.putExtra("position", 0);
                        startActivity(intent);
                    }
                    break;
                default:
                    break;
            }
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
