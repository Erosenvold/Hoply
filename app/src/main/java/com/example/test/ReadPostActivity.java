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

import com.example.test.dao.PostDao;
import com.example.test.dao.UsersDao;

public class ReadPostActivity extends AppCompatActivity {

    public static AppDatabase database;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        //if logged in.
        if (LogSession.isLoggedIn()) {

            //sets contentview to activity_readpost and gets database
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_readpost);
            this.database = MainActivity.getDB();
            UsersDao usersDao = database.getAllUsers();

            PostDao postDao = database.getAllPosts();

            //sets content TextView
            TextView content = (TextView) findViewById(R.id.postContent);
            content.setText(postDao.getAllContent(PostSession.getSessionID()));

            //sets uploaded by user TextView
            TextView createdBy = (TextView) findViewById(R.id.createdBy);
            createdBy.setText("Uploaded by "+usersDao.getUsernameFromID(postDao.getUserID(PostSession.getSessionID())));

            //sets Timestamp TextView
            TextView timestamp = (TextView) findViewById(R.id.timestamp);

            timestamp.setText("Uploaded "+ postDao.getTimestampFromID(PostSession.getSessionID()));


            //sets Location TextView if not null
            if(postDao.getLocationFromID(PostSession.getSessionID()) != null) {

                TextView location = (TextView) findViewById(R.id.location);
                location.setText("Uploaded from " + postDao.getLocationFromID(PostSession.getSessionID()));
            }

            //DET VAR HER VI NÅEDE TIL. :) tilføj resten af post indhold og knapper plus kommentare






        }


        //if not logged in, go to Frontpage (mainacivity)
        else {
            Intent intent = new Intent(this, MainActivity.class);


            startActivity(intent);
        }
    }

    public void sendToFeed(View view){
        Intent intent = new Intent(this,FeedActivity.class);
        startActivity(intent);
    }


}
