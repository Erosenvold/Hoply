package com.example.test.tables;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(tableName = "hoply_post",indices = {@Index(value = "post_id",unique = true)})
public class Posts {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "post_id")
    @NonNull public int postId;

    @ColumnInfo(name = "post_content")
    public String postContent;

    @ColumnInfo(name = "time_created")
    public long timeCreated;

    @Override
    public String toString(){return postContent;}

}
