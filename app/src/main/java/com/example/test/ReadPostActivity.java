package com.example.test;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.test.dao.CommentsDao;
import com.example.test.dao.PostDao;
import com.example.test.dao.RemoteCommentsDAO;
import com.example.test.dao.RemoteUserDAO;
import com.example.test.dao.UsersDao;
import com.example.test.tables.Comments;
import com.example.test.tables.RemoteComments;
import com.example.test.tables.RemoteUsers;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

//TIME TO TIMESTAMP + VISUALS

public class ReadPostActivity extends AppCompatActivity {
    RecyclerView rv;

    String commentContents[],usernames[], userIds[];


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
            content.setText(PostSession.getSessionContent());

            //sets Location TextView
            if(!PostSession.getSessionGPS().isEmpty()){
                TextView location = (TextView) findViewById(R.id.location);
                location.setText("Uploaded from " + PostSession.getSessionGPS());
            }

            //sets Postimage ImageView
            if(PostSession.getSessionIMG() != null){

                    ImageView postImage = findViewById(R.id.postImage);
                    postImage.setImageBitmap(PostSession.getSessionIMG());
            }

            //sets uploaded by user TextView
            TextView createdBy = (TextView) findViewById(R.id.createdBy);
            createdBy.setText("Uploaded by "+PostSession.getSessionName());

            //sets Timestamp TextView
            TextView timestamp = (TextView) findViewById(R.id.timestamp);
            timestamp.setText("Uploaded "+ PostSession.getSessionStamp());

//            //sets Post image
//            if (postDao.getPostImages(PostSession.getSessionID()) != null) {
//
//                String ImageStr = postDao.getPostImages(PostSession.getSessionID());
//
//                byte[]encodebyte = Base64.decode(ImageStr,Base64.DEFAULT);
//                Bitmap bitmapPostImage = BitmapFactory.decodeByteArray(encodebyte, 0,encodebyte.length);
//
//                ImageView postImage = findViewById(R.id.postImage);
//                postImage.setImageBitmap(bitmapPostImage);
//            }
//
//
//            //sets Location TextView if not null
//            if(postDao.getLocationFromID(PostSession.getSessionID()) != null) {
//
//                TextView location = (TextView) findViewById(R.id.location);
//                location.setText("Uploaded from " + postDao.getLocationFromID(PostSession.getSessionID()));
//
//            }

            //DET VAR HER VI NÅEDE TIL. :) tilføj resten af post indhold og knapper plus kommentare



            rv = findViewById(R.id.comments);
            System.out.println("Post ID = "+PostSession.getSessionPostID());
            RemoteCommentsDAO commentsDao = RemoteClient.getRetrofitInstance().create(RemoteCommentsDAO.class);
            System.out.println(PostSession.getSessionPostID());

            Call<List<RemoteComments>> getCommentsFromPostId = commentsDao.getCommentsFromPostId("eq."+PostSession.getSessionPostID());

            getCommentsFromPostId.enqueue(new Callback<List<RemoteComments>>() {
                @Override
                public void onResponse(Call<List<RemoteComments>> call, Response<List<RemoteComments>> response) {
                    System.out.println(response);

                    if(response.body() != null) {
                        AtomicInteger i = new AtomicInteger(0);
                        commentContents = new String[response.body().size()];
                        usernames = new String[commentContents.length];
                        userIds = new String[commentContents.length];

                        for (RemoteComments comment : response.body()) {
                            commentContents[i.get()] = comment.getContent();
                            userIds[i.get()] = comment.getUser_id();

                            System.out.println("This is the result: " + usernames[i.get()]);
                            setUsernames(i.get());
                            i.incrementAndGet();
                        }

                    }
                    setFeed();
                }

                @Override
                public void onFailure(Call<List<RemoteComments>> call, Throwable t) {
                    System.out.println("Failure : " +t.getMessage());
                }
            });


//            commentContents = commentsDao.getCommentsFromPostID(PostSession.getSessionPostID());
//            usernames = commentsDao.getCommentUserIDFromPostID(PostSession.getSessionPostID());
//            for(int i=0; usernames.length>i; i++){
//                usernames[i] = usersDao.getUsernameFromID(usernames[i]);
//            }
////            System.out.println(commentContents[0] + " : " + usernames[0]);


        }






        //if not logged in, go to Frontpage (mainacivity)
        else {
            Intent intent = new Intent(this, MainActivity.class);


            startActivity(intent);
        }
    }

    public void setUsernames(int k){
        RemoteUserDAO remoteUserDAO = RemoteClient.getRetrofitInstance().create(RemoteUserDAO.class);

        Call<List<RemoteUsers>> getUser = remoteUserDAO.getLimitedUsers("eq." + userIds[k], 20);
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
                        usernames[k] = result;
                    }

                    System.out.println("username on k : " + usernames[k]);

                }
            }

            @Override
            public void onFailure(Call<List<RemoteUsers>> call, Throwable t) {
                System.out.println("Failer in inner " + t.getMessage());
            }
        });
    }

    public void setFeed(){
        CommentAdapter commentAdapter = new CommentAdapter(this,usernames,commentContents);
        rv.setAdapter((commentAdapter));
        rv.setLayoutManager(new LinearLayoutManager(this));
    }


    public void createComment(View view){

        EditText comment = findViewById(R.id.commentField);
        String strComment = comment.getText().toString();
        if(!strComment.trim().isEmpty()){
            CommentsDao commentsDao = database.getAllComments();
            Comments newComment = new Comments();
            newComment.userID = LogSession.getSessionID();
//            newComment.postID = PostSession.getSessionID();

            Date currDate = new Date();
            SimpleDateFormat time = new SimpleDateFormat("yyyy-MM-dd'T'HH.mm.ss.SSSXXX");
//            newComment.timeCreated = time.format(currDate);

            newComment.commentContent = strComment;
            commentsDao.createNewComment(newComment);
            Intent intent = new Intent(this, ReadPostActivity.class);
            startActivity(intent);
        }
    }
    public void sendToFeed(View view){
        Intent intent = new Intent(this,FeedActivity.class);
        startActivity(intent);
    }


}
