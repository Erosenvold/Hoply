package com.example.test;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import com.example.test.dao.CommentsDao;
import com.example.test.dao.PostDao;
import com.example.test.dao.RemoteUserDAO;
import com.example.test.dao.UsersDao;
import com.example.test.tables.RemoteUsers;
import com.example.test.tables.Users;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MainActivity extends AppCompatActivity {
    public static AppDatabase database;
    @Override
    protected void onCreate(Bundle savedInstanceState) {



        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Builds the local DB and saves in variable which is used in method getDB used everywhere else
        AppDatabase database = Room.databaseBuilder(this, AppDatabase.class, "mydb")
                .allowMainThreadQueries()
                .build();
        this.database = database;

        //Clears local DB of all info
        UsersDao usersDao = database.getAllUsers();
        PostDao postDao = database.getAllPosts();
        CommentsDao commentsDao = database.getAllComments();
        commentsDao.deleteAllComments();
        postDao.deleteAllPosts();
        usersDao.deleteAllUsers();

        //Call remote DB then inserts user info into local DB
        RemoteUserDAO remoteUsersDAO;
        remoteUsersDAO = RemoteClient.getRetrofitInstance().create(RemoteUserDAO.class);
        Call<List<RemoteUsers>> getUserFromId = remoteUsersDAO.getAllUsers();
        getUserFromId.enqueue(new Callback<List<RemoteUsers>>() {
            @Override
            public void onResponse(Call<List<RemoteUsers>> call, Response<List<RemoteUsers>> response) {
                //For every user in remote DB, insert into local DB
                for(RemoteUsers u: response.body()){
                    Users user = new Users();
                    user.id = u.getId();
                    user.username = u.getName();
                    user.timeCreated = u.getStamp();
                    usersDao.createNewUser(user);
                }
            }

            @Override
            public void onFailure(Call<List<RemoteUsers>> call, Throwable t) {
                //Error message       System.out.println("Failure : "+ t.getMessage());
            }
        });


    }
    //Returns local DB connection - used on every page where necessary for local DB connection
    public static AppDatabase getDB(){
        return database;
    }
    //OnClick event that sends to login page
    public void loginPage(View view){
        Intent intent = new Intent(this,LoginActivity.class);
        startActivity(intent);

    }
    //OnClick event that sends to create user page
    public void createUserPage(View view){
        Intent intent = new Intent(this,CreateUserActivity.class);
        startActivity(intent);
    }
}
