package com.example.test;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

/**
 * This method handles the functionality of being in your own profile.
 */
public class ProfileActivity extends AppCompatActivity {

    /**
     * access local database
     */
    public static AppDatabase database;

    /**
     * This method is called when the activity is launched.
     * @param savedInstanceState can contain a saved state of the UI.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //if logged in.
        if (LogSession.isLoggedIn()) {

            //sets contentview to activity_profile and gets database
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_profile);
            this.database = MainActivity.getDB();

            //finds the TextView that will contain the username,
            //and sets it with information from the session log.
            TextView UserText =  findViewById(R.id.UserName);
            UserText.setText(LogSession.getSessionUsername());

            //finds the TextView that will contain the time of creation,
            //and set it with information from the session log.
            TextView Timestamp = findViewById(R.id.Timestamp);
            Timestamp.setText("Member since: " + LogSession.getSessionStamp().substring(0,10));

            //Checks whether there should be a profile picture.
            //if there is encode it and show it in the ImageView.
            if (LogSession.getSessionIMG().length() != 0) {
                String ImageStr = LogSession.getSessionIMG();
                byte[]encodebyte = Base64.decode(ImageStr,Base64.DEFAULT);
                Bitmap bitmapProfileImage = BitmapFactory.decodeByteArray(encodebyte, 0,encodebyte.length);
                ImageView profilePic = findViewById(R.id.ProfileImage);
                profilePic.setImageBitmap(bitmapProfileImage);
            //if there isn't a profile picture; show the standard profile picture.
            }else{
                Bitmap bitmapProfileImage = BitmapFactory.decodeResource(getResources(), R.drawable.defaultpic);
                ImageView profilePic = findViewById(R.id.ProfileImage);
                profilePic.setImageBitmap(bitmapProfileImage);
            }
        }
        //if not logged in, go to MainActivity
        else {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }
    }

    /**
     * This method sends the user to ProfileEdit when the Edit Profile button is clicked.
     * @param view the Edit Profile button.
     */
    public void ProfileEditButton(View view){
        Intent intent = new Intent(this,ProfileEdit.class);
        startActivity(intent);
    }

    /**
     * this method sends the user to CreatePostActivity when the Create Post button is clicked.
     * @param view the Create Post button.
     */
    public void createPostSendBtn(View view){
        Intent intent = new Intent(ProfileActivity.this,CreatePostActivity.class);
        startActivity(intent);
    }

    /**
     * this method sends the user to FeedActivity when the Home button is clicked.
     * @param view the Home button.
     */
    public void sendToFeed(View view){
        FeedSession.resetSessionOffset();
        Intent intent = new Intent(ProfileActivity.this,FeedActivity.class);
        startActivity(intent);
    }
}


