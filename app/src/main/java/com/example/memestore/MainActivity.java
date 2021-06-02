package com.example.memestore;

import android.Manifest;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.example.memestore.general_classes.User;
import com.example.memestore.ui.main.SectionsPagerAdapter;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    DrawerLayout drawerLayout;
    Toolbar toolbar;
    ActionBarDrawerToggle actionBarDrawerToggle;
    NavigationView navigationView;
    private final int PICK_IMAGE_REQUEST = 1;
    private final int PICK_USER_IMAGE_REQUEST = 2;
    private ImageView userImage;
    private TextView userName;
    private int tabIndex;
    private boolean LoginStatus=false;
    private FrameLayout frameLayout;
    private BottomAppBar bottomAppBar;
    private BottomNavigationView bottomNavigationView;
    private BottomNavigationView.OnNavigationItemSelectedListener navListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {

            Fragment selectedFragment = null;
            switch(item.getItemId())
            {
                case R.id.miHome:
                    selectedFragment = new memeFragment(getApplicationContext());
                    break;

                case R.id.miSettings:
                    selectedFragment = new SettingsFragment();
                    break;

                case R.id.miProfile:
                    selectedFragment = new ProfileFragment();
                    break;

                case R.id.miSearch:
                    selectedFragment = new SearchFragment();
                    break;
            }

            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,selectedFragment).commit();
            return true;
        }
    };

    @Override
    protected void onStart() {
        Log.d(TAG, "onStart: Called");

        super.onStart();
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d(TAG, "onCreate: Called");

        bottomAppBar = findViewById(R.id.bottomAppBar);
        int bottomAppBarHeight = bottomAppBar.getHeight();
        frameLayout = findViewById(R.id.fragment_container);
        ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) frameLayout.getLayoutParams();
        params.setMargins(0,0,0,bottomAppBarHeight);
        frameLayout.setLayoutParams(params);

        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnNavigationItemSelectedListener(navListener);

        bottomNavigationView.setBackground(null);

        //Disabling the touch action on placeholder in the bottom navigation view at the middle
        bottomNavigationView.getMenu().getItem(2).setEnabled(false);

        ActivityCompat.requestPermissions(MainActivity.this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE},1);

        checkLoginStatus();
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new memeFragment(getApplicationContext())).commit();

//        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager());
//        ViewPager viewPager = findViewById(R.id.view_pager);
//        viewPager.setAdapter(sectionsPagerAdapter);
//        TabLayout tabs = findViewById(R.id.tabs);
//        tabs.setupWithViewPager(viewPager);
//        tabIndex = tabs.getSelectedTabPosition();
        FloatingActionButton fab = findViewById(R.id.fab);
//        setUpToolbar();


//        tabs.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
//            @Override
//            public void onTabSelected(TabLayout.Tab tab) {
//                tabIndex = tab.getPosition();
//                Log.d(TAG, "onTabSelected: Tab selected with index: " + tabIndex);
//            }
//
//            @Override
//            public void onTabUnselected(TabLayout.Tab tab) {
//
//            }
//
//            @Override
//            public void onTabReselected(TabLayout.Tab tab) {
//
//            }
//        });
//        navigationView=findViewById(R.id.navigation_menu);
//        userImage = navigationView.getHeaderView(0).findViewById(R.id.userImage);
//        userName=navigationView.getHeaderView(0).findViewById(R.id.userName);
//        userImage.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                openPhotoPicker(PICK_USER_IMAGE_REQUEST);
//            }
//        });



        addEventListenerToUserDetails();

//        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
//            @Override
//            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
//                switch (item.getItemId()){
//                    case R.id.logout:
//                        FirebaseAuth.getInstance().signOut();
//                        Log.d(TAG, "onNavigationItemSelected: User signed out!");
//                        Intent intent = new Intent(MainActivity.this,LoginActivity.class);
//                        startActivity(intent);
//                        break;
//
//                    default:
//                }
//
//                return true;
//            }
//        });
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openPhotoPicker(PICK_IMAGE_REQUEST);
            }
        });

        // Checking whether user is logged in or not
//        if(LoginStatus==false) return;
    }

    private void setUpToolbar() {
//        drawerLayout = findViewById(R.id.drawerLayout);
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
                    String postDatabasePath = null;

                    switch(tabIndex){
                        case 0:
                            postDatabasePath = "Memes";
                            break;

                        case 1:
                            postDatabasePath = "Quotes";
                            break;

                        case 2:
                            postDatabasePath = "Facts";
                            break;
                    }
                    intent.putExtra("PATH",postDatabasePath);
                    startActivity(intent);
                    break;

                case PICK_USER_IMAGE_REQUEST:
                    Log.d(TAG, "onActivityResult: Called with request Code: " + PICK_USER_IMAGE_REQUEST);
                    Log.d(TAG, "onActivityResult: Successfully Picked an image with URI: " + data.getData());
                    Uri profileImgUri=data.getData();
                    final DatabaseReference userDatabaseReference= FirebaseDatabase.getInstance().getReference("Users");
                    final StorageReference userStorageReference=FirebaseStorage.getInstance().getReference("Users");
                    final String userUID=FirebaseAuth.getInstance().getCurrentUser().getUid();
                        userStorageReference.child(userUID)
                                .child("profile-pic")
                                .putFile(profileImgUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                Log.d(TAG, "onSuccess: Profile Pic updated successfully");
                                    userStorageReference.child(userUID+"/profile-pic").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                        @Override
                                        public void onSuccess(Uri uri) {
                                           String downloadUrl=uri.toString();
                                            Log.d(TAG, "onSuccess: Image url is "+downloadUrl);
                                            Picasso.get().load(downloadUrl).into(userImage);
                                            userDatabaseReference.child(userUID).child("userProfilePic").setValue(downloadUrl);

                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Log.d(TAG, "onFailure: Error: "+e.getMessage());
                                        }
                                    });
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.d(TAG, "onFailure: Error while changing profile pic!!");
                            }
                        });
            }
        }else{
            Toast.makeText(this,"Error Opening Image",Toast.LENGTH_SHORT).show();
        }
    }
    public void addEventListenerToUserDetails(){

        final String authorUid=FirebaseAuth.getInstance().getCurrentUser().getUid();
        FirebaseDatabase.getInstance().getReference("Users/"+ authorUid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                FirebaseDatabase.getInstance().getReference("Users").child(authorUid).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        Log.d(TAG, "onDataChange: Called");
                        User user = snapshot.getValue(User.class);
                        Log.d(TAG, "onDataChange: Post Author Name: " + user.getUserName());
                        Log.d(TAG, "onDataChange: Profile Pic Url: "+user.getUserProfilePic());
//                        Picasso.get().load(user.getUserProfilePic()).into(userImage);
//                        userName.setText(user.getUserName());
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Log.e(TAG, "onCancelled: "+error.getMessage());
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(MainActivity.this,error.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void checkLoginStatus() {
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
            FirebaseUser currentUser = firebaseAuth.getCurrentUser();
            GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
            if (currentUser != null || account!=null) {
                Log.d(TAG, "checkLoginStatus: User already logged in: ");
                LoginStatus= true;

            } else{
                Log.d(TAG, "User has to log in");
                LoginStatus= false;
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();

            }
        LoginStatus= false;
    }
}