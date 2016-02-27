package com.pixnfit.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.pixnfit.PostFragment;
import com.pixnfit.common.Post;

import java.util.List;

/**
 * Created by fabier on 27/02/16.
 */
public class PostPagerAdapter extends FragmentPagerAdapter {
    private List<Post> posts;

    public PostPagerAdapter(FragmentManager fragmentManager) {
        super(fragmentManager);
    }

    public void setPosts(List<Post> posts) {
        this.posts = posts;
    }

    @Override
    public Fragment getItem(int position) {
        return new PostFragment(this.posts.get(position));
    }

    @Override
    public int getCount() {
        return this.posts == null ? 0 : this.posts.size();
    }
}
