package com.example.memestore.general_classes;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class PostsRecyclerViewAdapter extends RecyclerView.Adapter<PostViewHolder> {
    private static final String TAG = "PostsRecyclerViewAdapte";
    private ArrayList<Post> posts = null;
    private final int layoutResource;
    private final Context mContext;

    public PostsRecyclerViewAdapter(ArrayList<Post> posts, int resource, Context context) {
        this.posts = posts;
        layoutResource = resource;
        mContext = context;
    }

    @Override
    public void onBindViewHolder(@NonNull PostViewHolder holder, int position) {
        Post requiredPost = posts.get(position);
        holder.postAuthorName.setText(requiredPost.getPostAuthorName());
        Picasso.get().load(requiredPost.getPostAuthorImageUrl()).into(holder.postAuthorImage);
        Picasso.get().load(requiredPost.getPostImageUrl()).into(holder.postImage);
    }

    @NonNull
    @Override
    public PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View createdView = LayoutInflater.from(mContext).inflate(layoutResource,parent,false);
        return new PostViewHolder(createdView);
    }

    @Override
    public int getItemCount() {
        return (posts != null ? posts.size() : 0);
    }

    public void loadData(ArrayList<Post> posts){
        Log.d(TAG, "loadData: Loading new Posts");
        this.posts=posts;
        notifyDataSetChanged();
    }
}
