package com.example.memestore;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

public class UploadPostActivity extends AppCompatActivity {
    private static final String TAG = "UploadPostActivity";
    private ImageView post_image;
    private EditText post_caption;
    private Button uploadBtn;

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
                Toast.makeText(UploadPostActivity.this,"Posted Successfully",Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }
}