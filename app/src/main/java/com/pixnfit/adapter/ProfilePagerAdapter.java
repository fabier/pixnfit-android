package com.pixnfit.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.pixnfit.fragment.ProfileAboutFragment;
import com.pixnfit.fragment.ProfilePixFragment;

/**
 * Created by fabier on 31/03/16.
 */
public class ProfilePagerAdapter extends FragmentPagerAdapter {
    public ProfilePagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new ProfileAboutFragment();
            case 1:
                return new ProfilePixFragment();
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
}
