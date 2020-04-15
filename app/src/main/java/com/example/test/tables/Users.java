package com.example.test.tables;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(tableName = "hoply_users",indices = {@Index(value={"id","name"},unique = true)})
public class Users {
    @PrimaryKey
    @ColumnInfo(name = "id")
    @NonNull public int id;

    @ColumnInfo(name = "name")
    public String username;

    @ColumnInfo(name = "password")
    public String password;

    @ColumnInfo(name = "time_created")
    public long timeCreated;

    @ColumnInfo(name = "profile_text")
    public String profileText;

    @ColumnInfo(name = "profile_image")
    public String profileImage;

    @Override
    public String toString(){
        return username;
    }
}
