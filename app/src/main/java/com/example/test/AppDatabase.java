package com.example.test;

import androidx.room.Database;
import androidx.room.RoomDatabase;


import com.example.test.dao.CommentsDao;
import com.example.test.dao.PostDao;
import com.example.test.dao.UsersDao;

import com.example.test.tables.Comments;
import com.example.test.tables.Posts;
import com.example.test.tables.Users;

@Database(entities = {Users.class, Posts.class, Comments.class},version=1)
public abstract class AppDatabase extends RoomDatabase {


    public abstract UsersDao getAllUsers();

    public abstract CommentsDao getAllComments();


    public abstract PostDao getAllPosts();

}

