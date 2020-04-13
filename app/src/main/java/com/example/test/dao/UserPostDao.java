package com.example.test.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.test.tables.UserPost;

import java.util.List;

@Dao
public interface UserPostDao {
    @Query("SELECT * FROM hoply_user_post")
    public List<UserPost> getAllUserPost();
    @Insert(onConflict = OnConflictStrategy.FAIL)
    public void createUserPost(UserPost userPost);
}
