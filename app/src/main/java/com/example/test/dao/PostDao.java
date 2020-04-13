package com.example.test.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.test.tables.Posts;

import java.util.List;

@Dao
public interface PostDao {
    @Query("SELECT * FROM hoply_post")
    public List<Posts> getAllPosts();
    @Insert(onConflict = OnConflictStrategy.FAIL)
    public void createNewPost(Posts posts);
}
