package com.example.test.tables;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(tableName = "hoply_comment", indices = {@Index(value = {"user_id","post_id","time_created"},unique = true)},foreignKeys = {
        @ForeignKey(entity = Users.class, parentColumns = "id",childColumns = "user_id"),
        @ForeignKey(entity = Posts.class, parentColumns = "id",childColumns = "post_id")
},primaryKeys = {"user_id","post_id","time_created"})
public class Comments {

    @ColumnInfo(name="user_id")
    @NonNull public String userID;


    @ColumnInfo(name= "post_id")
    @NonNull public int postID;


    @ColumnInfo(name = "time_created")
    @NonNull public long timeCreated;

    @ColumnInfo(name = "comment_content")
    public String commentContent;

    @Override
    public String toString(){return commentContent;}
}
