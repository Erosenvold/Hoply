package com.example.test;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.test.dao.UsersDao;




public class MainActivity extends AppCompatActivity {
    public static AppDatabase database;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        AppDatabase database = Room.databaseBuilder(this, AppDatabase.class, "mydb")
                .allowMainThreadQueries()
          //      .fallbackToDestructiveMigration()
                .build();
        this.database = database;
        //UsersDao usersDao = database.getAllUsers();


    }
    public static AppDatabase getDB(){
        return database;
    }
    public void loginPage(View view){

        Intent intent = new Intent(this,LoginActivity.class);
        System.out.println(LogSession.isLoggedIn());
        startActivity(intent);

    }
    public void createUserPage(View view){
        Intent intent = new Intent(this,CreateUserActivity.class);
        startActivity(intent);
    }
}
