package com.example.test.dao;

import androidx.room.Dao;
import androidx.room.Query;

import com.example.test.tables.PostUpvotes;

import java.util.List;

@Dao
public interface PostUpvotesDao {
    @Query("SELECT * FROM hoply_post_upvotes")
    public List<PostUpvotes> getAllPostUpvotes();
}
