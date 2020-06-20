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


public class FeedActivity extends AppCompatActivity implements FeedAdapter.OnPostListener {

    RecyclerView rv;
    PostDao postDao;

    //string arrays containing headlines and contents for posts
    String content[],  usernames[], gps[], stamp[],nameIds[];
    int postIds[];

    Bitmap images[];
    AtomicInteger i = new AtomicInteger(0);


    public static AppDatabase database;
    @Override
    //On start of activity
    protected void onCreate(Bundle savedInstanceState) {

        //Check if user is logged in
        if(LogSession.isLoggedIn()) {

            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_feed);


            this.database = MainActivity.getDB();
            rv = findViewById(R.id.feedRecyclerView);
            rv.setHasFixedSize(true);

            //Declares and initializes local databases for users.
            UsersDao usersDao = database.getAllUsers();

            //initializes local database for posts
            postDao = database.getAllPosts();

            //Deletes all posts in local database, as to not run into memory problems. This way the local database only holds 10 posts at a time.
            postDao.deleteAllPosts();

            //Declares remote post database
            RemotePostDAO remotePostDAO;

            /*
            * Initializes remote database. Query remote post database, sorted by timestamp and limited to 10 posts, with an offset set to from FeedSession.class.
            * This enables it to look at the 10 newest posts if offset is set to 0, the next 10 for offset 10 and so on.
            */
            remotePostDAO = RemoteClient.getRetrofitInstance().create(RemotePostDAO.class);
            Call<List<RemotePosts>> getPosts = remotePostDAO.getPostsDESC("stamp.desc",10,FeedSession.getSessionOffset());
            getPosts.enqueue(new Callback<List<RemotePosts>>() {
                @Override
                //If server response:
                public void onResponse(Call<List<RemotePosts>> call, Response<List<RemotePosts>> response) {

                    //Creates a new post in local for each post in getPosts (remote database).
                    for(RemotePosts u: response.body()){
                        Posts posts = new Posts();
                        posts.postID = u.getId();
                        posts.userID = u.getUser_id();
                        posts.timeCreated = u.getStamp();

                        //Makes sure that the the content is not to large for SQLite to handle.
                        if(u.getContent().length() < 1800000){
                            posts.postContent = u.getContent();
                        }else{
                            posts.postContent = "";
                        }
                        postDao.createNewPost(posts);
                    }

                    //Initialises Arrays to the length of the getALLIDDESC (10 unless the page has less than 10 elements);
                    content = new String[postDao.getAllIDDESC().length];
                    usernames = new String[content.length];
                    images = new Bitmap[content.length];
                    postIds = new int[content.length];
                    nameIds = new String[content.length];
                    gps = new String[content.length];
                    stamp = new String[content.length];


                    for(int i = 0; i< postDao.getAllIDDESC().length;i++){

                        //Separates actual username in local database from passwords and profile image, sets username array to actual username.
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

                        //Sets postIds array (index i) to postID's from local database.
                        postIds[i] = postDao.getAllIDDESC()[i];

                        //Sets content array (index i) to postID's from local database.
                        content[i] = postDao.getContentFromID(postIds[i]);

                        //Separate Text, images and location in content.
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

                        //Sets content to be only the text.
                        content[i] = text;

                        //sets gps array (index i) to location from separation of content above.
                        gps[i] = location;

                        //Creates a bitmap from string of image (from separation of content above), if no image is present create a default bitmap.
                        if (!image.isEmpty()) {
                            byte[] encodebyte = Base64.decode(image, Base64.DEFAULT);
                            Bitmap bitmapPostImage = BitmapFactory.decodeByteArray(encodebyte, 0, encodebyte.length);
                            images[i] = bitmapPostImage;
                        } else {
                            images[i] = BitmapFactory.decodeResource(getResources(), R.drawable.defaultpic);
                        }

                        // sets postIds to postID's from local database
                        postIds[i] = postDao.getAllIDDESC()[i];

                        stamp[i] =postDao.getAllStampsDESC()[i];
                    }

                    //Call instFeedAdapter to initialize FeedAdapter.
                    instFeedAdapter();
                }

                //if server does not response
                @Override
                public void onFailure(Call<List<RemotePosts>> call, Throwable t) {

                }
            });

        //if not user is not Logged in send to MainActivity.
        } else{
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }
    }



    //Initiate FeedAdapter
    public void instFeedAdapter(){
            rv.setLayoutManager(new LinearLayoutManager(FeedActivity.this));
            FeedAdapter feedAdapter = new FeedAdapter(FeedActivity.this, content, usernames, images, postIds, this);
            rv.setAdapter((feedAdapter));

    }

    //Sets Offset to 0 and
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
