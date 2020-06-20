package com.example.test.tables;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

//Local table of users
@Entity(tableName = "hoply_users",indices = {@Index(value={"id","name"},unique = true)})
public class Users {
    //Returns ID - Sets as primary key
    @PrimaryKey
    @ColumnInfo(name = "id")
    @NonNull public String id;

    //Returns username
    @ColumnInfo(name = "name")
    public String username;

    //Returns time stamp
    @ColumnInfo(name = "stamp")
    public String timeCreated;

    @Override
    public String toString(){
        return username;
    }
}
