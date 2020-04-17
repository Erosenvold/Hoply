package com.example.test;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.test.dao.PostDao;
import com.example.test.dao.UsersDao;

public class FeedActivity extends AppCompatActivity {

    RecyclerView rv;

    //string arrays containing headlines and contents for posts
    String headlines[],  usernames[];

    //array of paths to images in the drawables folder.
    //to do: populate from database instead of drawable
    Bitmap images[];

    public static AppDatabase database;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if(LogSession.isLoggedIn()) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_feed);

            this.database = MainActivity.getDB();

            rv = findViewById(R.id.feedRecyclerView);

            //populates arrays from the values -> strings.xml
            //to do: populate from database instead of values
            PostDao postDao = database.getAllPosts();
            UsersDao usersDao = database.getAllUsers();
            headlines = new String[postDao.getAllIDDESC().length];
            usernames = new String[headlines.length];
            images = new Bitmap[headlines.length];
            for(int i = 0; i< postDao.getAllIDDESC().length;i++){

                int x = postDao.getAllContent(postDao.getAllIDDESC()[i]).length();
                if(x>300){
                    x = 300;
                    headlines[i] = postDao.getAllContent(postDao.getAllIDDESC()[i]).substring(0,x) + "...";
                }else{
                    headlines[i] = postDao.getAllContent(postDao.getAllIDDESC()[i]).substring(0,x);
                }
                usernames[i] = usersDao.getUsernameFromID(postDao.getUserID(postDao.getAllIDDESC()[i]));
                System.out.println(postDao.getAllIDDESC()[i]);
                System.out.println(headlines[0] + " hello");


                //Imageview: shows profile image if it exists
                if (postDao.getPostImages(postDao.getAllIDDESC()[i]) != null) {

                    String ImageStr = postDao.getPostImages(postDao.getAllIDDESC()[i]);
                    byte[]encodebyte = Base64.decode(ImageStr,Base64.DEFAULT);
                    Bitmap bitmapProfileImage = BitmapFactory.decodeByteArray(encodebyte, 0,encodebyte.length);
                    images[i] = bitmapProfileImage;

                } else {

                    images[i] = BitmapFactory.decodeResource(getResources(),R.drawable.defaultpic);
                }
            }


            //Instantiates the adapter that contains the feeds
            FeedAdapter feedAdapter = new FeedAdapter(this, headlines, usernames, images);
            rv.setAdapter((feedAdapter));
            rv.setLayoutManager(new LinearLayoutManager(this));


        } else{
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }
    }
    public void refreshBtn(View view){

    }
    public void sendToUserPost(View view){
        System.out.println("Hej");
    }

}
