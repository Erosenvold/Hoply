package com.example.test;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.test.dao.RemoteCommentsDAO;
import com.example.test.dao.RemoteUserDAO;
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
    RemoteUserDAO remoteUserDAO;
    RemoteCommentsDAO remoteCommentsDAO;

    String commentContents[],usernames[], userIds[];

    AtomicInteger completionCount = new AtomicInteger(0);



    @Override
    protected void onCreate(Bundle savedInstanceState) {

        //if logged in.
        if (LogSession.isLoggedIn()) {

            //sets contentview to activity_readpost and gets database
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_readpost);



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

            remoteCommentsDAO = RemoteClient.getRetrofitInstance().create(RemoteCommentsDAO.class);


            Call<List<RemoteComments>> getCommentsFromPostId = remoteCommentsDAO.getCommentsFromPostId("eq."+PostSession.getSessionPostID());

            getCommentsFromPostId.enqueue(new Callback<List<RemoteComments>>() {
                @Override
                public void onResponse(Call<List<RemoteComments>> call, Response<List<RemoteComments>> response) {


                    if(response.body() != null) {
                        AtomicInteger i = new AtomicInteger(0);
                        commentContents = new String[response.body().size()];
                        usernames = new String[commentContents.length];
                        userIds = new String[commentContents.length];

                        for (RemoteComments comment : response.body()) {
                            commentContents[i.get()] = comment.getContent();
                            userIds[i.get()] = comment.getUser_id();


                            setUsernames(i.get());
                            i.incrementAndGet();
                        }

                    }

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
        remoteUserDAO = RemoteClient.getRetrofitInstance().create(RemoteUserDAO.class);

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

                        if(completionCount.incrementAndGet()>=commentContents.length-1){
                            setFeed();
                        }
                    }


                }
            }

            @Override
            public void onFailure(Call<List<RemoteUsers>> call, Throwable t) {
                System.out.println("Failure in inner " + t.getMessage());
            }
        });
    }

    public void setFeed(){
        rv.setLayoutManager(new LinearLayoutManager(this));
        CommentAdapter commentAdapter = new CommentAdapter(this,usernames,commentContents);
        rv.setAdapter((commentAdapter));

    }


    public void createComment(View view){

        EditText comment = findViewById(R.id.commentField);
        String strComment = comment.getText().toString();
        if(!strComment.trim().isEmpty()){
            Date currDate = new Date();
            SimpleDateFormat time = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
            String stamp = time.format(currDate);


            Call<RemoteComments> insertComment = remoteCommentsDAO.insertComment(LogSession.getSessionID(), Integer.parseInt(PostSession.getSessionPostID()),
                    strComment, stamp, "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJyb2xlIjoiYXBwMjAyMCJ9.PZG35xIvP9vuxirBshLunzYADEpn68wPgDUqzGDd7ok");

            insertComment.enqueue(new Callback<RemoteComments>() {
                @Override
                public void onResponse(Call<RemoteComments> call, Response<RemoteComments> response) {
                    refreshPage();
                }

                @Override
                public void onFailure(Call<RemoteComments> call, Throwable t) {
                    System.out.println("failure : "+ t.getMessage());
                    refreshPage();
                }
            });


        }
    }
    public void sendToFeed(View view){
        Intent intent = new Intent(this,FeedActivity.class);
        startActivity(intent);
    }

    public void refreshPage(){
        Intent intent = new Intent(this,ReadPostActivity.class);
        startActivity(intent);
    }

}
