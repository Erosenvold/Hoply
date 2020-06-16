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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.test.dao.PostDao;
import com.example.test.dao.RemotePostDAO;
import com.example.test.dao.RemoteUserDAO;
import com.example.test.dao.UsersDao;
import com.example.test.tables.RemotePosts;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

// MOVE EVERYTHING UP

public class FeedActivity extends AppCompatActivity implements FeedAdapter.OnPostListener {

    RecyclerView rv;

    //string arrays containing headlines and contents for posts
    String content[],  usernames[];
    int postIds[];


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
            rv.setHasFixedSize(true);

            //populates arrays from the values -> strings.xml
            //to do: populate from database instead of values
            RemotePostDAO remotePostDAO = RemoteClient.getRetrofitInstance().create(RemotePostDAO.class);
            RemoteUserDAO remoteUserDAO = RemoteClient.getRetrofitInstance().create(RemoteUserDAO.class);

            Call<List<RemotePosts>> getAllPostsDESC = remotePostDAO.getAllPostDESC();

            getAllPostsDESC.enqueue(new Callback<List<RemotePosts>>() {
                @Override
                public void onResponse(Call<List<RemotePosts>> call, Response<List<RemotePosts>> response) {
                    content = new String[response.body().size()];
                    usernames = new String[content.length];
                    images = new Bitmap[content.length];
                    postIds = new int[content.length];

                    for(int i = response.body().size();i> response.body().size()-20;i--) {

                        String[] tempStringArr = .split("@", -2);

                        String[] stringArr = {"", "GPS[]", "IMG[]"};

                        for(String s : tempStringArr){
                            if (s.contains("GPS[")) {
                                stringArr[1]= s;
                            }
                            else if (s.contains("IMG[")) {
                                stringArr[2]=s;
                            }
                            else{
                                stringArr[0]=s;
                            }
                        }


                        int x = stringArr[0].length() - 1;
                        if (x > 100  ) {
                            x = 100;
                            content[i] = stringArr[0].substring(0, x) + "...";
                        } else {
                            content[i] = stringArr[0];
                        }


                }

                @Override
                public void onFailure(Call<List<RemotePosts>> call, Throwable t) {

                }
            });

            content = new String[postDao.getAllIDDESC().length];
            usernames = new String[content.length];
            images = new Bitmap[content.length];
            postIds = new int[content.length];
            for(int i = 0; i< postDao.getAllIDDESC().length;i++) {

                String[] tempStringArr = postDao.getAllContent(postDao.getAllIDDESC()[i]).split("@", -2);

                String[] stringArr = {"", "GPS[]", "IMG[]"};

                for(String s : tempStringArr){
                    if (s.contains("GPS[")) {
                        stringArr[1]= s;
                    }
                    else if (s.contains("IMG[")) {
                        stringArr[2]=s;
                    }
                    else{
                        stringArr[0]=s;
                    }
                }


                int x = stringArr[0].length() - 1;
                if (x > 100  ) {
                    x = 100;
                    content[i] = stringArr[0].substring(0, x) + "...";
                } else {
                    content[i] = stringArr[0];
                }
                usernames[i] = usersDao.getUsernameFromID(postDao.getUserID(postDao.getAllIDDESC()[i]));
                postIds[i] = postDao.getAllIDDESC()[i];


                //Imageview: shows profile image if it exists
                if (!stringArr[2].substring(4, stringArr[2].length() - 1).isEmpty()) {
                    String ImageStr = stringArr[2].substring(3, stringArr[2].length() - 1);
                    byte[] encodebyte = Base64.decode(ImageStr, Base64.DEFAULT);
                    Bitmap bitmapPostImage = BitmapFactory.decodeByteArray(encodebyte, 0, encodebyte.length);

                    images[i] = bitmapPostImage;

                } else {

                    images[i] = BitmapFactory.decodeResource(getResources(), R.drawable.defaultpic);
                }

            }

            //Instantiates the adapter that contains the feeds
            FeedAdapter feedAdapter = new FeedAdapter(this, content, usernames, images, postIds, this);
            rv.setAdapter((feedAdapter));
            rv.setLayoutManager(new LinearLayoutManager(this));


        } else{
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }
    }
    public void refreshBtn(View view){

        Intent intent = new Intent(this,FeedActivity.class);
        startActivity(intent);
    }

    public void myProfile(View view){
        Intent intent = new Intent(this,ProfileActivity.class);
        startActivity(intent);
    }


    @Override
    public void onPostClick(int position){
        PostSession.setSession(postIds[position]);
        Intent intent = new Intent(this,ReadPostActivity.class);;
        startActivity(intent);
    }

}
