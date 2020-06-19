package com.example.test.tables;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class JoinPost {

    @SerializedName("posts")
    @Expose
    private RemotePosts remotePosts;

    public RemotePosts getRemotePosts() {
        return remotePosts;
    }

    public void setRemotePosts(RemotePosts remotePosts) {


    }
}
