package com.example.memestore.general_classes;

import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.memestore.R;

public class GalleryImageViewHolder extends RecyclerView.ViewHolder {

    public ImageView memeImageView;

    public GalleryImageViewHolder(@NonNull View itemView) {
        super(itemView);
        memeImageView = itemView.findViewById(R.id.gallery_image);
    }
}
