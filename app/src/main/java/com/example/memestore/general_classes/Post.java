package com.example.memestore.general_classes;

import androidx.annotation.NonNull;

public class Post {
    private String postAuthorName = "Sanchit Khurana";
    private String postAuthorImageUrl = "https://www.thespruce.com/thmb/kPFArxxQPDx6o4vXjeORPx6HZDc=/2119x1192/smart/filters:no_upscale()/GettyImages-971582964-ee0f28aa66b04fb1a54171fa4bdee7a6.jpg";
    private String postImageUrl;
    private String postTags;
    private String postName;
    private int likes = 50;

    public String getPostAuthorName() {
        return postAuthorName;
    }

    public void setPostAuthorName(String postAuthorName) {
        this.postAuthorName = postAuthorName;
    }

    public String getPostAuthorImageUrl() {
        return postAuthorImageUrl;
    }

    public void setPostAuthorImageUrl(String postAuthorImageUrl) {
        this.postAuthorImageUrl = postAuthorImageUrl;
    }

    public String getPostImageUrl() {
        return postImageUrl;
    }

    public void setPostImageUrl(String postImageUrl) {
        this.postImageUrl = postImageUrl;
    }

    public int getLikes() {
        return likes;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }

    public String getPostTags() {
        return postTags;
    }

    public void setPostTags(String postTags) {
        this.postTags = postTags;
    }

    public String getPostName() {
        return postName;
    }

    public void setPostName(String postName) {
        this.postName = postName;
    }

    @NonNull
    @Override
    public String toString() {
        return  "Name: " + postName + "\n" +
                "Author: " + postAuthorName + "\n" +
                "Tags: " + postTags + "\n" +
                "Image Url: " + postImageUrl + "\n" +
                "Author image url: " + postAuthorImageUrl + "\n" +
                "Likes: " + likes + "\n";

    }
}
