package com.example.memestore;

import android.content.ContentResolver;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.UUID;

public class UploadPostActivity extends AppCompatActivity {
    private static final String TAG = "UploadPostActivity";
    private ImageView post_image;
    private EditText post_caption;
    private Button uploadBtn;

    private StorageReference mStorageRef;
    private DatabaseReference mDatabaseRef;
    private FirebaseAuth auth;
    private DatabaseReference mUserDatabaseRef;

    private StorageTask mUploadTask;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.upload_post_layout);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        post_image = findViewById(R.id.selectedPic);
        post_caption = findViewById(R.id.uploadPostCaption);

        mStorageRef = FirebaseStorage.getInstance().getReference("Memes");
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("Memes");
        mUserDatabaseRef = FirebaseDatabase.getInstance().getReference("Users");
        auth = FirebaseAuth.getInstance();

        final String postImageUri = getIntent().getStringExtra("imageUri");

        if(postImageUri != null){
            Log.d(TAG, "onCreate: Viewing image with URI: " + postImageUri);
            Picasso.get().load(postImageUri).into(post_image);
        }
        else{
            Log.e(TAG, "onCreate: Null Uri passed");
        }

        uploadBtn = findViewById(R.id.upload_post_btn);

        uploadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: Posted");
                Log.d(TAG, "onClick: Image Uri: " + postImageUri);
                Log.d(TAG, "onClick: Caption: " + post_caption.getText().toString());
                uploadPost(postImageUri);
                Toast.makeText(UploadPostActivity.this,"Posted Successfully",Toast.LENGTH_SHORT).show();
            }
        });
    }
    private String getFileExtension(Uri uri) {
        ContentResolver cR = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }
    private void uploadPost(String postImageUri) {
        if(postImageUri!=null){
            String caption=post_caption.getText().toString();
            Uri imageUri=Uri.parse(postImageUri);
            final String key= UUID.randomUUID().toString();
            mDatabaseRef.child(key).child("postName").setValue(caption);

            //Setting the post author name from the user UID
            String authorUid = auth.getCurrentUser().getUid();
            mUserDatabaseRef.child(authorUid).child("userName").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    Log.d(TAG, "onDataChange: Called");
                    String authorName = snapshot.getValue(String.class);
                    Log.d(TAG, "onDataChange: Post Author Name: " + authorName);
                    mDatabaseRef.child(key).child("postAuthorName").setValue(authorName);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Log.e(TAG, "onCancelled: "+error.getMessage());
                }
            });

            final String imageName=System.currentTimeMillis()
                    + "." + getFileExtension(imageUri);
            Log.d(TAG, "uploadPost: Key is "+key);
            Log.d(TAG, "uploadPost: Image name is "+imageName);
            StorageReference postImageRef=mStorageRef.child(key).child(imageName);
            mUploadTask = postImageRef.putFile(imageUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Toast.makeText(UploadPostActivity.this, "Upload successful", Toast.LENGTH_LONG).show();
                            mStorageRef.child(key+"/"+imageName).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    String downloadUrl= uri.toString();
                                    Log.d(TAG, "onSuccess: Image Url is "+downloadUrl);
                                    mDatabaseRef.child(key).child("postImageUrl").setValue(downloadUrl);
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.d(TAG, "onFailure: Error while getting image url!! "+e.getMessage());
                                }
                            });

                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(UploadPostActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            // for showing progress bar
                        }
                    });
        } else {
            Toast.makeText(this, "No file selected", Toast.LENGTH_SHORT).show();
        }
        finish();
    }

}