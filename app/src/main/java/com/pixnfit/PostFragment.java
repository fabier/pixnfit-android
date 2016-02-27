package com.pixnfit;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.pixnfit.adapter.PostAdapter;
import com.pixnfit.common.Post;
import com.pixnfit.common.PostComment;
import com.pixnfit.ws.GetPostCommentsAsyncTask;

import java.util.List;

/**
 * Created by fabier on 27/02/16.
 */
public class PostFragment extends Fragment {

    private Post post;
    private PostAdapter postCommentsListAdapter;

    public PostFragment(Post post) {
        this.post = post;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_post, container, false);

//        return rootView;
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.fragment_post);

        ListView postCommentsListView = (ListView) rootView.findViewById(R.id.postCommentsListView);
        postCommentsListAdapter = new PostAdapter(getActivity());
        postCommentsListView.setAdapter(postCommentsListAdapter);
        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();
        if (post != null) {
            postCommentsListAdapter.setPost(post);
            GetPostCommentsAsyncTask getPostCommentsAsyncTask = new GetPostCommentsAsyncTask(getActivity()) {
                @Override
                protected void onPostExecute(List<PostComment> postComments) {
                    postCommentsListAdapter.setPostComments(postComments);
                    postCommentsListAdapter.notifyDataSetChanged();
                }
            };
            getPostCommentsAsyncTask.execute(post);
        }
    }
}
