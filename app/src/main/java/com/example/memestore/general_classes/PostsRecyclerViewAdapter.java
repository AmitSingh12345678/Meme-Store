package com.example.memestore.general_classes;

import android.content.Context;
import android.media.Image;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.memestore.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
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
    public void onBindViewHolder(@NonNull final PostViewHolder holder, int position) {
        final Post requiredPost = posts.get(position);
        String authorUID = requiredPost.getUserUID();
        DatabaseReference userDatabaseRef = FirebaseDatabase.getInstance().getReference("Users/" + authorUID);
        userDatabaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Log.d(TAG, "onDataChange: Called");
                User user = snapshot.getValue(User.class);
                if (user != null) {
                    holder.postAuthorName.setText(user.getUserName());
                    Picasso.get().load(user.getUserProfilePic()).into(holder.postAuthorImage);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e(TAG, "onCancelled: Error " + error.getMessage());
            }
        });
        holder.postName.setText(requiredPost.getPostName());
        isPostLiked(requiredPost.getPostId(),holder.likeButton);
        getLikesCount(requiredPost.getPostId(),holder.likesCount);

        holder.likeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               if(v.getTag().equals("like")){
                   FirebaseDatabase.getInstance().getReference("Memes").child(requiredPost.getPostId())
                           .child("likes").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(true);
               }else{
                   FirebaseDatabase.getInstance().getReference("Memes").child(requiredPost.getPostId())
                           .child("likes").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).removeValue();
               }
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

    void isPostLiked(final String postId, final ImageButton imageButton){
        final FirebaseAuth auth = FirebaseAuth.getInstance();

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Memes")
                .child(postId).child("likes");

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.child(auth.getCurrentUser().getUid()).exists()){
                    imageButton.setImageResource(R.drawable.fav_filled);
                    imageButton.setTag("liked");
                }else{
                    imageButton.setImageResource(R.drawable.fav_outlined);
                    imageButton.setTag("like");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e(TAG, "onCancelled: DataBaseError" + error.getMessage());
            }
        });
    }

    void getLikesCount(final String postId, final TextView likesCountView){
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Memes").child(postId).child("likes");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                long likesCount = snapshot.getChildrenCount();
                String likesString = String.format("%d likes",likesCount);

                if(likesCount == 1 || likesCount == 0){
                    likesString = String.format("%d like",likesCount);
                }

                likesCountView.setText(likesString);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e(TAG, "onCancelled: Database error: " + error.getMessage());
            }
        });
    }
}
