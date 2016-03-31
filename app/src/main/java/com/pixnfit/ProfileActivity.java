package com.pixnfit;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;

import com.pixnfit.adapter.ProfilePagerAdapter;

/**
 * Created by fabier on 31/03/16.
 */
public class ProfileActivity extends FragmentActivity {
    // When requested, this adapter returns a DemoObjectFragment,
    // representing an object in the collection.
    ProfilePagerAdapter profilePagerAdapter;
    ViewPager profileViewPager;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

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
}
