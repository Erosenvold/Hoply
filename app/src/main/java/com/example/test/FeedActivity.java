package com.example.test;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.test.dao.PostDao;
import com.example.test.dao.RemotePostDAO;
import com.example.test.dao.UsersDao;
import com.example.test.tables.Posts;
import com.example.test.tables.RemotePosts;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


// MOVE EVERYTHING UP
//Asger
public class FeedActivity extends AppCompatActivity implements FeedAdapter.OnPostListener {

    RecyclerView rv;

    //string arrays containing headlines and contents for posts
    String content[],  usernames[], gps[], stamp[],nameIds[];
    int postIds[];

    Bitmap images[];
    AtomicInteger i = new AtomicInteger(0);

    public static AppDatabase database;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        if(LogSession.isLoggedIn()) {

            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_feed);


            this.database = MainActivity.getDB();
            rv = findViewById(R.id.feedRecyclerView);
            rv.setHasFixedSize(true);


            PostDao postDao = database.getAllPosts();
            UsersDao usersDao = database.getAllUsers();
            postDao.deleteAllPosts();


            RemotePostDAO remotePostDAO;

            remotePostDAO = RemoteClient.getRetrofitInstance().create(RemotePostDAO.class);
            System.out.println("OFFSET: " + FeedSession.getSessionOffset());
            Call<List<RemotePosts>> getPosts = remotePostDAO.getPostsDESC("stamp.desc",10,FeedSession.getSessionOffset());
            getPosts.enqueue(new Callback<List<RemotePosts>>() {
                @Override
                public void onResponse(Call<List<RemotePosts>> call, Response<List<RemotePosts>> response) {

                    PostDao postDao = database.getAllPosts();

                    for(RemotePosts u: response.body()){
                        Posts posts = new Posts();
                        posts.postID = u.getId();
                        posts.userID = u.getUser_id();
                        posts.timeCreated = u.getStamp();
                        if(u.getContent().length() < 1800000){
                            posts.postContent = u.getContent();
                        }else{
                            posts.postContent = "";
                        }

                        postDao.createNewPost(posts);
                    }
                    content = new String[postDao.getAllIDDESC().length];
                    System.out.println("CONTENT LENGTH: "+content.length);
                    usernames = new String[content.length];
                    images = new Bitmap[content.length];
                    postIds = new int[content.length];
                    nameIds = new String[content.length];
                    gps = new String[content.length];
                    stamp = new String[content.length];

                        for(int i = 0; i< postDao.getAllIDDESC().length;i++){



                            //CUT UP USERNAMES
                            String result = "";

                                boolean done = false;
                                String name = usersDao.getUsernameFromID(postDao.getUserID(postDao.getAllIDDESC()[i]));
                                if( name == null){
                                    name = "";
                                }
                                int j = 0;
                                while (!done) {
                                    if (name.length() > j && name.charAt(j) != '@') {
                                        result = result + name.charAt(j);
                                    } else {
                                        done = true;
                                    }
                                    j++;
                                }
                                usernames[i] = result;


                            postIds[i] = postDao.getAllIDDESC()[i];


                            content[i] = postDao.getContentFromID(postIds[i]);

                            String[] tempStringArr = content[i].split("@|]", -2);


                            String text = "";
                            String image = "";
                            String location = "";

                            for (String s : tempStringArr) {
                                if (s.contains("GPS[") && !s.equals("GPS[")) {
                                    location = s.substring(4);
                                } else if (s.contains("IMG[" ) && !s.equals("IMG[")) {
                                    image = s.substring(4);
                                } else {
                                    text = text + s;
                                }
                            }

                            content[i] = text;
                            System.out.println("IMAGE LENGTH: "+image.length());

                            //Imageview: shows profile image if it exists
                            if (!image.isEmpty()) {
                                byte[] encodebyte = Base64.decode(image, Base64.DEFAULT);
                                Bitmap bitmapPostImage = BitmapFactory.decodeByteArray(encodebyte, 0, encodebyte.length);

                                images[i] = bitmapPostImage;

                            } else {

                                images[i] = BitmapFactory.decodeResource(getResources(), R.drawable.defaultpic);
                            }

                            postIds[i] = postDao.getAllIDDESC()[i];
                            gps[i] = location;
                            stamp[i] =postDao.getAllStampsDESC()[i];


                        }
                        instFeedAdapter();

                }
                @Override
                public void onFailure(Call<List<RemotePosts>> call, Throwable t) {

                }
            });


        } else{
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }
    }




    public void instFeedAdapter(){
            rv.setLayoutManager(new LinearLayoutManager(FeedActivity.this));
            FeedAdapter feedAdapter = new FeedAdapter(FeedActivity.this, content, usernames, images, postIds, this);
            rv.setAdapter((feedAdapter));

    }

    public void refreshBtn(View view){
        FeedSession.resetSessionOffset();

        FeedActivity.this.finish();
        Intent intent = new Intent(FeedActivity.this, FeedActivity.class);
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

            FeedSession.incSessionOffset();
            FeedActivity.this.finish();
            Intent intent = new Intent(FeedActivity.this, FeedActivity.class);
            startActivity(intent);

    }

    public void decreaseOffset(View view ){

        if(FeedSession.getSessionOffset()>0) {
            FeedSession.decSessionOffset();
            FeedActivity.this.finish();
            Intent intent = new Intent(FeedActivity.this, FeedActivity.class);
            startActivity(intent);

        }

    }

    //Starts CreatePost Activity
    public void createBtn(View view){
        Intent intent = new Intent(this,CreatePostActivity.class);
        startActivity(intent);
    }
}
