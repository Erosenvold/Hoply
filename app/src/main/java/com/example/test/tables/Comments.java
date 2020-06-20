package com.example.test.tables;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Index;

//Erik Dao
@Entity(tableName = "hoply_comment", indices = {@Index(value = {"user_id","post_id","stamp"},unique = true)},primaryKeys = {"user_id","post_id","stamp"})
public class Comments {

    @ColumnInfo(name="user_id")
    @NonNull public String userID;


    @ColumnInfo(name= "post_id")
    @NonNull public int postID;


    @ColumnInfo(name = "stamp")
    @NonNull public String timeCreated;

    @ColumnInfo(name = "content")
    public String commentContent;

    @Override
    public String toString(){return commentContent;}
}
