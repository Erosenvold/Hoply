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

    @Query("SELECT username FROM hoply_users WHERE username = :username")
    public String getUsername(String username);

    @Query("SELECT username FROM hoply_users WHERE username = :username AND password =:password")
    public String getUsernameLogin(String username,String password);

    @Query("SELECT user_id FROM hoply_users WHERE username = :username")
    public int getUserID(String username);

    @Insert(onConflict = OnConflictStrategy.FAIL)
    public void createNewUser(Users user);
}
