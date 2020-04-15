package com.example.test;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.test.dao.UsersDao;


public class ProfileActivity extends AppCompatActivity {


    public static AppDatabase database;
    @Override
    protected void onCreate(Bundle savedInstanceState){
        if(LogSession.isLoggedIn()) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_profile);
            this.database = MainActivity.getDB();
            UsersDao usersDao = database.getAllUsers();
            TextView UserText = (TextView) findViewById(R.id.UserName);

            UserText.setText(usersDao.getUsernameFromID(LogSession.getSessionID()));
        }else{
            Intent intent = new Intent(this,MainActivity.class);

            startActivity(intent);

        }


    }

    public void ProfileEditButton(View view){

        Intent intent = new Intent(this,ProfileEdit.class);

        startActivity(intent);


    }
    public void createPostSendBtn(View view){
        Intent intent = new Intent(this,CreatePostActivity.class);
        startActivity(intent);
    }







}


