package com.example.test.tables;

import com.google.gson.annotations.SerializedName;

//Remote table getters and setters for Comments
public class RemoteComments {

    @SerializedName("post_id")
    private int post_id;

    @SerializedName("user_id")
    private String user_id;

    @SerializedName("stamp")
    private String stamp;

    @SerializedName("content")
    private String content;

    public String getContent() {
        return content;
    }

    public int getPost_id() {
        return post_id;
    }

    public String getStamp() {
        return stamp;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setPost_id(int post_id) {
        this.post_id = post_id;
    }

    public void setContent(String content){
        this.content = content;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public void setStamp(String stamp) {
        this.stamp = stamp;
    }

    public RemoteComments(int post_id, String user_id, String content, String stamp){
        this.user_id = user_id;
        this.content = content;
        this.stamp = stamp;
        this.post_id = post_id;
    }
}
