package com.example.memestore;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.memestore.general_classes.GetDataBasePosts;
import com.example.memestore.general_classes.GetPostList;
import com.example.memestore.general_classes.Post;
import com.example.memestore.general_classes.PostsRecyclerViewAdapter;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link quotesFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class quotesFragment extends Fragment implements GetPostList.OnListAvailable, SwipeRefreshLayout.OnRefreshListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private RecyclerView mRecyclerView;
    private PostsRecyclerViewAdapter mPostsRecyclerViewAdapter;
    private ArrayList<Post> mPosts;
    private final String POST_TYPE = "Quotes";
    private static final String TAG = "quotesFragment";
    private SwipeRefreshLayout quotesRefreshLayout;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public quotesFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment quotesFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static quotesFragment newInstance(String param1, String param2) {
        quotesFragment fragment = new quotesFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_quotes, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mRecyclerView = view.findViewById(R.id.quotes_list);
        quotesRefreshLayout = view.findViewById(R.id.post_refresh);
        quotesRefreshLayout.setOnRefreshListener(this);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mPostsRecyclerViewAdapter = new PostsRecyclerViewAdapter(mPosts,R.layout.post,getContext(),POST_TYPE);
        mRecyclerView.setAdapter(mPostsRecyclerViewAdapter);

        GetDataBasePosts getDataBasePosts = new GetDataBasePosts(getContext(),this,POST_TYPE);
        getDataBasePosts.getPosts();
    }

    @Override
    public void onListAvailable(ArrayList<Post> posts) {
        Log.d(TAG, "onListAvailable: Got the post list");
        Log.d(TAG, "onListAvailable: Posts:" + posts);

        if(posts != null)
        {
            mPosts = posts;
            mPostsRecyclerViewAdapter.loadData(mPosts);
        }
    }

    @Override
    public void onRefresh() {
        updatePosts();
        quotesRefreshLayout.setRefreshing(false);
    }

    private void updatePosts(){
        GetDataBasePosts getNewDataBasePosts = new GetDataBasePosts(getContext(),this,POST_TYPE);
        getNewDataBasePosts.getPosts();
    }
}