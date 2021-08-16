package com.example.memestore.general_classes;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.memestore.DisplayPost;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collections;

public class MemeGalleryAdapter extends RecyclerView.Adapter<GalleryImageViewHolder>{

    private ArrayList<Post> mPosts;
    private int layoutResource;
    private Context mContext;

    public MemeGalleryAdapter(ArrayList<Post> posts, Context context, int layout){
        mPosts = posts;
        mContext = context;
        layoutResource = layout;
    }

    @NonNull
    @Override
    public GalleryImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(layoutResource,parent,false);
        return new GalleryImageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GalleryImageViewHolder holder, final int position) {
        Post requiredPost = mPosts.get(position);
        String postImageUrl = requiredPost.getPostImageUrl();

        Picasso.get()
                .load(postImageUrl)
                .into(holder.memeImageView);

        holder.memeImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, DisplayPost.class);
                intent.putExtra("displayPost",mPosts.get(position));
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return (mPosts != null ? mPosts.size() : 0);
    }

    public void updatePosts(ArrayList<Post> updatedPosts){
        mPosts = updatedPosts;
        Collections.shuffle(mPosts);
        notifyDataSetChanged();
    }
}

