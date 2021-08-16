package com.example.memestore;

import androidx.annotation.LongDef;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.example.memestore.general_classes.Post;
import com.example.memestore.general_classes.PostOperations;
import com.example.memestore.general_classes.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.io.Serializable;

public class DisplayPost extends AppCompatActivity {

    private static final String TAG = "DisplayPost";
    private TextView postAuthorName;
    private TextView postDate;
    private ImageView postImage;
    private ImageView authorImage;
    private TextView likesCount;
    private ImageButton likeBtn;
    private ImageButton shareBtn;
    private ImageButton downloadBtn;
    private Post displayPost;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_post);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        displayPost = (Post) getIntent().getSerializableExtra("displayPost");

        Log.d(TAG, "onCreate: display Post: " + displayPost);

        postAuthorName = findViewById(R.id.display_post_author_name);
        postDate = findViewById(R.id.display_post_upload_date);
        postImage = findViewById(R.id.display_post_image);
        likesCount = findViewById(R.id.displaylikesCountView);
        likeBtn = findViewById(R.id.display_post_like_btn);
        shareBtn = findViewById(R.id.display_post_share_btn);
        downloadBtn = findViewById(R.id.display_post_download_btn);
        authorImage = findViewById(R.id.display_post_author_image);

        if(displayPost == null)
        {
            Toast.makeText(this,"Post not found",Toast.LENGTH_SHORT).show();
            finish();
        }

        String authorId = displayPost.getUserUID();
        DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference("Users");
        usersRef.child(authorId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User postAuthor = snapshot.getValue(User.class);
                postAuthorName.setText(postAuthor.getUserName());
                Picasso.get().load(postAuthor.getUserProfilePic())
                        .into(authorImage);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        postDate.setText(displayPost.getPostUploadDate());
        Picasso.get().load(displayPost.getPostImageUrl())
                .into(postImage);

        PostOperations.isPostLiked(displayPost.getPostId(),likeBtn);
        PostOperations.getLikesCount(displayPost.getPostId(),likesCount);

        likeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PostOperations.likeClicked(v,displayPost.getPostId());
            }
        });

        downloadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PostOperations.saveImage(postImage,DisplayPost.this);
            }
        });

        shareBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PostOperations.shareImage(postImage,DisplayPost.this);
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }
}