package com.example.memestore;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class ProfileFragment extends Fragment {

    private Button yourPosts;
    private Button likedPosts;
    private Fragment selectedFragment=null;
    private final float margin_bottom= (float) 2.0;

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
        selectedFragment=new userPostFragment();
        getActivity().getSupportFragmentManager().beginTransaction().
                replace(R.id.fragment_container_profile,selectedFragment).commit();
        yourPosts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                float dpRatio = getContext().getResources().getDisplayMetrics().density;
                int pixelForDp = (int) ((int)margin_bottom * dpRatio);
                setMargins(yourPosts,0,0,0,pixelForDp);
                setMargins(likedPosts,0,0,0,0);
                selectedFragment=new userPostFragment();
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
                selectedFragment=new likedPostFragment();
                getActivity().getSupportFragmentManager().beginTransaction().
                        replace(R.id.fragment_container_profile,selectedFragment).commit();
            }
        });
    }

    public static void setMargins (View v, int l, int t, int r, int b) {
        if (v.getLayoutParams() instanceof ViewGroup.MarginLayoutParams) {
            ViewGroup.MarginLayoutParams p = (ViewGroup.MarginLayoutParams) v.getLayoutParams();
            p.setMargins(l, t, r, b);
            v.requestLayout();
        }
    }
}
