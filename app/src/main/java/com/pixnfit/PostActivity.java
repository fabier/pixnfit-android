package com.pixnfit;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;

import com.pixnfit.adapter.PostPagerAdapter;
import com.pixnfit.common.Post;
import com.pixnfit.common.User;

import java.util.List;

/**
 * Created by fabier on 27/02/16.
 */
public class PostActivity extends FragmentActivity {

    private ViewPager viewPager;
    private PostPagerAdapter postPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);

        // Instantiate a ViewPager and a PagerAdapter.
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        Bitmap postImagePlaceholder = BitmapFactory.decodeResource(getResources(), R.drawable.camera_transparent);
        postPagerAdapter = new PostPagerAdapter(getSupportFragmentManager(), postImagePlaceholder);
        viewPager.setAdapter(postPagerAdapter);

        Intent intent = getIntent();
        intent.setExtrasClassLoader(Post.class.getClassLoader());
        intent.setExtrasClassLoader(User.class.getClassLoader());
        List<Post> posts = (List<Post>) intent.getSerializableExtra("posts");
        int position = intent.getIntExtra("position", 0);

        postPagerAdapter.setPosts(posts);
        postPagerAdapter.notifyDataSetChanged();

        viewPager.setCurrentItem(position);
    }
}
