package com.example.instagramclone.model;

public class Post {
    public String postId;
    public String email;
    public String comment;
    public String downloadUrl;
    public String date;

    public Post(String email, String comment, String downloadUrl, String date) {
        this.email = email;
        this.comment = comment;
        this.downloadUrl = downloadUrl;
        this.date = date;
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
