package com.example.test.tables;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(tableName = "hoply_post",indices = {@Index(value = "id",unique = true)},
        foreignKeys = @ForeignKey(entity = Users.class,parentColumns = "id",childColumns = "user_id" ))
public class Posts {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    @NonNull public int postID;

    @ColumnInfo(name="user_id")
    public String userID;

    @ColumnInfo(name = "content")
    public String postContent;

    @ColumnInfo(name = "timestamp")
    public long timeCreated;

    @ColumnInfo(name="post_image")
    public String postImage;

    @ColumnInfo(name="post_rating")
    public int postRating;

    @ColumnInfo(name="location")
    public String location;

    @Override
    public String toString(){return postContent;}

}
