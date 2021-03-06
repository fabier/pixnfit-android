package com.pixnfit.fragment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.pixnfit.ProfileActivity;
import com.pixnfit.R;
import com.pixnfit.adapter.PostAdapter;
import com.pixnfit.common.Post;
import com.pixnfit.common.PostComment;
import com.pixnfit.utils.ThreadPools;
import com.pixnfit.ws.GetPostCommentsAsyncTask;

import java.util.List;

/**
 * Created by fabier on 27/02/16.
 */
public class PostFragment extends Fragment implements AdapterView.OnItemClickListener {

    private PostAdapter postAdapter;

    public PostFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_post, container, false);
        ListView postListView = (ListView) rootView.findViewById(R.id.postListView);
        postListView.setOnItemClickListener(this);
        postListView.setAdapter(postAdapter);
        return rootView;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        postAdapter = new PostAdapter(getActivity());
        postAdapter.setPostImagePlaceHolder((Bitmap) getArguments().getParcelable("postImagePlaceholder"));

        Post post = (Post) getArguments().getSerializable("post");
        if (post != null) {
            postAdapter.setPost(post);
            GetPostCommentsAsyncTask getPostCommentsAsyncTask = new GetPostCommentsAsyncTask(getActivity()) {
                @Override
                protected void onPostExecute(List<PostComment> postComments) {
                    super.onPostExecute(postComments);
                    if (!isCancelled()) {
                        postAdapter.setPostComments(postComments);
                        postAdapter.notifyDataSetChanged();
                    }
                }
            };
            getPostCommentsAsyncTask.executeOnExecutor(ThreadPools.METADATA_THREADPOOL, post);
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        PostComment postComment = this.postAdapter.getPostComment(position);
        if (postComment != null) {
            Intent profileIntent = new Intent(getActivity(), ProfileActivity.class);
            profileIntent.putExtra("user", postComment.creator);
            startActivity(profileIntent);
        }
    }
}
