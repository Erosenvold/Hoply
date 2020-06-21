package com.example.test;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.test.dao.UsersDao;

//LogIn page
public class LoginActivity extends AppCompatActivity {
    public TextView newUserMsg;
    public static AppDatabase database;
    UsersDao usersDao;

    //On Start of page
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //sets local database connection
        this.database = MainActivity.getDB();
        //Initializes local user DAO
        usersDao = database.getAllUsers();
    }

    // Checks Local database for login information
    public void login(View view){

        //Initializes newUserMsg. This message is used to give feedback to the user.
        newUserMsg = findViewById(R.id.loginJoinMsg);

        //Saves input from username
        EditText userName = findViewById(R.id.usernameInput);
        String strUserID = userName.getText().toString();

        //Saves input from password
        EditText password = findViewById(R.id.passwordInput);
        String strPassword = password.getText().toString();

        String userID = usersDao.getUserID(strUserID);
        //Checks if input matches a user
        if(userID != null && !userID.isEmpty() ){

                String s = usersDao.getUsernameFromID(strUserID);

                //Splits username into Actual username, password and profile image
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

                //Checks if password matches and starts ProfileActivity if it does.
                // Saves user info in LoginSession
                if (strPassword.equals(logPassword)) {

                    LogSession.setSession(usersDao.getUserID(strUserID), logUsername, logProfileIMG, usersDao.getUserStamp(strUserID), logPassword);
                    Intent intent = new Intent(LoginActivity.this, ProfileActivity.class);
                    startActivity(intent);
                }
//                Shows error message
                else {
                    newUserMsg.setVisibility(View.VISIBLE);
                    newUserMsg.setText("Incorrect Username or Password!");
                    newUserMsg.setTextColor(Color.RED);
                }
        }
//        Shows error message
        else{
            newUserMsg.setVisibility(View.VISIBLE);
            newUserMsg.setText("Incorrect Username or Password!");
            newUserMsg.setTextColor(Color.RED);
        }
    }
}
