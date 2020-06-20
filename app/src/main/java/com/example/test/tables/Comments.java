package com.example.test.tables;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Index;

//Local table of comments
@Entity(tableName = "hoply_comment", indices = {@Index(value = {"user_id","post_id","stamp"},unique = true)},primaryKeys = {"user_id","post_id","stamp"})
public class Comments {

    //Returns user ID
    @ColumnInfo(name="user_id")
    @NonNull public String userID;

    //Returns post ID
    @ColumnInfo(name= "post_id")
    @NonNull public int postID;

    //Returns time stamp
    @ColumnInfo(name = "stamp")
    @NonNull public String timeCreated;

    //Returns content
    @ColumnInfo(name = "content")
    public String commentContent;

    @Override
    public String toString(){return commentContent;}
}
