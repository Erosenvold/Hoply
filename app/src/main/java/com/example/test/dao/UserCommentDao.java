package com.example.test.dao;

import androidx.room.Dao;
import androidx.room.Query;

import com.example.test.tables.UserComment;

import java.util.List;

@Dao
public interface UserCommentDao {
    @Query("SELECT * FROM hoply_user_comment")
    public List<UserComment> getAllUserComment();
}
