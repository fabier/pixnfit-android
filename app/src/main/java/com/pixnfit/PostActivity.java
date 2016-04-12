package com.pixnfit;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageButton;

import com.pixnfit.adapter.PostPagerAdapter;
import com.pixnfit.common.Post;
import com.pixnfit.common.User;

import java.util.List;

/**
 * Created by fabier on 27/02/16.
 */
public class PostActivity extends AppCompatActivity implements View.OnClickListener, ViewPager.OnPageChangeListener {

    private static final int REQUESTCODE_CREATE_POST = 1;

    private ViewPager viewPager;
    private PostPagerAdapter postPagerAdapter;
    private List<Post> posts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Instantiate a ViewPager and a PagerAdapter.
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        Bitmap postImagePlaceholder = BitmapFactory.decodeResource(getResources(), R.drawable.camera_transparent);
        postPagerAdapter = new PostPagerAdapter(getSupportFragmentManager(), postImagePlaceholder);
        viewPager.addOnPageChangeListener(this);
        viewPager.setAdapter(postPagerAdapter);

        Intent intent = getIntent();
        intent.setExtrasClassLoader(Post.class.getClassLoader());
        intent.setExtrasClassLoader(User.class.getClassLoader());
        this.posts = (List<Post>) intent.getSerializableExtra("posts");
        int position = intent.getIntExtra("position", 0);

        postPagerAdapter.setPosts(posts);
        postPagerAdapter.notifyDataSetChanged();

        viewPager.setCurrentItem(position);

        ImageButton cameraButton = (ImageButton) findViewById(R.id.cameraButtonBar);
        cameraButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.cameraButtonBar:
                Intent createPostIntent = new Intent(this, CreatePostActivity.class);
                startActivityForResult(createPostIntent, REQUESTCODE_CREATE_POST);
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
                        // ...et on ajoute le post nouvellement créé
                        postPagerAdapter.addFirstToPosts(post);
                        postPagerAdapter.notifyDataSetChanged();
                        // ... on affiche ce post en plein écran
                        viewPager.setCurrentItem(0);
                    }
                    break;
                default:
                    break;
            }
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        CharSequence title = this.postPagerAdapter.getPageTitle(position);
        ActionBar supportActionBar = getSupportActionBar();
        if (supportActionBar != null) {
            supportActionBar.setTitle(title);
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}
