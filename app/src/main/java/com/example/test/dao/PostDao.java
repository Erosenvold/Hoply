package com.example.test.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import com.example.test.tables.Posts;
import java.util.List;
//Data Access Object For Local DB Posts
@Dao
public interface PostDao {
    //Used for DB connection
    @Query("SELECT * FROM hoply_post")
    public List<Posts> getAllPosts();

    //Returns 10 newest post ID's in array
    @Query("SELECT id FROM hoply_post ORDER BY stamp DESC LIMIT 10")
    public int[] getLimitedPostIDDESC();

    //Returns newest timestamps in array
    @Query("SELECT stamp FROM hoply_post ORDER BY stamp DESC")
    public String[] getAllStampsDESC();

    //Returns content that matches a given post ID
    @Query("SELECT content FROM hoply_post WHERE id=:postID")
    public String getContentFromID(int postID);

    //Returns user ID that matches a given post ID
    @Query("SELECT user_id FROM hoply_post WHERE id=:postID")
    public String getUserID(int postID);

    //Inserts new post
    @Insert(onConflict = OnConflictStrategy.FAIL)
    public void createNewPost(Posts posts);

    //DELETES ALL POSTS IN LOCAL DB!
    @Query("DELETE FROM hoply_post")
    public void deleteAllPosts();





}
