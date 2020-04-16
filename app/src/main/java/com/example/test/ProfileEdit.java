package com.example.test;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import com.example.test.dao.UsersDao;
import com.example.test.tables.Users;

import java.net.URI;

public class ProfileEdit extends AppCompatActivity {


    public static AppDatabase database;
    public static Uri imageUri;
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profedit);

        this.database = MainActivity.getDB();




    }
    public void UploadNewImageButton(View view){
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);

        intent.setType("image/*");
        startActivityForResult(intent,1);

    }

    public void SaveChanges(View view){

        UsersDao userDao = database.getAllUsers();



        EditText ProfileTxt = findViewById(R.id.editText);
        String ProfileTxtStr = ProfileTxt.getText().toString();
        String UserId = LogSession.getSessionID();
        userDao.createNewProfileTxt(ProfileTxtStr, UserId);

    }


    public void DoneButton(View view) {
        Intent intent = new Intent(this, ProfileActivity.class);

        startActivity(intent);
    }
    @Override
    protected void onActivityResult(int requestCode,int resultCode,Intent data){
        super.onActivityResult(requestCode,resultCode,data);
        if(requestCode==1 && resultCode == RESULT_OK){
            imageUri = data.getData();
            System.out.println(imageUri);
            ImageView editImage = findViewById(R.id.ProfilePic);
            editImage.setImageURI(imageUri);
        }
    }

}








