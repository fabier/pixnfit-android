package com.pixnfit.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.pixnfit.common.User;
import com.pixnfit.fragment.ProfileAboutFragment;
import com.pixnfit.fragment.ProfileFollowersFragment;
import com.pixnfit.fragment.ProfilePixFragment;
import com.pixnfit.fragment.ProfileFollowingFragment;

/**
 * Created by fabier on 31/03/16.
 */
public class ProfilePagerAdapter extends FragmentPagerAdapter {

    // All Fragments
    private final ProfileAboutFragment profileAboutFragment;
    private final ProfilePixFragment profilePixFragment;
    private final ProfileFollowersFragment profileFollowersFragment;
    private final ProfileFollowingFragment profileFollowingFragment;

    private User user;

    public ProfilePagerAdapter(FragmentManager fm) {
        super(fm);
        this.profileAboutFragment = new ProfileAboutFragment();
        this.profilePixFragment = new ProfilePixFragment();
        this.profileFollowersFragment = new ProfileFollowersFragment();
        this.profileFollowingFragment = new ProfileFollowingFragment();
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return profileAboutFragment;
            case 1:
                return profilePixFragment;
            case 2:
                return profileFollowersFragment;
            case 3:
                return profileFollowingFragment;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return 4;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return "About";
            case 1:
                return "Pix";
            case 2:
                return "Followers";
            case 3:
                return "Following";
            default:
                return "???";
        }
    }

    public void setUser(User user) {
        this.user = user;
        profileAboutFragment.setUser(user);
        profilePixFragment.setUser(user);
        profileFollowersFragment.setUser(user);
        profileFollowingFragment.setUser(user);
    }
}
