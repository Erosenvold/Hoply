package com.example.test.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.test.tables.Users;

import java.util.List;

@Dao
public interface UsersDao {
    @Query("SELECT * FROM hoply_users")
    public List<Users> getAllUsers();

    @Query("SELECT id FROM hoply_users WHERE id = :userID")
    public String getUserID(String userID);

    @Query("SELECT id FROM hoply_users WHERE id = :userID AND password =:password")
    public String getUserIDLogin(String userID,String password);

    @Query("SELECT name FROM hoply_users WHERE id = :userID")
    public String getUsernameFromID(String userID);

    @Query("SELECT profile_text FROM hoply_users WHERE id = :userID")
    public String getProfileTxtFromID(String userID);

    @Query("SELECT profile_image FROM hoply_users WHERE id = :userID")
    public String getProfileImageFromID(String userID);

    @Query("UPDATE hoply_users SET profile_text = :profileTxt WHERE id = :sessionId")
    public void createNewProfileTxt(String profileTxt, String sessionId);

    @Query("UPDATE hoply_users SET profile_image = :profileImage WHERE id = :sessionId")
    public void createNewProfileImage(String profileImage, String sessionId);

    @Insert(onConflict = OnConflictStrategy.FAIL)
    public void createNewUser(Users user);

    @Query("DELETE FROM hoply_users")
    public void deleteAllUsers();

}
