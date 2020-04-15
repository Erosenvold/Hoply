package com.example.test;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import com.example.test.dao.UsersDao;

public class ProfileEdit extends AppCompatActivity {




    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profedit);

    }


    public void DoneButton(View view) {
        Intent intent = new Intent(this, ProfileActivity.class);

        startActivity(intent);
    }

}


