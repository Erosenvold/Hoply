package com.example.test;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.test.dao.CommentsDao;
import com.example.test.dao.PostDao;
import com.example.test.dao.RemoteUserDAO;
import com.example.test.dao.UsersDao;
import com.example.test.tables.RemoteUsers;
import com.example.test.tables.Users;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
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

        AppDatabase database = Room.databaseBuilder(this, AppDatabase.class, "mydb")
                .allowMainThreadQueries()
                .fallbackToDestructiveMigration()
                .build();
        this.database = database;


        //Slet alt
        UsersDao usersDao = database.getAllUsers();
        PostDao postDao = database.getAllPosts();
        CommentsDao commentsDao = database.getAllComments();
        commentsDao.deleteAllComments();
        postDao.deleteAllPosts();
        usersDao.deleteAllUsers();
        //Indsæt users
        RemoteUserDAO remoteUsersDAO;
        remoteUsersDAO = RemoteClient.getRetrofitInstance().create(RemoteUserDAO.class);
        Call<List<RemoteUsers>> getUserFromId = remoteUsersDAO.getAllUsers();
        getUserFromId.enqueue(new Callback<List<RemoteUsers>>() {
            @Override
            public void onResponse(Call<List<RemoteUsers>> call, Response<List<RemoteUsers>> response) {
                System.out.println(response);
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

            }
        });

        /*
        READ

        for(Users u: usersDao.getAllUsers()){

            System.out.println("username: "+u.username);
            System.out.println("user id: "+u.id);
            System.out.println("stamp: " + u.timeCreated);

        }
/*
        WRITE
        Users user = new Users();
        user.id = ;
        user.username = ;
        user.timeCreated = ;
        usersDao.createNewUser(user);
*/



//      Read Users
//        RemoteUserDAO userDAO = RemoteClient.getRetrofitInstance().create(RemoteUserDAO.class);
//
//        Call<List<RemoteUsers>> call = userDAO.getUserFromId("eq.54321");
//
//        call.enqueue(new Callback<List<RemoteUsers>>() {
//            @Override
//            public void onResponse(Call<List<RemoteUsers>> call, Response<List<RemoteUsers>> response) {
//
//                if(response.isSuccessful()){
//                    System.out.println("Succes!");
//
//                    for(RemoteUsers u : response.body()){
//                        System.out.println(u.getName());
//                    }
//
//
//                }else{
//                    System.out.println(response.message());
//                    JSONObject jObjErr = null;
//                    try {
//                        jObjErr = new JSONObject(response.errorBody().string());
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//
//                    System.out.println(jObjErr);
//                }
//            }
//
//            @Override
//            public void onFailure(Call<List<RemoteUsers>> call, Throwable t) {
//
//                System.out.println("Failure! "+t.getMessage() );
//
//            }
//        });
//      Read Users

//        DANGER ZONE
//
//        UsersDao usersDao = database.getAllUsers();
//        PostDao postDao = database.getAllPosts();
//        CommentsDao commentsDao = database.getAllComments();
////
//        commentsDao.deleteAllComments();
//        postDao.deleteAllPosts();
//        usersDao.deleteAllUsers();

   //     DANGER ZONE
 //

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
