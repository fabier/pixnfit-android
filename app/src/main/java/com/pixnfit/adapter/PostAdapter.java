package com.pixnfit.adapter;

import android.content.Context;
import android.graphics.Bitmap;
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
import com.pixnfit.async.BitmapWorkerTask;
import com.pixnfit.common.Image;
import com.pixnfit.common.Post;
import com.pixnfit.common.PostComment;
import com.pixnfit.common.PostMe;
import com.pixnfit.ws.GetPostMeAsyncTask;
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
    private PostHeaderHolder postHeaderHolder;

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

                    postHeaderHolder.likeFloatingActionButton = (FloatingActionButton) view.findViewById(R.id.fabLike);
                    postHeaderHolder.dislikeFloatingActionButton = (FloatingActionButton) view.findViewById(R.id.fabDislike);

                    postHeaderHolder.likeFloatingActionButton.setVisibility(View.GONE);
                    postHeaderHolder.dislikeFloatingActionButton.setVisibility(View.GONE);

                    if (post != null) {
                        postHeaderHolder.postTitleTextView.setText(post.name);
                        postHeaderHolder.postTitleViewCountTextView.setText(post.viewCount + " views");
                        if (post.images != null && post.images.size() > 0) {
                            Image image = post.images.get(0);
                            loadPostUserInfo();
                            loadImageIntoView(image, postHeaderHolder);
                        } else {
                            postHeaderHolder.postImageView.setTag(R.id.tagImageUrl, null);
                            postHeaderHolder.postImageView.setImageResource(R.drawable.camera_transparent);
                        }
                    }
                } else {
                    view = convertView;
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
                if (postMe != null && postMe.vote != null) {
                    setHasVoted(postMe.vote.vote);
                } else {
                    setHasVoted(null);
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

    private void loadImageIntoView(final Image image, final PostHeaderHolder postHeaderHolder) {
        postHeaderHolder.postImageView.setTag(R.id.tagImageUrl, image.imageUrl);
        BitmapWorkerTask bitmapWorkerTask = new BitmapWorkerTask(postHeaderHolder.postImageView, 128, 128) {
            @Override
            protected void onPostExecute(Bitmap bitmap) {
                super.onPostExecute(bitmap);
                BitmapWorkerTask bitmapWorkerTask = new BitmapWorkerTask(postHeaderHolder.postImageView, 512, 512);
                bitmapWorkerTask.execute(image.imageUrl);

                postHeaderHolder.likeFloatingActionButton.setOnClickListener(PostAdapter.this);
                postHeaderHolder.likeFloatingActionButton.setVisibility(View.VISIBLE);
                postHeaderHolder.dislikeFloatingActionButton.setOnClickListener(PostAdapter.this);
                postHeaderHolder.dislikeFloatingActionButton.setVisibility(View.VISIBLE);
            }
        };
        bitmapWorkerTask.execute(image.imageUrl);
    }

    private void setHasVoted(Boolean vote) {
        FloatingActionButton likeFloatingActionButton = postHeaderHolder.likeFloatingActionButton;
        FloatingActionButton dislikeFloatingActionButton = postHeaderHolder.dislikeFloatingActionButton;
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
            default:
                break;
        }
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
    public FloatingActionButton likeFloatingActionButton;
    public FloatingActionButton dislikeFloatingActionButton;
}