package com.example.test.tables;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(tableName = "hoply_post",indices = {@Index(value = "id",unique = true)})
public class Posts {
    @PrimaryKey
    @ColumnInfo(name = "id")
    @NonNull public int postID;

    @ColumnInfo(name="user_id")
    public String userID;

    @ColumnInfo(name = "content")
    public String postContent;

    @ColumnInfo(name = "stamp")
    public String timeCreated;

    @Override
    public String toString(){return postContent;}

}
