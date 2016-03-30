package com.pixnfit;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageButton;

import com.pixnfit.adapter.PostPagerAdapter;
import com.pixnfit.common.Post;
import com.pixnfit.common.User;

import org.apache.commons.collections4.CollectionUtils;

import java.util.List;

/**
 * Created by fabier on 27/02/16.
 */
public class PostActivity extends FragmentActivity implements View.OnClickListener {

    private static final int REQUESTCODE_CREATE_POST = 1;

    private ViewPager viewPager;
    private PostPagerAdapter postPagerAdapter;
    private List<Post> posts;

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
                Intent intent = new Intent(this, CreatePostActivity.class);
                startActivityForResult(intent, REQUESTCODE_CREATE_POST);
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
                    List<Post> posts = (List<Post>) data.getSerializableExtra("posts");
                    if (CollectionUtils.isNotEmpty(posts)) {
                        // ...et on ajoute le post nouvellement créé
                        postPagerAdapter.addFirstToPosts(posts);
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
}
