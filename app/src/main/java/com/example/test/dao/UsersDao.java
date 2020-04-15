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

    @Insert(onConflict = OnConflictStrategy.FAIL)
    public void createNewUser(Users user);
}
