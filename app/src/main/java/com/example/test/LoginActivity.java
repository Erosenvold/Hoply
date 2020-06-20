package com.example.test;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.test.dao.UsersDao;


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
        String userID = usersDao.getUserID(strUsername);

        if(userID != null && !userID.isEmpty() ){


                String s = usersDao.getUsernameFromID(strUsername);


                String[] tempString = s.split("@|]", -2);

                String logUsername = "";
                String logPassword = "";
                String logProfileIMG = "";


                for (String str : tempString) {
                    if (str.contains("PWD[")) {
                        logPassword = str.substring(4);


                    } else if (str.contains("IMG[")) {
                        logProfileIMG = str.substring(4);
                    } else {
                        logUsername = logUsername+ str;

                    }

                }

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

        
    }
}
