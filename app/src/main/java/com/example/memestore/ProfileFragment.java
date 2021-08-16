package com.example.memestore;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.memestore.general_classes.MemeGalleryAdapter;
import com.example.memestore.general_classes.Post;
import com.example.memestore.general_classes.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ProfileFragment extends Fragment {

    private static final String TAG = "ProfileFragment";
    private Button yourPosts;
    private Button likedPosts;
    private Fragment selectedFragment=null;
    private final float margin_bottom= (float) 2.0;
    private DatabaseReference mDatabaseReference;
    private FirebaseAuth mAuth;
    private ImageView userImageView;
    private TextView userNameView;
    private MemeGalleryAdapter mAdapter;
    private ArrayList<Post> userPostList;
    private ArrayList<Post> likedPostList;
    private FragmentManager fragmentManager=null;
    FirebaseAuth auth;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_profile,container,false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        yourPosts=view.findViewById(R.id.yourPosts);
        likedPosts=view.findViewById(R.id.likedPosts);
        userImageView = view.findViewById(R.id.userImage);
        userNameView = view.findViewById(R.id.profile_username);

        fragmentManager=requireActivity().getSupportFragmentManager();

        yourPosts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                float dpRatio = getContext().getResources().getDisplayMetrics().density;
                int pixelForDp = (int) ((int)margin_bottom * dpRatio);
                setMargins(yourPosts,0,0,0,pixelForDp);
                setMargins(likedPosts,0,0,0,0);
                selectedFragment=new userPostFragment(userPostList);
                getActivity().getSupportFragmentManager().beginTransaction().
                        replace(R.id.fragment_container_profile,selectedFragment).commit();
            }
        });
        likedPosts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                float dpRatio = getContext().getResources().getDisplayMetrics().density;
                int pixelForDp = (int) ((int)margin_bottom * dpRatio);
                setMargins(likedPosts,0,0,0,pixelForDp);
                setMargins(yourPosts,0,0,0,0);
                selectedFragment=new likedPostFragment(likedPostList);
                getActivity().getSupportFragmentManager().beginTransaction().
                        replace(R.id.fragment_container_profile,selectedFragment).commit();
            }
        });

        mDatabaseReference = FirebaseDatabase.getInstance().getReference("Users");
        mAuth = FirebaseAuth.getInstance();

        final String currentUserId = mAuth.getCurrentUser().getUid();

        mDatabaseReference.child(currentUserId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User currentUser = snapshot.getValue(User.class);
                Log.d(TAG, "onDataChange: Current User Details: " + currentUser.toString());
                Picasso.get().load(currentUser.getUserProfilePic()).into(userImageView);
                userNameView.setText(currentUser.getUserName());
                userPostList = getPostList(currentUser.getPosts());
                likedPostList = getPostList(currentUser.getLikedPosts());
                selectedFragment = new userPostFragment(userPostList);
                if(fragmentManager!=null)
                fragmentManager.beginTransaction().
                        replace(R.id.fragment_container_profile,selectedFragment).commit();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public ArrayList<Post> getPostList(final ArrayList<String> postId){

        DatabaseReference memref = FirebaseDatabase.getInstance().getReference("Memes");
        ArrayList<Post> ans = new ArrayList<>();
        for(int i=0;i<postId.size();i++){
            final ArrayList<Post> finalAns = ans;
            final int finalI = i;
            memref.child(postId.get(i)).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    Post currentPost = snapshot.getValue(Post.class);
                    currentPost.setPostId(postId.get(finalI));
                    finalAns.add(currentPost);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }

        return ans;
    }

    public static void setMargins (View v, int l, int t, int r, int b) {
        if (v.getLayoutParams() instanceof ViewGroup.MarginLayoutParams) {
            ViewGroup.MarginLayoutParams p = (ViewGroup.MarginLayoutParams) v.getLayoutParams();
            p.setMargins(l, t, r, b);
            v.requestLayout();
        }
    }
}
