package com.example.test.tables;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(tableName = "hoply_user_comment",indices = {@Index(value="user_comment_id",unique = true)},foreignKeys = {
        @ForeignKey(entity = Users.class,parentColumns = "user_id",childColumns = "user_id"),
        @ForeignKey(entity = Comments.class,parentColumns = "comment_id",childColumns = "comment_id")})
public class UserComment {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "user_comment_id")
    @NonNull public int userCommentId;

    @ColumnInfo(name = "user_id")
    public int userId;

    @ColumnInfo(name="comment_id")
    public int commentId;
}
