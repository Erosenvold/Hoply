package com.example.test;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;


import com.example.test.dao.CommentsDao;
import com.example.test.dao.PostCommentDao;
import com.example.test.dao.PostDao;
import com.example.test.dao.UserCommentDao;
import com.example.test.dao.UserPostDao;
import com.example.test.dao.UsersDao;

import com.example.test.tables.Comments;
import com.example.test.tables.PostComment;
import com.example.test.tables.Posts;
import com.example.test.tables.UserComment;
import com.example.test.tables.UserPost;
import com.example.test.tables.Users;

@Database(entities = {Users.class, Posts.class, Comments.class, PostComment.class, UserComment.class, UserPost.class},version=1)
public abstract class AppDatabase extends RoomDatabase {

    public abstract UsersDao getAllUsers();

    public abstract CommentsDao getAllComments();
    public abstract PostCommentDao getAllPostComment();
    public abstract UserCommentDao getAllUserComment();
    public abstract UserPostDao getAllUserPost();
    public abstract PostDao getAllPosts();



}
