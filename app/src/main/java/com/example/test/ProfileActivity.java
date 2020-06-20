package com.example.test;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;



public class ProfileActivity extends AppCompatActivity {


    public static AppDatabase database;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //if logged in.
        if (LogSession.isLoggedIn()) {

            //sets contentview to activity_profile and gets database
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_profile);
            this.database = MainActivity.getDB();






            TextView UserText =  findViewById(R.id.UserName);
            UserText.setText(LogSession.getSessionUsername());

            TextView Timestamp = findViewById(R.id.Timestamp);
            Timestamp.setText("Member since: " + LogSession.getSessionStamp().substring(0,10));

            //Imageview: shows profile image if it exists
            if (LogSession.getSessionIMG().length() != 0) {

                String ImageStr = LogSession.getSessionIMG();
                byte[]encodebyte = Base64.decode(ImageStr,Base64.DEFAULT);
                Bitmap bitmapProfileImage = BitmapFactory.decodeByteArray(encodebyte, 0,encodebyte.length);

                ImageView profilePic = findViewById(R.id.ProfileImage);
                profilePic.setImageBitmap(bitmapProfileImage);
            }else{
                Bitmap bitmapProfileImage = BitmapFactory.decodeResource(getResources(), R.drawable.defaultpic);
                ImageView profilePic = findViewById(R.id.ProfileImage);
                profilePic.setImageBitmap(bitmapProfileImage);

            }


        }
        //if not logged in, go to Frontpage (mainacivity)
        else {
            Intent intent = new Intent(this, MainActivity.class);


            startActivity(intent);
        }
    }


    //Starts ProfileEdit Activity
    public void ProfileEditButton(View view){

        Intent intent = new Intent(this,ProfileEdit.class);

        startActivity(intent);


    }

    //Starts CreatePost Activity
    public void createPostSendBtn(View view){

        FeedSession.resetSessionOffset();
        Intent intent = new Intent(ProfileActivity.this,CreatePostActivity.class);
        startActivity(intent);
    }

    //Starts Feed Activity
    public void sendToFeed(View view){

        FeedSession.resetSessionOffset();
        Intent intent = new Intent(ProfileActivity.this,FeedActivity.class);
        startActivity(intent);
    }






}


