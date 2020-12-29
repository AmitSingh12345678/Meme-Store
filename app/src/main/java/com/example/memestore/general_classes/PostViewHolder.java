package com.example.memestore.general_classes;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.memestore.R;

public class PostViewHolder extends RecyclerView.ViewHolder{
    public ImageView postAuthorImage;
    public TextView postAuthorName;
    public ImageView postImage;

    public PostViewHolder(@NonNull View itemView) {
        super(itemView);
        postAuthorImage = itemView.findViewById(R.id.post_author_image);
        postAuthorName = itemView.findViewById(R.id.post_author_name);
        postImage = itemView.findViewById(R.id.post_image);
    }
}
