package com.pixnfit.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.pixnfit.R;
import com.pixnfit.common.Image;
import com.pixnfit.common.Post;

import java.util.List;

/**
 * Created by fabier on 16/02/16.
 */
public class PostListAdapter extends BaseAdapter {
    private static final String TAG = PostListAdapter.class.getSimpleName();

    private Context context;
    private List<Post> posts;

    public PostListAdapter(Context context) {
        this.context = context;
    }

    @Override
    public int getCount() {
        return posts == null ? 0 : posts.size();
    }

    @Override
    public Object getItem(int position) {
        return getPost(position);
    }

    public Post getPost(int position) {
        return posts == null ? null : posts.get(position);
    }

    @Override
    public long getItemId(int position) {
        Post post = getPost(position);
        return post == null ? 0 : post.id;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view;
        ImageView imageView;
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.item_post, null);
            imageView = (ImageView) view.findViewById(R.id.postImageView);
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        } else {
            view = convertView;
            imageView = (ImageView) view.findViewById(R.id.postImageView);
        }

        imageView.setImageResource(R.drawable.camera_transparent);

        if (imageView != null) {
            Post post = getPost(position);
            if (post.images != null && post.images.size() > 0) {
                Image image = post.images.get(0);
                imageView.setTag(R.id.tagImageUrl, image.imageUrl);
                loadBitmap(image.imageUrl, imageView);
            }
        }

        return view;
    }

    public void loadBitmap(String imageUrl, ImageView imageView) {
        BitmapWorkerTask task = new BitmapWorkerTask(imageView, 128, 128);
        task.execute(imageUrl);
    }

    public void setPosts(List<Post> posts) {
        this.posts = posts;
    }
}