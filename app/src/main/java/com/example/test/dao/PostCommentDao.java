package com.example.test.dao;

import androidx.room.Dao;
import androidx.room.Query;

import com.example.test.tables.PostComment;

import java.util.List;

@Dao
public interface PostCommentDao {
    @Query("SELECT * FROM hoply_post_comment")
    public List<PostComment> getAllPostComment();
}
