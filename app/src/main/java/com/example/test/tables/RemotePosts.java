package com.example.test.tables;
import com.google.gson.annotations.SerializedName;



public class RemotePosts {

    @SerializedName("id")
    private int id;

    @SerializedName("user_id")
    private String user_id;

    @SerializedName("stamp")
    private String stamp;

    @SerializedName("content")
    private String content;


    public String getContent() {
        return content;
    }

    public int getId() {
        return id;
    }

    public String getStamp() {
        return stamp;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setId(int id) {
        this.id = id;
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

    public RemotePosts(int id, String user_id, String content, String stamp){
        this.user_id = user_id;
        this.content = content;
        this.stamp = stamp;
        this.id = id;
    }
}
