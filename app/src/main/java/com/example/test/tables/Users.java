package com.example.test.tables;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(tableName = "hoply_users",indices = {@Index(value={"user_id","username"},unique = true)})
public class Users {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "user_id")
    @NonNull public int userId;

    @ColumnInfo(name = "username")
    public String username;

    @ColumnInfo(name = "password")
    public String password;

    @ColumnInfo(name = "time_created")
    public long timeCreated;

    @Override
    public String toString(){
        return username;
    }
}
