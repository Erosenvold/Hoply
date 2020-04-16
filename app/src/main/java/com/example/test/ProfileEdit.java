package com.example.test;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import com.example.test.dao.UsersDao;
import com.example.test.tables.Users;

public class ProfileEdit extends AppCompatActivity {


    public static AppDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profedit);

        this.database = MainActivity.getDB();




    }

    public void SaveChanges(View view){

        UsersDao userDao = database.getAllUsers();



        EditText ProfileTxt = findViewById(R.id.editText);
        String ProfileTxtStr = ProfileTxt.getText().toString();
        String UserId = LogSession.getSessionID();
        userDao.createNewProfileTxt(ProfileTxtStr, UserId);



    }

    public uploadPhoto() {
        ;
    }


    public void DoneButton(View view) {
        Intent intent = new Intent(this, ProfileActivity.class);

        startActivity(intent);
    }

}


