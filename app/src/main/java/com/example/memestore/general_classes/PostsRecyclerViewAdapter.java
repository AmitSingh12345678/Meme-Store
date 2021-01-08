package com.example.memestore.general_classes;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

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

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;

public class PostsRecyclerViewAdapter extends RecyclerView.Adapter<PostViewHolder> {
    private static final String TAG = "PostsRecyclerViewAdapte";
    private ArrayList<Post> posts = null;
    private final int layoutResource;
    private final Context mContext;
    private OutputStream outputStream=null ;

    public PostsRecyclerViewAdapter(ArrayList<Post> posts, int resource, Context context) {
        this.posts = posts;
        layoutResource = resource;
        mContext = context;
    }

    @Override
    public void onBindViewHolder(@NonNull final PostViewHolder holder, final int position) {
        final Post requiredPost = posts.get(position);
        final String[] postImageUri = {null};
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
                BitmapDrawable bitmapDrawable= (BitmapDrawable) holder.postImage.getDrawable();
                Bitmap bitmap=null;
                if(bitmapDrawable!=null) {
                     bitmap = bitmapDrawable.getBitmap();
                }else{
                    Log.d(TAG, "onClick: Error!!!!!!!!!!!");
                    return;
                }
                File filepath= Environment.getExternalStorageDirectory();
                File dir=new File(filepath.getAbsolutePath()+"/MemeStore/");
                Log.d(TAG, "onClick: "+filepath.getAbsolutePath()+"/MemeStore/");
                dir.mkdir();
                File file=new File(dir,System.currentTimeMillis()+".jpg");
                try {
                    outputStream=new FileOutputStream(file);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                if(bitmap!=null && outputStream!=null) {
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
                    Toast.makeText(mContext, "Image Saved!!", Toast.LENGTH_SHORT).show();
                }else{
                    Log.d(TAG, "onClick: Error");
                    Toast.makeText(mContext, "Error while downloading image", Toast.LENGTH_SHORT).show();
                }
                if(outputStream!=null) {
                    try {
                        outputStream.flush();
                        outputStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
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
