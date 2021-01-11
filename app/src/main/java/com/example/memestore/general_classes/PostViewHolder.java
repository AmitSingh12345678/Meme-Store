package com.example.memestore.general_classes;

import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.memestore.R;

public class PostViewHolder extends RecyclerView.ViewHolder{
    public ImageView postAuthorImage;
    public TextView postAuthorName;
    public ImageView postImage;
    public TextView postName;
    public ImageButton likeButton;
    public TextView likesCount;
    public ImageButton downloadButton;
    public ImageButton shareButton;

    public PostViewHolder(@NonNull View itemView) {
        super(itemView);
        postAuthorImage = itemView.findViewById(R.id.post_author_image);
        postAuthorName = itemView.findViewById(R.id.post_author_name);
        postImage = itemView.findViewById(R.id.post_image);
        postName = itemView.findViewById(R.id.caption);
        likeButton = itemView.findViewById(R.id.like_btn);
        likesCount = itemView.findViewById(R.id.likesCountView);
        downloadButton=itemView.findViewById(R.id.download_btn);
        shareButton = itemView.findViewById(R.id.share_btn);
    }
}
