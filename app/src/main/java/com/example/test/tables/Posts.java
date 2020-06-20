package com.example.test.tables;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

//Local table of Posts
@Entity(tableName = "hoply_post",indices = {@Index(value = "id",unique = true)})
public class Posts {
    //Returns post ID - Sets as primary key
    @PrimaryKey
    @ColumnInfo(name = "id")
    @NonNull public int postID;

    //Returns user ID
    @ColumnInfo(name="user_id")
    public String userID;

    //Returns content
    @ColumnInfo(name = "content")
    public String postContent;

    //Returns time stamp
    @ColumnInfo(name = "stamp")
    public String timeCreated;

    @Override
    public String toString(){return postContent;}

}
