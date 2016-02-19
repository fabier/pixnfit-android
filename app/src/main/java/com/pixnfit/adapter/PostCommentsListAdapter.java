package com.pixnfit.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.pixnfit.R;
import com.pixnfit.common.Image;
import com.pixnfit.common.Post;
import com.pixnfit.common.PostComment;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class PostCommentsListAdapter extends BaseAdapter {

    private static final String TAG = PostCommentsListAdapter.class.getSimpleName();
    private static final int VIEWTYPE_POSTHEADER = 0;
    private static final int VIEWTYPE_POSTCOMMENT = 1;

    private List<PostComment> postComments;
    private Context context;
    private Post post;

    public PostCommentsListAdapter(Context context) {
        this.context = context;
    }

    public Post getPost() {
        return post;
    }

    public void setPost(Post post) {
        this.post = post;
    }

    public List<PostComment> getPostComments() {
        return postComments;
    }

    public void setPostComments(List<PostComment> postComments) {
        this.postComments = postComments;
    }

    @Override
    public int getCount() {
        return 1 + (postComments == null ? 0 : postComments.size());
    }

    @Override
    public int getViewTypeCount() {
        // Soit le header, soit le commentaire
        return 2;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return VIEWTYPE_POSTHEADER;
        } else {
            return VIEWTYPE_POSTCOMMENT;
        }
    }

    @Override
    public Object getItem(int position) {
        if (position == 0) {
            return post;
        } else {
            return getPostComment(position);
        }
    }

    public PostComment getPostComment(int position) {
        return postComments == null ? null : postComments.get(position - 1);
    }

    @Override
    public long getItemId(int position) {
        if (position == 0) {
            return post.id;
        } else {
            PostComment postComment = getPostComment(position);
            return postComment == null ? 0 : postComment.id;
        }
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view;
        switch (getItemViewType(position)) {
            case VIEWTYPE_POSTHEADER:
                PostHeaderHolder postHeaderHolder;
                if (convertView == null) {
                    LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    view = inflater.inflate(R.layout.item_header_post, null);
                    postHeaderHolder = new PostHeaderHolder();
                    postHeaderHolder.postImageView = (ImageView) view.findViewById(R.id.postImageView);
                    postHeaderHolder.postButtonHeart = (ImageButton) view.findViewById(R.id.postButtonHeart);
                    postHeaderHolder.postButtonComments = (ImageButton) view.findViewById(R.id.postButtonComments);
                    postHeaderHolder.postButtonShare = (ImageButton) view.findViewById(R.id.postButtonShare);
                    postHeaderHolder.postButtonHanger = (ImageButton) view.findViewById(R.id.postButtonHanger);
                    postHeaderHolder.postButtonMoreOptions = (ImageButton) view.findViewById(R.id.postButtonMoreOptions);
                    postHeaderHolder.postTitleTextView = (TextView) view.findViewById(R.id.postTitleTextView);
                    postHeaderHolder.postTitleViewCountTextView = (TextView) view.findViewById(R.id.postTitleViewCountTextView);
                    view.setTag(postHeaderHolder);
                } else {
                    view = convertView;
                    postHeaderHolder = (PostHeaderHolder) view.getTag();
                }

                if (postHeaderHolder != null && post != null) {
                    postHeaderHolder.postTitleTextView.setText(post.name);
                    postHeaderHolder.postTitleViewCountTextView.setText(post.viewCount + " views");
                    if (post.images != null && post.images.size() > 0) {
                        Image image = post.images.get(0);
                        postHeaderHolder.postImageView.setTag(R.id.tagImageUrl, image.imageUrl);
                        loadImageIntoView(image.imageUrl, postHeaderHolder.postImageView);
                    } else {
                        postHeaderHolder.postImageView.setTag(R.id.tagImageUrl, null);
                        postHeaderHolder.postImageView.setImageResource(R.drawable.camera_transparent);
                    }
                }
                break;
            case VIEWTYPE_POSTCOMMENT:
                PostCommentHolder postCommentHolder;
                if (convertView == null) {
                    LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    view = inflater.inflate(R.layout.item_postcomment, null);
                    postCommentHolder = new PostCommentHolder();
                    postCommentHolder.postCommentAuthorImageView = (ImageView) view.findViewById(R.id.postCommentAuthorImageView);
                    postCommentHolder.postCommentAuthorNameTextView = (TextView) view.findViewById(R.id.postCommentAuthorNameTextView);
                    postCommentHolder.postCommentDateTextView = (TextView) view.findViewById(R.id.postCommentDateTextView);
                    postCommentHolder.postCommentDescriptionTextView = (TextView) view.findViewById(R.id.postCommentDescriptionTextView);
                    postCommentHolder.postCommentAuthorImageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                    view.setTag(postCommentHolder);
                } else {
                    view = convertView;
                    postCommentHolder = (PostCommentHolder) view.getTag();
                }

                PostComment postComment = getPostComment(position);

                if (postCommentHolder != null && postComment != null) {
                    postCommentHolder.postCommentAuthorImageView.setImageResource(R.drawable.profile);
                    postCommentHolder.postCommentAuthorNameTextView.setText(postComment.creator.username);
                    postCommentHolder.postCommentDateTextView.setText(new SimpleDateFormat(", dd MMM yyyy, HH:mm", Locale.ENGLISH).format(postComment.dateCreated));
                    postCommentHolder.postCommentDescriptionTextView.setText(postComment.description);
                    if (postComment.creator != null && postComment.creator.imageUrl != null) {
                        postCommentHolder.postCommentAuthorImageView.setTag(R.id.tagImageUrl, postComment.creator.imageUrl);
                        loadImageIntoView(postComment.creator.imageUrl, postCommentHolder.postCommentAuthorImageView);
                    } else {
                        postCommentHolder.postCommentAuthorImageView.setTag(R.id.tagImageUrl, null);
                        postCommentHolder.postCommentAuthorImageView.setImageResource(R.drawable.profile);
                    }
                }
                break;
            default:
                throw new IllegalArgumentException("Item view type should be either VIEWTYPE_POSTHEADER or VIEWTYPE_POSTCOMMENT");
        }

        return view;
    }

    private void loadImageIntoView(final String imageUrl, final ImageView postImageView) {
        BitmapWorkerTask bitmapWorkerTask = new BitmapWorkerTask(postImageView, 128, 128) {
            @Override
            protected void onPostExecute(Bitmap bitmap) {
                super.onPostExecute(bitmap);
                BitmapWorkerTask bitmapWorkerTask = new BitmapWorkerTask(postImageView, 512, 512);
                bitmapWorkerTask.execute(imageUrl);
            }
        };
        bitmapWorkerTask.execute(imageUrl);
    }
}

class PostCommentHolder {
    ImageView postCommentAuthorImageView;
    TextView postCommentAuthorNameTextView;
    TextView postCommentDateTextView;
    TextView postCommentDescriptionTextView;
}

class PostHeaderHolder {
    public ImageView postImageView;
    public ImageButton postButtonHeart;
    public ImageButton postButtonComments;
    public ImageButton postButtonShare;
    public ImageButton postButtonHanger;
    public ImageButton postButtonMoreOptions;
    public TextView postTitleTextView;
    public TextView postTitleViewCountTextView;
}