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
            headlines = new String[postDao.getAllIDDESC().length];
            for(int i = 0; i< postDao.getAllIDDESC().length;i++){

                int x = postDao.getAllContent(postDao.getAllIDDESC()[i]).length();
                if(x>300){
                    x = 300;
                    headlines[i] = postDao.getAllContent(postDao.getAllIDDESC()[i]).substring(0,x) + "...";
                }else{
                    headlines[i] = postDao.getAllContent(postDao.getAllIDDESC()[i]).substring(0,x);
                }

                System.out.println(postDao.getAllIDDESC()[i]);
                System.out.println(headlines[0] + " hello");
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
