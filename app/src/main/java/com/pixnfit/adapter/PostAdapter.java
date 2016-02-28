package com.pixnfit.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.pixnfit.R;
import com.pixnfit.async.AsyncDrawable;
import com.pixnfit.async.BitmapWorkerTask;
import com.pixnfit.common.Image;
import com.pixnfit.common.Post;
import com.pixnfit.common.PostComment;
import com.pixnfit.common.PostMe;
import com.pixnfit.utils.ThreadPools;
import com.pixnfit.ws.AddPostToFavoriteAsyncTask;
import com.pixnfit.ws.GetPostMeAsyncTask;
import com.pixnfit.ws.RemovePostFromFavoriteAsyncTask;
import com.pixnfit.ws.SubmitPostVoteAsyncTask;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class PostAdapter extends BaseAdapter implements View.OnClickListener {

    private static final String TAG = PostAdapter.class.getSimpleName();
    private static final int VIEWTYPE_POSTHEADER = 0;
    private static final int VIEWTYPE_POSTCOMMENT = 1;

    private List<PostComment> postComments;
    private Context context;
    private Post post;
    //    private PostHeaderHolder postHeaderHolder;
    private Bitmap postImagePlaceHolder;
    private FloatingActionButton heartFloatingActionButton;
    private FloatingActionButton likeFloatingActionButton;
    private FloatingActionButton dislikeFloatingActionButton;

    public PostAdapter(Context context) {
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
            return post == null ? 0 : post.id;
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
                if (convertView == null) {
                    LayoutInflater inflater = LayoutInflater.from(context);
                    view = inflater.inflate(R.layout.item_header_post, null);
                    ImageView postImageView = (ImageView) view.findViewById(R.id.postImageView);
                    ImageButton postButtonComments = (ImageButton) view.findViewById(R.id.postButtonComments);
                    ImageButton postButtonShare = (ImageButton) view.findViewById(R.id.postButtonShare);
                    ImageButton postButtonHanger = (ImageButton) view.findViewById(R.id.postButtonHanger);
                    ImageButton postButtonMoreOptions = (ImageButton) view.findViewById(R.id.postButtonMoreOptions);
                    TextView postTitleTextView = (TextView) view.findViewById(R.id.postTitleTextView);
                    TextView postTitleViewCountTextView = (TextView) view.findViewById(R.id.postTitleViewCountTextView);

                    heartFloatingActionButton = (FloatingActionButton) view.findViewById(R.id.fabHeart);
                    likeFloatingActionButton = (FloatingActionButton) view.findViewById(R.id.fabLike);
                    dislikeFloatingActionButton = (FloatingActionButton) view.findViewById(R.id.fabDislike);

                    heartFloatingActionButton.setVisibility(View.GONE);

                    likeFloatingActionButton.setVisibility(View.GONE);
                    dislikeFloatingActionButton.setVisibility(View.GONE);

                    if (post != null) {
                        postTitleTextView.setText(post.name);
                        postTitleViewCountTextView.setText(post.viewCount + " views");
                        if (post.images != null && post.images.size() > 0) {
                            Image image = post.images.get(0);
                            loadPostUserInfo();
                            loadImageIntoView(image, postImageView);
                        } else {
                            postImageView.setTag(R.id.tagImageUrl, null);
                            postImageView.setImageResource(R.drawable.camera_transparent);
                        }
                    }
                } else {
                    view = convertView;
                }
                break;
            case VIEWTYPE_POSTCOMMENT:
                PostCommentHolder postCommentHolder;
                if (convertView == null) {
                    LayoutInflater inflater = LayoutInflater.from(context);
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
                    if (postComment.creator != null && postComment.creator.image != null) {
                        loadPostCommentAuthorImageView(postComment, postCommentHolder);
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

    private void loadPostUserInfo() {
        GetPostMeAsyncTask getPostMeAsyncTask = new GetPostMeAsyncTask(context) {
            @Override
            protected void onPostExecute(PostMe postMe) {
                if (!isCancelled() && postMe != null) {
                    if (postMe.vote != null) {
                        setHasVoted(postMe.vote.vote);
                    } else {
                        setHasVoted(null);
                    }

                    setIsFavorite(postMe.isFavorite);
                }
            }
        };
        getPostMeAsyncTask.execute(post);
    }

    private void loadPostCommentAuthorImageView(PostComment postComment, PostCommentHolder postCommentHolder) {
        postCommentHolder.postCommentAuthorImageView.setTag(R.id.tagImageUrl, postComment.creator.image.imageUrl);
        BitmapWorkerTask bitmapWorkerTask = new BitmapWorkerTask(postCommentHolder.postCommentAuthorImageView, 32, 32);
        bitmapWorkerTask.execute(postComment.creator.image.imageUrl);
    }

    private void loadImageIntoView(final Image image, final ImageView postImageView) {
        final String imageUrl = image.imageUrl;

        postImageView.setTag(R.id.tagImageUrl, imageUrl);

        if (cancelPotentialWork(imageUrl, postImageView)) {
            BitmapWorkerTask task = new BitmapWorkerTask(postImageView, 128, 128) {
                @Override
                protected void onPostExecute(Bitmap bitmap) {
                    super.onPostExecute(bitmap);
                    if (!isCancelled()) {
                        BitmapWorkerTask bitmapWorkerTask = new BitmapWorkerTask(postImageView, 512, 512);
                        bitmapWorkerTask.executeOnExecutor(ThreadPools.IMAGELOAD_THREADPOOL, imageUrl);

                        likeFloatingActionButton.setOnClickListener(PostAdapter.this);
                        likeFloatingActionButton.setVisibility(View.VISIBLE);
                        dislikeFloatingActionButton.setOnClickListener(PostAdapter.this);
                        dislikeFloatingActionButton.setVisibility(View.VISIBLE);
                    }
                }
            };
            AsyncDrawable asyncDrawable = new AsyncDrawable(context.getResources(), postImagePlaceHolder, task);
            postImageView.setImageDrawable(asyncDrawable);
            task.executeOnExecutor(ThreadPools.IMAGELOAD_THREADPOOL, imageUrl);
        }
    }

    private void setHasVoted(Boolean vote) {
        if (vote == null) {
            // L'utilisateur n'a pas voté
            likeFloatingActionButton.setBackgroundTintList(context.getResources().getColorStateList(R.color.green));
            dislikeFloatingActionButton.setBackgroundTintList(context.getResources().getColorStateList(R.color.orange));
            likeFloatingActionButton.setOnClickListener(this);
            dislikeFloatingActionButton.setOnClickListener(this);
        } else {
            // L'utilisateur a voté...
            if (vote) {
                // ... positivement
                likeFloatingActionButton.setBackgroundTintList(context.getResources().getColorStateList(R.color.green));
                dislikeFloatingActionButton.setBackgroundTintList(context.getResources().getColorStateList(R.color.grey));
            } else {
                // ... négativement
                likeFloatingActionButton.setBackgroundTintList(context.getResources().getColorStateList(R.color.grey));
                dislikeFloatingActionButton.setBackgroundTintList(context.getResources().getColorStateList(R.color.orange));
            }
            likeFloatingActionButton.setOnClickListener(null);
            dislikeFloatingActionButton.setOnClickListener(null);
        }
        likeFloatingActionButton.setVisibility(View.VISIBLE);
        dislikeFloatingActionButton.setVisibility(View.VISIBLE);
    }

    private void setIsFavorite(Boolean isFavorite) {
        heartFloatingActionButton.setTag(R.id.tagPostFavorite, isFavorite);
        heartFloatingActionButton.setOnClickListener(this);

        if (isFavorite) {
            heartFloatingActionButton.setBackgroundTintList(context.getResources().getColorStateList(R.color.red));
        } else {
            heartFloatingActionButton.setBackgroundTintList(context.getResources().getColorStateList(R.color.black));
        }

        heartFloatingActionButton.setVisibility(View.VISIBLE);
    }

    private boolean cancelPotentialWork(String imageUrl, ImageView imageView) {
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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fabLike:
                new SubmitPostVoteAsyncTask(context, post, true).execute();
                Snackbar.make(v, "Thank you for your vote !", Snackbar.LENGTH_LONG).show();
                setHasVoted(true);
                break;
            case R.id.fabDislike:
                new SubmitPostVoteAsyncTask(context, post, false).execute();
                Snackbar.make(v, "Thank you for your vote !", Snackbar.LENGTH_LONG).show();
                setHasVoted(false);
                break;
            case R.id.fabHeart:
                Boolean isFavorite = (Boolean) v.getTag(R.id.tagPostFavorite);
                if (isFavorite) {
                    new RemovePostFromFavoriteAsyncTask(context, post).execute();
                    Snackbar.make(v, "Post has been remove from favorites !", Snackbar.LENGTH_LONG).show();
                } else {
                    new AddPostToFavoriteAsyncTask(context, post).execute();
                    Snackbar.make(v, "Post has been added to favorites !", Snackbar.LENGTH_LONG).show();
                }
                setIsFavorite(!isFavorite);
            default:
                break;
        }
    }

    public void setPostImagePlaceHolder(Bitmap postImagePlaceHolder) {
        this.postImagePlaceHolder = postImagePlaceHolder;
    }
}

class PostCommentHolder {
    ImageView postCommentAuthorImageView;
    TextView postCommentAuthorNameTextView;
    TextView postCommentDateTextView;
    TextView postCommentDescriptionTextView;
}