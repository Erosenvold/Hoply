package com.example.test.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import com.example.test.tables.Users;
import java.util.List;
//Data Access Object For Local DB Users
@Dao
public interface UsersDao {
    //Used for DB connection
    @Query("SELECT * FROM hoply_users")
    public List<Users> getAllUsers();

    //Returns user ID from given user ID
    @Query("SELECT id FROM hoply_users WHERE id = :userID")
    public String getUserID(String userID);

    //Returns timestamp from given user ID
    @Query("SELECT stamp FROM hoply_users WHERE id = :userID")
    public String getUserStamp(String userID);

    //Returns username from given user ID
    @Query("SELECT name FROM hoply_users WHERE id = :userID")
    public String getUsernameFromID(String userID);

    //Updates username with given user ID
    @Query("UPDATE hoply_users SET name = :name  WHERE id = :userID")
    public void updateUser(String name, String userID);

    //Inserts new user
    @Insert(onConflict = OnConflictStrategy.FAIL)
    public void createNewUser(Users user);

    //DELETES ALL USERS IN LOCAL DB!
    @Query("DELETE FROM hoply_users")
    public void deleteAllUsers();

}
