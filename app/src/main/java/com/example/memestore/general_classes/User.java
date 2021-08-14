package com.example.memestore.general_classes;

import java.util.ArrayList;
import java.util.HashMap;

public class User {
    private String userName;
    private String userProfilePic;
    private HashMap<String, Boolean> posts;
    private HashMap<String, Boolean> likedPosts;

    public ArrayList<String> getPosts() {
        ArrayList<String> temp = new ArrayList<>();
        if(posts!=null)
        temp.addAll(posts.keySet());

        return temp;
    }

    public void setPosts(HashMap<String,Boolean> posts) {
        this.posts = posts;
    }

    public ArrayList<String> getLikedPosts() {
        ArrayList<String> temp = new ArrayList<>();
        if(likedPosts!=null)
        temp.addAll(likedPosts.keySet());

        return temp;
    }

    public void setLikedPosts(HashMap<String,Boolean> likedPosts) {
        this.likedPosts = likedPosts;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserProfilePic() {
        return userProfilePic;
    }

    public void setUserProfilePic(String userProfilePic) {
        this.userProfilePic = userProfilePic;
    }
}
