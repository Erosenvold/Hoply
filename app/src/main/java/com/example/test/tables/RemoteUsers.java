package com.example.test.tables;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RemoteUsers {

    @SerializedName("id")

    private String id;

    @SerializedName("name")

    private String name;

    @SerializedName("stamp")

    private String stamp;

    public String getId() {
        return id;
    }

    public String getStamp() {
        return stamp;
    }

    public String getName() {
        return name;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setStamp(String stamp) {
        this.stamp = stamp;
    }

    public RemoteUsers(String id, String name, String stamp){
        this.name = name;
        this.stamp = stamp;
        this.id = id;
    }
}
