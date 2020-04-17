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

    @Query("SELECT id FROM hoply_post ORDER BY timestamp ASC")
    public int[] getAllIDASC();

    @Query("SELECT content FROM hoply_post WHERE id=:postID")
    public String[] getAllContent(int postID);

    @Query("SELECT id FROM hoply_post WHERE id = :postID")
    public int getPostID(int postID);

    @Query("SELECT id FROM hoply_post WHERE user_id= :userID AND timestamp = :timeCreated")
    public int getPostID(String userID, long timeCreated);

    @Insert(onConflict = OnConflictStrategy.FAIL)
    public void createNewPost(Posts posts);

    @Query("UPDATE hoply_post SET post_image = :postImage WHERE id = :postID")
    public void createNewPostImage(String postImage, int postID);

}
