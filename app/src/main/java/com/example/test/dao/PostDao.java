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

    @Query("SELECT id FROM hoply_post ORDER BY stamp DESC LIMIT 10")
    public int[] getAllIDDESC();

    @Query("SELECT stamp FROM hoply_post ORDER BY stamp DESC")
    public String[] getAllStampsDESC();

    @Query("SELECT content FROM hoply_post WHERE id=:postID")
    public String getContentFromID(int postID);

    @Query("SELECT user_id FROM hoply_post WHERE id=:postID")
    public String getUserID(int postID);

    @Query("SELECT id FROM hoply_post WHERE id = :postID")
    public int getPostID(int postID);

    @Query("SELECT id FROM hoply_post WHERE user_id= :userID AND stamp = :timeCreated")
    public int getPostID(String userID, long timeCreated);

    @Insert(onConflict = OnConflictStrategy.FAIL)
    public void createNewPost(Posts posts);

    @Query("SELECT stamp FROM hoply_post WHERE id= :postID")
    public String getTimestampFromID(int postID);


    @Query("DELETE FROM hoply_post WHERE id >= 0")
    public void deleteAllPosts();





}
