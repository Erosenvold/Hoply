package com.example.test.tables;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(tableName = "hoply_comment", indices = {@Index(value = "comment_id",unique = true)})
public class Comments {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name="comment_id")
    @NonNull public int commentId;

    @ColumnInfo(name = "comment_content")
    public String commentContent;

    @ColumnInfo(name = "time_created")
    public long timeCreated;

    @Override
    public String toString(){return commentContent;}
}
