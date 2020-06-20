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

    //On Start of page
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //sets local database connection
        this.database = MainActivity.getDB();
    }

    // Checks Local database for login information
    public void login(View view){

        //Saves input from username
        EditText userName = findViewById(R.id.usernameInput);
        String strUsername = userName.getText().toString();

        //Saves input from password
        EditText password = findViewById(R.id.passwordInput);
        String strPassword = password.getText().toString();

        //Initializes local user DAO
        UsersDao usersDao = database.getAllUsers();

        String userID = usersDao.getUserID(strUsername);

        //Checks if input matches a user
        if(userID != null && !userID.isEmpty() ){

                String s = usersDao.getUsernameFromID(strUsername);

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

                    LogSession.setSession(usersDao.getUserID(strUsername), logUsername, logProfileIMG, usersDao.getUserStamp(strUsername), logPassword);
                    Intent intent = new Intent(LoginActivity.this, ProfileActivity.class);
                    startActivity(intent);
                }
                //Shows error message
                else {
                    newUserMsg.setText("Incorrect Username or Password!");
                    newUserMsg.setTextColor(Color.RED);
                }
        }
        //Shows error message
        else{
            newUserMsg.setText("Incorrect Username or Password!");
            newUserMsg.setTextColor(Color.RED);
        }
    }
}
