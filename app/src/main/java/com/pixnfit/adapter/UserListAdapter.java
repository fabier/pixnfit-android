package com.pixnfit.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.pixnfit.R;
import com.pixnfit.async.AsyncDrawable;
import com.pixnfit.async.BitmapWorkerTask;
import com.pixnfit.common.Image;
import com.pixnfit.common.User;
import com.pixnfit.utils.ThreadPools;

import org.apache.commons.collections4.CollectionUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by fabier on 16/02/16.
 */
public class UserListAdapter extends BaseAdapter {
    private static final String TAG = UserListAdapter.class.getSimpleName();

    private Context context;
    private List<User> users;
    private Bitmap userImagePlaceHolder;

    public UserListAdapter(Context context) {
        this.context = context;
    }

    @Override
    public int getCount() {
        return users == null ? 0 : users.size();
    }

    @Override
    public Object getItem(int position) {
        return getUser(position);
    }

    public User getUser(int position) {
        return users == null ? null : users.get(position);
    }

    public List<User> getUsers() {
        return users;
    }

    @Override
    public long getItemId(int position) {
        User user = getUser(position);
        return user == null ? 0 : user.id;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view;
        ImageView imageView;
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.grid_item_user, null);
            imageView = (ImageView) view.findViewById(R.id.userImageView);
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        } else {
            view = convertView;
            imageView = (ImageView) view.findViewById(R.id.userImageView);
        }

        if (imageView != null) {
            User user = getUser(position);
            if (user.image != null) {
                Image image = user.image;
                loadBitmap(image.imageUrl, imageView);
            } else {
                imageView.setImageResource(R.drawable.camera_transparent);
            }
        }

        return view;
    }

    public void loadBitmap(String imageUrl, ImageView imageView) {
        if (cancelPotentialWork(imageUrl, imageView)) {
            imageView.setTag(R.id.tagImageUrl, imageUrl);
            BitmapWorkerTask task = new BitmapWorkerTask(imageView, 128, 128);
            AsyncDrawable asyncDrawable = new AsyncDrawable(context.getResources(), userImagePlaceHolder, task);
            imageView.setImageDrawable(asyncDrawable);
            task.executeOnExecutor(ThreadPools.IMAGELOAD_THREADPOOL, imageUrl);
        }
    }

    public boolean cancelPotentialWork(String imageUrl, ImageView imageView) {
        final BitmapWorkerTask bitmapWorkerTask = getBitmapWorkerTask(imageView);

        if (bitmapWorkerTask != null) {
            String workerImageUrl = bitmapWorkerTask.getImageURL();
            // If workerImageUrl is not yet set or it differs from the new imageUrl
            if (workerImageUrl == null || !workerImageUrl.equals(imageUrl)) {
                // Cancel previous task
                bitmapWorkerTask.cancel(true);
            } else {
                // The same work is already in progress
                return false;
            }
        }
        // No task associated with the ImageView, or an existing task was cancelled
        return true;
    }

    private BitmapWorkerTask getBitmapWorkerTask(ImageView imageView) {
        if (imageView != null) {
            final Drawable drawable = imageView.getDrawable();
            if (drawable instanceof AsyncDrawable) {
                final AsyncDrawable asyncDrawable = (AsyncDrawable) drawable;
                return asyncDrawable.getBitmapWorkerTask();
            }
        }
        return null;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

    public void setUserImagePlaceHolder(Bitmap userImagePlaceHolder) {
        this.userImagePlaceHolder = userImagePlaceHolder;
    }

    public void addNewUsers(List<User> users) {
        if (CollectionUtils.isNotEmpty(users)) {
            if (CollectionUtils.isEmpty(this.users)) {
                this.users = users;
            } else {
                // Il faut inverser la liste pour l'avoir dans le même ordre vu qu'on insère les éléments au début...
                Collections.reverse(users);
                for (User user : users) {
                    this.users.add(0, user);
                }
            }
        }
    }

    public void addUsersUnique(List<User> users) {
        if (CollectionUtils.isNotEmpty(users)) {
            if (this.users == null) {
                this.users = new ArrayList<>(users);
            } else {
                for (User user : users) {
                    if (!this.users.contains(user)) {
                        this.users.add(user);
                    }
                }
            }
        }
    }
}