package com.pixnfit.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.pixnfit.R;
import com.pixnfit.common.User;

/**
 * Created by fabier on 31/03/16.
 */
public class ProfilePixFragment extends Fragment {

    private User user;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_profile_pix, container, false);
        return rootView;
    }

    public void setUser(User user) {
        this.user = user;
        updateView();
    }
}
