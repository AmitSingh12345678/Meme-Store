package com.example.memestore;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.memestore.general_classes.GetDataBasePosts;
import com.example.memestore.general_classes.GetPostList;
import com.example.memestore.general_classes.MemeGalleryAdapter;
import com.example.memestore.general_classes.Post;

import java.util.ArrayList;

public class SearchFragment extends Fragment implements GetPostList.OnListAvailable {
    private static final String TAG = "SearchFragment";
    private RecyclerView mRecyclerView;
    private ArrayList<Post> mPosts;
    private MemeGalleryAdapter mAdapter;
    private final String dataBasePath = "Memes";
    private final int GRID_COLS = 2;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_search,container,false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mRecyclerView = view.findViewById(R.id.meme_gallery);
        mRecyclerView.setLayoutManager(new GridLayoutManager(getContext(),GRID_COLS));

        mAdapter = new MemeGalleryAdapter(mPosts,getContext(),R.layout.gallery_image_layout);
        mRecyclerView.setAdapter(mAdapter);

        GetDataBasePosts getDataBasePosts = new GetDataBasePosts(getContext(),this,dataBasePath);
        getDataBasePosts.getPosts();
    }

    @Override
    public void onListAvailable(ArrayList<Post> posts) {

        Log.d(TAG, "onListAvailable: " + posts);
        if(posts != null){
            mPosts = posts;
            mAdapter.updatePosts(posts);
        }
    }
}
