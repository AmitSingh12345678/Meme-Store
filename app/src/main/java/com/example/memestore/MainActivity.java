package com.example.memestore;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;

import androidx.annotation.LongDef;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.memestore.ui.main.SectionsPagerAdapter;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    DrawerLayout drawerLayout;
    Toolbar toolbar;
    ActionBarDrawerToggle actionBarDrawerToggle;
    NavigationView navigationView;
    private final int PICK_IMAGE_REQUEST = 1;
    private final int PICK_USER_IMAGE_REQUEST = 2;
    private ImageView userImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager());
        ViewPager viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(sectionsPagerAdapter);
        TabLayout tabs = findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);
        FloatingActionButton fab = findViewById(R.id.fab);
        setUpToolbar();
        navigationView=findViewById(R.id.navigation_menu);
        userImage = navigationView.getHeaderView(0).findViewById(R.id.userImage);
        userImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openPhotoPicker(PICK_USER_IMAGE_REQUEST);
            }
        });

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    default:
                }

                return true;
            }
        });
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openPhotoPicker(PICK_IMAGE_REQUEST);
            }
        });
    }

    private void setUpToolbar() {
        drawerLayout = findViewById(R.id.drawerLayout);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);//we have to import androidx.appcompat.widget.Toolbar,as AS import old one
        /**
         * We use this as in style.xml,we set NoActionBar.It set this toolbar as actionbar.
         * Press ctrl+Q for more information.
         */
        /**
         * ActionBarDrawerToggle:This class provides a handy way to tie together the functionality of DrawerLayout
         * and the framework ActionBar to implement the recommended design for navigation drawers.
         * It is an helper class which helps us to sync both drawerLayout and toolbar.
         * In the fourth and fifth parameter,we write when menu gets openned and closed respectively.
         */
        actionBarDrawerToggle=new ActionBarDrawerToggle(this,drawerLayout,toolbar,//CTRL+Q for more info
                R.string.app_name,R.string.app_name);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();//CTRL+Q for more info
    }

    private void openPhotoPicker(int requestType){
        Intent intent =  new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, requestType);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == RESULT_OK && data != null && data.getData() != null){
            switch(requestCode){
                case PICK_IMAGE_REQUEST:
                    Log.d(TAG, "onActivityResult: Called with request Code: " + PICK_IMAGE_REQUEST);
                    Log.d(TAG, "onActivityResult: Successfully picked an image with URI: " + data.getData());
                    Intent intent = new Intent(MainActivity.this,UploadPostActivity.class);
                    intent.putExtra("imageUri",data.getData().toString());
                    startActivity(intent);
                    break;

                case PICK_USER_IMAGE_REQUEST:
                    Log.d(TAG, "onActivityResult: Called with request Code: " + PICK_USER_IMAGE_REQUEST);
                    Log.d(TAG, "onActivityResult: Successfully Picked an image with URI: " + data.getData());
                    Toast.makeText(this,"Enter code to upload image",Toast.LENGTH_SHORT).show();
            }
        }else{
            Toast.makeText(this,"Error Opening Image",Toast.LENGTH_SHORT).show();
        }
    }
}