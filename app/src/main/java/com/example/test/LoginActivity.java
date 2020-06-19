package com.example.test;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import com.example.test.dao.RemoteUserDAO;
import com.example.test.dao.UsersDao;
import com.example.test.tables.RemoteUsers;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {
    public TextView newUserMsg;
    public static AppDatabase database;
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


       this.database = MainActivity.getDB();


        Intent newUserIntent = getIntent();
        String newUserMessage = newUserIntent.getStringExtra("NEWUSERMSG");
        TextView newUserMsg = findViewById(R.id.loginJoinMsg);
        this.newUserMsg = newUserMsg;
        newUserMsg.setVisibility(View.VISIBLE);
        newUserMsg.setText(newUserMessage);
    }

    public void login(View view){
        EditText userName = findViewById(R.id.usernameInput);
        String strUsername = userName.getText().toString();
        EditText password = findViewById(R.id.passwordInput);
        String strPassword = password.getText().toString();
        UsersDao usersDao = database.getAllUsers();
        System.out.println("USERID: "+usersDao.getUserID(strUsername));

        if(!usersDao.getUserID(strUsername).isEmpty()){


                String s = usersDao.getUsernameFromID(strUsername);


                String[] tempString = s.split("@|]", -2);

                String logUsername = "";
                String logPassword = "";
                String logProfileIMG = "";


                for (String str : tempString) {
                    if (str.contains("PWD[")) {
                        logPassword = str.substring(4, str.length());

//                                System.out.println(password);
                    } else if (str.contains("IMG[")) {
                        logProfileIMG = str.substring(4, str.length());
                    } else {
                        logUsername = logUsername+ str;
//                                System.out.println(username);
                    }

                }
                System.out.println("PASSWORD FROM INPUT: " + strPassword);
                System.out.println("PASSWORD FROM NAME: " + logPassword);
                if (strPassword.equals(logPassword)) {

                    LogSession.setSession(usersDao.getUserID(strUsername), logUsername, logProfileIMG, usersDao.getUserStamp(strUsername), logPassword);
                    Intent intent = new Intent(LoginActivity.this, ProfileActivity.class);
                    startActivity(intent);
                } else {
                    newUserMsg.setText("Incorrect Username or Password!");
                    newUserMsg.setTextColor(Color.RED);
                }



        }else{
            newUserMsg.setText("Incorrect Username or Password!");
            newUserMsg.setTextColor(Color.RED);
        }




/*
        RemoteUserDAO remoteUserDAO = RemoteClient.getRetrofitInstance().create(RemoteUserDAO.class);

        Call<List<RemoteUsers>> userFromId = remoteUserDAO.getUserFromId("eq."+strUsername);

        userFromId.enqueue(new Callback<List<RemoteUsers>>() {
            @Override
            public void onResponse(Call<List<RemoteUsers>> call, Response<List<RemoteUsers>> response) {



            }

            @Override
            public void onFailure(Call<List<RemoteUsers>> call, Throwable t) {

            }
        });*/
        
    }
}
