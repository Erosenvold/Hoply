package com.example.test.dao;

import androidx.room.Dao;
import androidx.room.Query;

import com.example.test.tables.Comments;

import java.util.List;

@Dao
public interface CommentsDao {
    @Query("SELECT * FROM hoply_comment")
    public List<Comments> getAllComments();
}
