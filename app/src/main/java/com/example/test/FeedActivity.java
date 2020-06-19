package com.example.test;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.test.dao.RemotePostDAO;
import com.example.test.dao.RemoteUserDAO;

import com.example.test.tables.RemotePosts;
import com.example.test.tables.RemoteUsers;

import java.util.List;

import java.util.concurrent.atomic.AtomicInteger;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


// MOVE EVERYTHING UP

public class FeedActivity extends AppCompatActivity implements FeedAdapter.OnPostListener {

    RecyclerView rv;

    //string arrays containing headlines and contents for posts
    String content[],  usernames[], gps[], stamp[],nameIds[];
    int postIds[];

    Bitmap images[];
    AtomicInteger i = new AtomicInteger(0);
    AtomicInteger completionCount = new AtomicInteger(0);


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        if(LogSession.isLoggedIn()) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_feed);



            rv = findViewById(R.id.feedRecyclerView);
            rv.setHasFixedSize(true);


            //populates arrays from the values -> strings.xml
            //to do: populate from database instead of values
            RemotePostDAO remotePostDAO = RemoteClient.getRetrofitInstance().create(RemotePostDAO.class);

            Call<List<RemotePosts>> getPostsDESC = remotePostDAO.getPostsDESC("stamp.desc",10,FeedSession.getSessionOffset());

            getPostsDESC.enqueue(new Callback<List<RemotePosts>>() {
                @Override
                public void onResponse(Call<List<RemotePosts>> call, Response<List<RemotePosts>> response) {
                    System.out.println("Succes: ");
                    content = new String[response.body().size()];
                    usernames = new String[content.length];
                    images = new Bitmap[content.length];
                    postIds = new int[content.length];
                    nameIds = new String[content.length];
                    gps = new String[content.length];
                    stamp = new String[content.length];



                    for (RemotePosts u : response.body()) {

                        nameIds[i.get()]=u.getUser_id();

                        String[] tempStringArr = u.getContent().split("@|]", -2);


                        String text = "";
                        String image = "";
                        String location = "";

                        for (String s : tempStringArr) {
                            if (s.contains("GPS[") && !s.equals("GPS[")) {
                                location = s.substring(4,s.length());
                            } else if (s.contains("IMG[" ) && !s.equals("IMG[")) {
                                image = s.substring(4,s.length());
                            } else {
                                text = text + s;
                            }
                        }

                        int x = text.length() - 1;
                        if (x > 100) {
                            x = 100;
                            content[i.get()] = text.substring(0, x) + "...";
                        } else {
                            content[i.get()] = text;
                        }

                        //Imageview: shows profile image if it exists
                        if (!image.isEmpty()) {
                            byte[] encodebyte = Base64.decode(image, Base64.DEFAULT);
                            Bitmap bitmapPostImage = BitmapFactory.decodeByteArray(encodebyte, 0, encodebyte.length);

                            images[i.get()] = bitmapPostImage;

                        } else {

                            images[i.get()] = BitmapFactory.decodeResource(getResources(), R.drawable.defaultpic);
                        }

                        postIds[i.get()] = u.getId();
                        gps[i.get()] = location;
                        stamp[i.get()] = u.getStamp();
                        setUsernames(i.get());

                        i.incrementAndGet();
                    }


                }

                @Override
                public void onFailure(Call<List<RemotePosts>> call, Throwable t) {
                    System.out.println("Failure! :" + t.getMessage());
                }
        });


        } else{
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }
    }

    public void setUsernames(int k) {
        RemoteUserDAO remoteUserDAO = RemoteClient.getRetrofitInstance().create(RemoteUserDAO.class);

        Call<List<RemoteUsers>> getUser = remoteUserDAO.getLimitedUsers("eq." + nameIds[k], 10);
        getUser.enqueue(new Callback<List<RemoteUsers>>() {

            @Override
            public void onResponse(Call<List<RemoteUsers>> call, Response<List<RemoteUsers>> response) {

                for (RemoteUsers s : response.body()) {
                    String result = "";
                    if (s != null) {
                        boolean done = false;
                        String name = s.getName();
                        int j = 0;
                        while (!done) {
                            if (name.length() > j && name.charAt(j) != '@') {
                                result = result + name.charAt(j);
                            } else {
                                done = true;
                            }
                            j++;
                        }
                    }
                    usernames[k] = result;

                    if(completionCount.incrementAndGet()>=content.length-1){
                            instFeedAdapter();
                    }


                }

            }

            @Override
            public void onFailure(Call<List<RemoteUsers>> call, Throwable t) {
                System.out.println("Failer in inner " + t.getMessage());
            }

        });

    }


    public void instFeedAdapter(){
            rv.setLayoutManager(new LinearLayoutManager(FeedActivity.this));
            FeedAdapter feedAdapter = new FeedAdapter(FeedActivity.this, content, usernames, images, postIds, this);
            rv.setAdapter((feedAdapter));

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
        PostSession.setSession(postIds[position], usernames[position], nameIds[position], content[position], stamp[position], gps[position], images[position]);
        Intent intent = new Intent(this,ReadPostActivity.class);;
        startActivity(intent);
    }

    public void increaseOffset(View view ){
        if(FeedSession.getSessionOffset()<=200) {
            FeedSession.incSessionOffset();
            Intent intent = new Intent(this, FeedActivity.class);
            startActivity(intent);
        }
    }

    public void decreaseOffset(View view ){

        if(FeedSession.getSessionOffset()>0) {
            FeedSession.decSessionOffset();
            Intent intent = new Intent(this,FeedActivity.class);
            startActivity(intent);
        }

    }

    //Starts CreatePost Activity
    public void createBtn(View view){
        Intent intent = new Intent(this,CreatePostActivity.class);
        startActivity(intent);
    }

}
