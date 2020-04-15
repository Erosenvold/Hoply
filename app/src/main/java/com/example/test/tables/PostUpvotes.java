package com.example.test.tables;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(tableName = "hoply_post_upvotes",indices = {@Index(value={"post_id","user_id"})},foreignKeys = {
        @ForeignKey(entity = Posts.class,parentColumns = "id",childColumns = "post_id"),
        @ForeignKey(entity = Users.class,parentColumns = "id",childColumns = "user_id")
},primaryKeys = {"post_id","user_id"})
public class PostUpvotes {

    @ColumnInfo(name="post_id")
    @NonNull public int postID;


    @ColumnInfo(name = "user_id")
    @NonNull public int userID;

    @ColumnInfo(name = "post_upvoted")
    public int postUpvoted;
}
