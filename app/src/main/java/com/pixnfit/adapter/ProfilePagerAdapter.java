package com.pixnfit.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.pixnfit.common.User;
import com.pixnfit.fragment.ProfileAboutFragment;
import com.pixnfit.fragment.ProfilePixFragment;

/**
 * Created by fabier on 31/03/16.
 */
public class ProfilePagerAdapter extends FragmentPagerAdapter {
    private final ProfileAboutFragment profileAboutFragment;
    private final ProfilePixFragment profilePixFragment;
    private User user;

    public ProfilePagerAdapter(FragmentManager fm) {
        super(fm);
        this.profileAboutFragment = new ProfileAboutFragment();
        this.profilePixFragment = new ProfilePixFragment();
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return profileAboutFragment;
            case 1:
                return profilePixFragment;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return "About";
            case 1:
                return "Pix";
            default:
                return "???";
        }
    }

    public void setUser(User user) {
        this.user = user;
        profileAboutFragment.setUser(user);
        profilePixFragment.setUser(user);
    }
}
