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
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;

import com.example.memestore.BuildConfig;
import com.example.memestore.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class PostOperations {

    private static final String TAG = "PostOperations";

    public static void isPostLiked(final String postId, final ImageButton imageButton){
        final FirebaseAuth auth = FirebaseAuth.getInstance();

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Memes")
                .child(postId).child("likes");

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.child(auth.getCurrentUser().getUid()).exists()){
                    imageButton.setImageResource(R.drawable.fav_filled);
                    imageButton.setColorFilter(Color.RED);
                    imageButton.setTag("liked");
                }else{
                    imageButton.setImageResource(R.drawable.fav_outlined);
                    imageButton.setColorFilter(Color.BLACK);
                    imageButton.setTag("like");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e(TAG, "onCancelled: DataBaseError" + error.getMessage());
            }
        });
    }

    public static void getLikesCount(final String postId, final TextView likesCountView){
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

    public static Uri saveImage(ImageView saveImage, Context context){
        OutputStream outputStream = null;
        BitmapDrawable bitmapDrawable= (BitmapDrawable) saveImage.getDrawable();
        Bitmap bitmap=null;
        if(bitmapDrawable!=null) {
            bitmap = bitmapDrawable.getBitmap();
        }else{
            Log.d(TAG, "onClick: Error!!!!!!!!!!!");
            return null;
        }

        //Check condition
        if(ActivityCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED){
            //If permission granted
            File filepath= Environment.getExternalStorageDirectory();
            File dir=new File(filepath.getAbsolutePath()+"/MemeStore/");
            Log.d(TAG, "onClick: "+filepath.getAbsolutePath()+"/MemeStore/");
            if(!dir.exists()){
                Toast.makeText(context,
                        (dir.mkdirs() ? "Directory has been created" : "Directory not created"),
                        Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(context, "Directory exists", Toast.LENGTH_SHORT).show();
            }
            File file=new File(dir,System.currentTimeMillis()+".jpg");
            try {
                outputStream=new FileOutputStream(file);
            } catch (Exception e) {
                e.printStackTrace();
                Log.d(TAG, "saveImage: "+e.getMessage());
            }
            Log.d(TAG, "saveImage: "+bitmap+">>>"+outputStream);
            if(bitmap!=null && outputStream!=null) {
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
                Toast.makeText(context, "Image Saved!!", Toast.LENGTH_SHORT).show();
            }else{
                Log.d(TAG, "onClick: Error");
                Toast.makeText(context, "Error while downloading image", Toast.LENGTH_SHORT).show();
            }
            if(outputStream!=null) {
                try {
                    outputStream.flush();
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            file.setReadable(true,false);
            Uri photoURI = FileProvider.getUriForFile(context.getApplicationContext(), BuildConfig.APPLICATION_ID +".provider", file);
            Log.d(TAG, "saveImage: Photo Uri: " + photoURI);
            return photoURI;
        }else{
            //Request permission
            Log.d(TAG, "onClick: Permission is not given!!!");
        }

        return  null;
    }

    public static void shareImage(ImageView shareImage,Context context){
        Uri shareImageUri = saveImage(shareImage,context);
        Log.d(TAG, "onClick: Got a Uri of image to send: " + shareImageUri);
        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.putExtra(Intent.EXTRA_STREAM, shareImageUri);
        shareIntent.putExtra(Intent.EXTRA_TEXT,"Shared from MemeStore");
        shareIntent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        shareIntent.setType("*/*");
        context.startActivity(Intent.createChooser(shareIntent,"Share via"));
    }

    public static void likeClicked(View v,String postId){
        if(v.getTag().equals("like")){
            FirebaseDatabase.getInstance().getReference("Memes").child(postId)
                    .child("likes").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(true);

            FirebaseDatabase.getInstance().getReference("Users").child(FirebaseAuth.getInstance().getCurrentUser()
                    .getUid()).child("likedPosts").child(postId).setValue(true);
        }else{
            FirebaseDatabase.getInstance().getReference("Memes").child(postId)
                    .child("likes").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).removeValue();

            FirebaseDatabase.getInstance().getReference("Users").child(FirebaseAuth.getInstance().getCurrentUser()
                    .getUid()).child("likedPosts").child(postId).removeValue();
        }
    }
}
