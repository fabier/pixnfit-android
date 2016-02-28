package com.pixnfit.adapter;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.LruCache;

import com.pixnfit.PostFragment;
import com.pixnfit.common.Post;
import com.pixnfit.utils.LRUCache;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;

/**
 * Created by fabier on 27/02/16.
 */
public class PostPagerAdapter extends FragmentPagerAdapter {

    private static final LruCache<Integer, PostFragment> FRAGMENT_CACHE = new LruCache<>(6);

    private final Bitmap postImagePlaceholder;
    private List<Post> posts;

    public PostPagerAdapter(FragmentManager fragmentManager, Bitmap postImagePlaceholder) {
        super(fragmentManager);
        this.postImagePlaceholder = postImagePlaceholder;
    }

    public void setPosts(List<Post> posts) {
        this.posts = posts;
        FRAGMENT_CACHE.evictAll();
    }

    @Override
    public Fragment getItem(int position) {
        PostFragment postFragment = FRAGMENT_CACHE.get(position);
        if (postFragment == null) {
            postFragment = new PostFragment();
            Bundle bundle = new Bundle();
            bundle.putSerializable("post", this.posts.get(position));
            bundle.putParcelable("postImagePlaceholder", postImagePlaceholder);
            postFragment.setArguments(bundle);
            FRAGMENT_CACHE.put(position, postFragment);
        }
        return postFragment;
    }

    @Override
    public int getCount() {
        return this.posts == null ? 0 : this.posts.size();
    }
}
