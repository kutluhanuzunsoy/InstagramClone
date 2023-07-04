package com.example.instagramclone.model;

public class Post {
    public String postId;
    public String email;
    public String comment;
    public String downloadUrl;

    public Post(String email, String comment, String downloadUrl) {
        this.email = email;
        this.comment = comment;
        this.downloadUrl = downloadUrl;
    }

    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }

    public String getEmail() {
        return email;
    }
}
