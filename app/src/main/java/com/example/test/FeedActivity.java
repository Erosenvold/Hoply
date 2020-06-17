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
import com.example.test.tables.JoinPost;
import com.example.test.tables.RemotePosts;
import com.example.test.tables.RemoteUsers;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


// MOVE EVERYTHING UP

public class FeedActivity extends AppCompatActivity implements FeedAdapter.OnPostListener {

    RecyclerView rv;

    //string arrays containing headlines and contents for posts
    String content[],  usernames[], gps[], stamp[];
    int postIds[];
    String[] nameIds;
    AtomicInteger i = new AtomicInteger(0);

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



            int limit = 20;
            Call<List<RemotePosts>> getPostsDESC = remotePostDAO.getPostsDESC("stamp.desc",limit,0);


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

//                            Call<RemoteUsers> getUser = remoteUserDAO.getSingleUserFromId(u.getUser_id());
//                            getUser.enqueue(new Callback<RemoteUsers>() {
//
//                                @Override
//                                public void onResponse(Call<RemoteUsers> call, Response<RemoteUsers> response) {
//
//                                        usernames[i.get()]= response.body().getName();
//                                        System.out.println(response.body().getName());
//
//
//                                }
//
//                                @Override
//                                public void onFailure(Call<RemoteUsers> call, Throwable t) {
//                                    System.out.println("Failer in inner " + t.getMessage());
//                                }
//                            });




                            nameIds[i.get()]=u.getUser_id();
                            System.out.println("user ID : " + u.getUser_id());

                            String[] tempStringArr = u.getContent().split("@", -2);

                            String text = "";
                            String image = "IMG[]";
                            String location = "";

                            for (String s : tempStringArr) {
                                if (s.contains("GPS[")) {
                                    location = s;
                                } else if (s.contains("IMG[")) {
                                    image = s;
                                } else {
                                    text = s;
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
                            if (!image.substring(4, image.length() - 1).isEmpty()) {
                                String ImageStr = image.substring(3, image.length() - 1);
                                byte[] encodebyte = Base64.decode(ImageStr, Base64.DEFAULT);
                                Bitmap bitmapPostImage = BitmapFactory.decodeByteArray(encodebyte, 0, encodebyte.length);

                                images[i.get()] = bitmapPostImage;

                            } else {

                                images[i.get()] = BitmapFactory.decodeResource(getResources(), R.drawable.defaultpic);
                            }

                            postIds[i.get()] = u.getId();
                            gps[i.get()] = location;
                            stamp[i.get()] = u.getStamp();

                            if(i.get()<limit-1) {
                                System.out.println(i.get() +" "+i.incrementAndGet());

                            }



                        }

                        setUsernames();

                    }

                    @Override
                    public void onFailure(Call<List<RemotePosts>> call, Throwable t) {

                        System.out.println("Failure! :" + t.getMessage());

                    }
                });





//            content = new String[postDao.getAllIDDESC().length];
//            usernames = new String[content.length];
//            images = new Bitmap[content.length];
//            postIds = new int[content.length];
//            for(int i = 0; i< postDao.getAllIDDESC().length;i++) {
//
//                String[] tempStringArr = postDao.getAllContent(postDao.getAllIDDESC()[i]).split("@", -2);
//
//                String[] stringArr = {"", "GPS[]", "IMG[]"};
//
//                for(String s : tempStringArr){
//                    if (s.contains("GPS[")) {
//                        stringArr[1]= s;
//                    }
//                    else if (s.contains("IMG[")) {
//                        stringArr[2]=s;
//                    }
//                    else{
//                        stringArr[0]=s;
//                    }
//                }
//
//
//                int x = stringArr[0].length() - 1;
//                if (x > 100  ) {
//                    x = 100;
//                    content[i] = stringArr[0].substring(0, x) + "...";
//                } else {
//                    content[i] = stringArr[0];
//                }
//                usernames[i] = usersDao.getUsernameFromID(postDao.getUserID(postDao.getAllIDDESC()[i]));
//                postIds[i] = postDao.getAllIDDESC()[i];

//
//                //Imageview: shows profile image if it exists
//                if (!stringArr[2].substring(4, stringArr[2].length() - 1).isEmpty()) {
//                    String ImageStr = stringArr[2].substring(3, stringArr[2].length() - 1);
//                    byte[] encodebyte = Base64.decode(ImageStr, Base64.DEFAULT);
//                    Bitmap bitmapPostImage = BitmapFactory.decodeByteArray(encodebyte, 0, encodebyte.length);
//
//                    images[i] = bitmapPostImage;
//
//                } else {
//
//                    images[i] = BitmapFactory.decodeResource(getResources(), R.drawable.defaultpic);
//                }
//
//            }




        } else{
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }
    }

    public void setUsernames(){
        RemoteUserDAO remoteUserDAO = RemoteClient.getRetrofitInstance().create(RemoteUserDAO.class);
        System.out.println(nameIds[i.get()]+" Name iD"+ nameIds.length);
        AtomicInteger k = new AtomicInteger(0);
        for(int n = 0; n<nameIds.length; n++) {
            Call<List<RemoteUsers>> getUser = remoteUserDAO.getLimitedUsers("eq." + nameIds[n], 20);
            getUser.enqueue(new Callback<List<RemoteUsers>>() {

                @Override
                public void onResponse(Call<List<RemoteUsers>> call, Response<List<RemoteUsers>> response) {


//                    System.out.println(response);
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
                        usernames[k.get()] = result;

                        k.incrementAndGet();
                    }


                    instFeedAdapter();
                }

                @Override
                public void onFailure(Call<List<RemoteUsers>> call, Throwable t) {
                    System.out.println("Failer in inner " + t.getMessage());
                }
            });
        }
        for(String g : usernames){
            System.out.println(g);
        }
    }

    public void instFeedAdapter(){
        FeedAdapter feedAdapter = new FeedAdapter(FeedActivity.this, content, usernames, images, postIds, this);
        rv.setAdapter((feedAdapter));
        rv.setLayoutManager(new LinearLayoutManager(FeedActivity.this));
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

}
