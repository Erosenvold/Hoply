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
    @NonNull public String id;

    @ColumnInfo(name = "name")
    public String username;



    @ColumnInfo(name = "stamp")
    public String timeCreated;


    @Override
    public String toString(){
        return username;
    }
}
