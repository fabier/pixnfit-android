package com.pixnfit;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;
import android.widget.TextView;

import com.pixnfit.adapter.ProfilePagerAdapter;
import com.pixnfit.async.BitmapWorkerTask;
import com.pixnfit.common.User;
import com.pixnfit.utils.ThreadPools;
import com.pixnfit.ws.GetMeAsyncTask;

/**
 * Created by fabier on 31/03/16.
 */
public class ProfileActivity extends AppCompatActivity {
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

        // ViewPager and its adapters use support library
        // fragments, so use getSupportFragmentManager.
        profilePagerAdapter = new ProfilePagerAdapter(getSupportFragmentManager());
        profileViewPager = (ViewPager) findViewById(R.id.profileViewPager);
        profileViewPager.setAdapter(profilePagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        tabLayout.addTab(tabLayout.newTab().setText("ABOUT"));
        tabLayout.addTab(tabLayout.newTab().setText("PIX"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

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
    }


    @Override
    protected void onResume() {
        super.onResume();
        User user = (User) getIntent().getSerializableExtra("user");
        if (user != null) {
            setUser(user);
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
            this.profileRankingTextView.setText("");
            this.profilePicturesTextView.setText("");
            this.profileFollowersTextView.setText("");
            this.profileFollowingTextView.setText("");
        } else {
            this.profileRankingTextView.setText(user.points);
            this.profilePicturesTextView.setText(user.postCount);
            this.profileFollowersTextView.setText(user.followersCount);
            this.profileFollowingTextView.setText(user.followedCount);
        }

        this.profilePagerAdapter.setUser(user);
        this.profilePagerAdapter.notifyDataSetChanged();
    }

    private void loadProfileImageView(User user, ImageView profileImageView) {
        profileImageView.setTag(R.id.tagImageUrl, user.image.imageUrl);
        BitmapWorkerTask bitmapWorkerTask = new BitmapWorkerTask(profileImageView, 64, 64);
        bitmapWorkerTask.execute(user.image.imageUrl);
    }
}
