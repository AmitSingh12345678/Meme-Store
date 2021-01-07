package com.example.memestore.general_classes;

public class Post {
//    private String postAuthorName = "Meme Bot";
//    private String postAuthorImageUrl = "https://www.thespruce.com/thmb/kPFArxxQPDx6o4vXjeORPx6HZDc=/2119x1192/smart/filters:no_upscale()/GettyImages-971582964-ee0f28aa66b04fb1a54171fa4bdee7a6.jpg";
    private String userUID;
    private String postImageUrl;
    private String postTags;
    private String postName;
    private int likes = 50;

    public String getUserUID() {
        return userUID;
    }


    public void setUserUID(String userUID) {
        this.userUID = userUID;
    }

    public Post(){
        //Empty Constructor needed
    }

    public Post(String post_name,String post_image_url){
        postName = post_name;
        postImageUrl = post_image_url;
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
    @Override
    public String toString() {
        return "Post{" +
                "userUID='" + userUID + '\'' +
                ", postImageUrl='" + postImageUrl + '\'' +
                ", postTags='" + postTags + '\'' +
                ", postName='" + postName + '\'' +
                ", likes=" + likes +
                '}';
    }


}
