package com.example.test.tables;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(tableName = "hoply_post_comment",indices = {@Index(value = "post_comment_id",unique = true)},foreignKeys = {
        @ForeignKey(entity = Posts.class,parentColumns = "post_id",childColumns = "post_id"),
        @ForeignKey(entity = Comments.class,parentColumns = "comment_id",childColumns = "comment_id")
})
public class PostComment {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "post_comment_id")
    @NonNull public int postCommentId;

    @ColumnInfo(name = "post_id")
    public int postId;

    @ColumnInfo(name = "comment_id")
    public int commentId;
}
