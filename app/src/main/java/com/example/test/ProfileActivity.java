package com.example.test;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.test.dao.UsersDao;

import java.net.URI;


public class ProfileActivity extends AppCompatActivity {


    public static AppDatabase database;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (LogSession.isLoggedIn()) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_profile);
            this.database = MainActivity.getDB();
            UsersDao usersDao = database.getAllUsers();

            //Textview: Profile ProfileText. Queries Database from SessionId to find Username.
            TextView UserText = (TextView) findViewById(R.id.UserName);
            UserText.setText(usersDao.getUsernameFromID(LogSession.getSessionID()));


            //Textview: Profile text. Queries Database from SessionId to find Profile Txt.
            TextView ProfileTxt = (TextView) findViewById(R.id.ProfileText);
            ProfileTxt.setText(usersDao.getProfileTxtFromID(LogSession.getSessionID()));



            //Imageview: shows profile image if it exists
            if (usersDao.getProfileImageFromID(LogSession.getSessionID()) != null) {

                String ImageStr = usersDao.getProfileImageFromID((LogSession.getSessionID()));
                byte[]encodebyte = Base64.decode(ImageStr,Base64.DEFAULT);
                Bitmap bitmapProfileImage = BitmapFactory.decodeByteArray(encodebyte, 0,encodebyte.length);

                ImageView profilePic = findViewById(R.id.ProfileImage);
                profilePic.setImageBitmap(bitmapProfileImage);
            } else {
                System.out.println("It's null dummy!");
            }

            
        } else {
            Intent intent = new Intent(this, MainActivity.class);


            startActivity(intent);
        }
    }












    public void ProfileEditButton(View view){

        Intent intent = new Intent(this,ProfileEdit.class);

        startActivity(intent);


    }
    public void createPostSendBtn(View view){
        Intent intent = new Intent(this,CreatePostActivity.class);
        startActivity(intent);
    }
    public void sendToFeed(View view){
        Intent intent = new Intent(this,FeedActivity.class);
        startActivity(intent);
    }







}


