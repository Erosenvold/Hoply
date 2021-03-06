package com.example.test;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

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

        if(strUsername.equals(usersDao.getUsernameLogin(strUsername, strPassword))){

          LogSession.setSession(usersDao.getUserID(strUsername));
          Intent intent = new Intent(this,ProfileActivity.class);
          startActivity(intent);



        }
        else{
            newUserMsg.setText("Incorrect Username or Password!");
            newUserMsg.setTextColor(Color.RED);
        }

    }
}
