package com.example.test;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.test.dao.PostDao;

public class FeedActivity extends AppCompatActivity {

    RecyclerView rv;

    //string arrays containing headlines and contents for posts
    String headlines[],  usernames[];

    //array of paths to images in the drawables folder.
    //to do: populate from database instead of drawable
    int images[] = {R.drawable.tp,
                    R.drawable.rc,
                    R.drawable.re,
                    R.drawable.tc,
                    R.drawable.opkast,
                    R.drawable.l,
                    R.drawable.ff,
                    R.drawable.a16};

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
            for(int i = 0; i< postDao.getAllIDASC().length;i++){
                headlines = postDao.getAllContent(postDao.getAllIDASC()[i]);

            }

            usernames = getResources().getStringArray(R.array.post_username);

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

}
