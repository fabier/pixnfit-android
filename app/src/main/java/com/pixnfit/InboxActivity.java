package com.pixnfit;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageButton;

public class InboxActivity extends AppCompatActivity implements View.OnClickListener {

    private static final int REQUESTCODE_CREATE_POST = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inbox);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        initFooterButtonBar();
    }

    private void initFooterButtonBar() {
        ImageButton homeButtonBar = (ImageButton) findViewById(R.id.homeButtonBar);
        homeButtonBar.setOnClickListener(this);
        ImageButton followersButtonBar = (ImageButton) findViewById(R.id.followersButtonBar);
        followersButtonBar.setOnClickListener(this);
        ImageButton cameraButtonBar = (ImageButton) findViewById(R.id.cameraButtonBar);
        cameraButtonBar.setOnClickListener(this);
        ImageButton inboxButtonBar = (ImageButton) findViewById(R.id.inboxButtonBar);
        inboxButtonBar.setOnClickListener(this);
        ImageButton profileButtonBar = (ImageButton) findViewById(R.id.profileButtonBar);
        profileButtonBar.setOnClickListener(this);

        // Set inboxButtonBar as selected
        inboxButtonBar.setBackgroundColor(getResources().getColor(R.color.colorAccent));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.homeButtonBar:
                Intent homeIntent = new Intent(this, HomeActivity.class);
                startActivity(homeIntent);
                break;
            case R.id.followersButtonBar:
                Intent followersIntent = new Intent(this, FollowersActivity.class);
                startActivity(followersIntent);
                break;
            case R.id.cameraButtonBar:
                Intent createPostIntent = new Intent(this, CreatePostActivity.class);
                startActivityForResult(createPostIntent, REQUESTCODE_CREATE_POST);
                break;
            case R.id.inboxButtonBar:
                // already there
                break;
            case R.id.profileButtonBar:
                Intent profileIntent = new Intent(this, ProfileActivity.class);
                startActivity(profileIntent);
                break;
            default:
                break;
        }
    }
}
