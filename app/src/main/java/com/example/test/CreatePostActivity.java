package com.example.test;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.test.dao.PostDao;
import com.example.test.tables.Posts;

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

    public void createPostBtn(View view){
        TextView errMsg = findViewById(R.id.createPostError);
        PostDao postDao = database.getAllPosts();

        EditText postTxt = findViewById(R.id.createPost);
        String strPostTxt = postTxt.getText().toString();
        if(!strPostTxt.trim().isEmpty()){
            Posts post = new Posts();
            post.userID = LogSession.getSessionID();
            post.postContent = strPostTxt;
            post.timeCreated = System.currentTimeMillis();
            post.postRating = 0;
            //Needs to insert post


        }else{
            errMsg.setText("Remember to write something in your post");
            errMsg.setTextColor(Color.RED);
        }
    }
}