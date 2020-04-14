package com.example.test;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;


public class ProfileActivity extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        TextView UserText = (TextView) findViewById(R.id.UserName);

        UserText.setText("TODO");



    }

    public void ProfileEditButton(View view){

        Intent intent = new Intent(this,ProfileEdit.class);

        startActivity(intent);


    }







}


