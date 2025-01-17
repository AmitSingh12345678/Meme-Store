package com.example.memestore.general_classes;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class GetDataBasePosts {
    private static final String TAG = "GetDataBasePosts";
    private ArrayList<Post> mPosts;
    private GetPostList.OnListAvailable mCallback;
    private DatabaseReference mDatabaseRef;
    private Context mContext;
    private String databasePath;

    public GetDataBasePosts(Context context, GetPostList.OnListAvailable callback,String databasePath){
        mDatabaseRef = FirebaseDatabase.getInstance().getReference(databasePath);
        mContext = context;
        mCallback = callback;
        this.databasePath = databasePath;
    }

    public void getPosts(){

        mDatabaseRef.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                mPosts = new ArrayList<>();
                for(DataSnapshot postSnapShot: snapshot.getChildren()){
                    Post post = postSnapShot.getValue(Post.class);
                    post.setPostId(postSnapShot.getKey());
                    Log.d(TAG, "onDataChange: Adding the post: " + post.toString());
                    mPosts.add(post);
                }

                mCallback.onListAvailable(mPosts);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(mContext,error.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });
    }
}
