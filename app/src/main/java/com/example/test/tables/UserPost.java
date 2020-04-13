package com.example.test.tables;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(tableName = "hoply_user_post",indices = {@Index(value = "user_post_id",unique = true)},foreignKeys = {
        @ForeignKey(entity = Users.class,parentColumns = "user_id",childColumns = "user_id"),
        @ForeignKey(entity = Posts.class,parentColumns = "post_id",childColumns = "post_id")})
public class UserPost {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "user_post_id")
    @NonNull public int userPostId;

    @ColumnInfo(name = "user_id")
    public int userId;

    @ColumnInfo(name = "post_id")
    public int postId;
}
