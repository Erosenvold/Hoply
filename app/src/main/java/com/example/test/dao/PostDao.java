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
    @Query("SELECT id FROM hoply_post WHERE id = :postID")
    public int getPostID(int postID);

    @Insert(onConflict = OnConflictStrategy.FAIL)
    public long createNewPost(Posts posts);
}
