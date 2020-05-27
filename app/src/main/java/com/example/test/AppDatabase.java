package com.example.test;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;


import com.example.test.dao.CommentsDao;
import com.example.test.dao.PostDao;
import com.example.test.dao.PostUpvotesDao;
import com.example.test.dao.UsersDao;

import com.example.test.tables.Comments;
import com.example.test.tables.PostUpvotes;
import com.example.test.tables.Posts;
import com.example.test.tables.Users;

@Database(entities = {Users.class, Posts.class, Comments.class, PostUpvotes.class},version=2)
public abstract class AppDatabase extends RoomDatabase {


    public abstract UsersDao getAllUsers();

    public abstract CommentsDao getAllComments();
    public abstract PostUpvotesDao getAllPostUpvotes();


    public abstract PostDao getAllPosts();

}

