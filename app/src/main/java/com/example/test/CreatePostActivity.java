package com.example.test;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import com.example.test.AppDatabase;
import com.example.test.LogSession;

import com.example.test.MainActivity;
import com.example.test.R;
import com.example.test.dao.PostDao;
import com.example.test.dao.UserPostDao;
import com.example.test.dao.UsersDao;
import com.example.test.tables.Posts;
import com.example.test.tables.UserPost;

public class CreatePostActivity extends AppCompatActivity {
    public static AppDatabase database;

    protected void onCreate(Bundle savedInstanceState) {
        if(LogSession.isLoggedIn()) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_createpost);

            this.database = MainActivity.getDB();
        } else{
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }
    }

    public void createPost(View view){
        TextView errMsg = findViewById(R.id.createPostError);
        PostDao postDao = database.getAllPosts();
        UserPostDao userPostDao = database.getAllUserPost();
        EditText postTxt = findViewById(R.id.createPost);
        String strPostTxt = postTxt.getText().toString();
        if(!strPostTxt.trim().isEmpty()){
            Posts post = new Posts();
            post.postContent = strPostTxt;
            post.timeCreated = System.currentTimeMillis();

            UserPost userPost = new UserPost();
            userPost.postId = (int)postDao.createNewPost(post);
            userPost.userId = LogSession.getSessionID();



        }else{
            errMsg.setText("Remember to write something in your post");
            errMsg.setTextColor(Color.RED);
        }
    }
}