package com.example.test;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import com.example.test.dao.PostDao;
import com.example.test.tables.Posts;
/*
public class CreatePostActivity extends AppCompatActivity {
    public static AppDatabase database;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_createpost);

       AppDatabase database = Room.databaseBuilder(this, AppDatabase.class, "mydb")
                .allowMainThreadQueries()
                .build();*/
       // this.database = LoginActivity.getDB();
    //}

    //public void createPost(View view){
      /*  TextView errMsg = findViewById(R.id.createPostError);
        PostDao postDao = database.getAllPosts();
        EditText postTxt = findViewById(R.id.createPost);
        String strPostTxt = postTxt.getText().toString();
        if(!strPostTxt.trim().isEmpty()){
            Posts post = new Posts();
            post.postContent = strPostTxt;
            post.timeCreated = System.currentTimeMillis();
            postDao.createNewPost(post);


        }else{
            errMsg.setText("Remember to write something in your post");
            errMsg.setTextColor(Color.RED);
        }
    }
}*/