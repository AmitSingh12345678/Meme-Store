package com.example.memestore.general_classes;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.example.memestore.BuildConfig;
import com.example.memestore.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;

import static com.example.memestore.general_classes.PostOperations.getLikesCount;
import static com.example.memestore.general_classes.PostOperations.isPostLiked;
import static com.example.memestore.general_classes.PostOperations.saveImage;

public class PostsRecyclerViewAdapter extends RecyclerView.Adapter<PostViewHolder> {
    private static final String TAG = "PostsRecyclerViewAdapte";
    private ArrayList<Post> posts = null;
    private final int layoutResource;
    private final Context mContext;
    private OutputStream outputStream=null ;
    private String postType;

    public PostsRecyclerViewAdapter(ArrayList<Post> posts, int resource, Context context,String postType) {
        this.posts = posts;
        layoutResource = resource;
        mContext = context;
        this.postType = postType;
    }

    @Override
    public void onBindViewHolder(@NonNull final PostViewHolder holder, final int position) {
        final Post requiredPost = posts.get(position);
        final String[] postImageUri = {null};
        if(requiredPost!=null)
        holder.postUploadDate.setText(requiredPost.getPostUploadDate());

        String authorUID = requiredPost.getUserUID();
        DatabaseReference userDatabaseRef = FirebaseDatabase.getInstance().getReference("Users/" + authorUID);
        userDatabaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Log.d(TAG, "onDataChange: Called");
                User user = snapshot.getValue(User.class);
                if (user != null) {
                    postImageUri[0] =user.getUserProfilePic();
                    holder.postAuthorName.setText(user.getUserName());
                    Picasso.get().load(user.getUserProfilePic()).into(holder.postAuthorImage);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e(TAG, "onCancelled: Error " + error.getMessage());
            }
        });

        holder.downloadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveImage(holder.postImage,mContext);
            }
        });

        holder.shareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PostOperations.shareImage(holder.postImage,mContext);
            }
        });

        holder.postName.setText(requiredPost.getPostName());
        isPostLiked(requiredPost.getPostId(),holder.likeButton);
        getLikesCount(requiredPost.getPostId(),holder.likesCount);

        holder.likeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               PostOperations.likeClicked(v,requiredPost.getPostId());
            }
        });

        Picasso.get().load(requiredPost.getPostImageUrl()).into(holder.postImage);
    }

    @NonNull
    @Override
    public PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View createdView = LayoutInflater.from(mContext).inflate(layoutResource, parent, false);
        return new PostViewHolder(createdView);
    }

    @Override
    public int getItemCount() {
        return (posts != null ? posts.size() : 0);
    }

    public void loadData(ArrayList<Post> posts) {
        Log.d(TAG, "loadData: Loading new Posts");
        this.posts = posts;
        notifyDataSetChanged();
    }
}
