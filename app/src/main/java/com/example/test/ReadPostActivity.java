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

import com.example.test.dao.CommentsDao;
import com.example.test.dao.RemoteCommentsDAO;
import com.example.test.dao.UsersDao;
import com.example.test.tables.Comments;
import com.example.test.tables.RemoteComments;

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

    RemoteCommentsDAO remoteCommentsDAO;
    CommentsDao commentsDao;
    UsersDao usersDao;
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
            usersDao = database.getAllUsers();



            commentsDao = database.getAllComments();
            commentsDao.deleteAllComments();

            //sets content TextView
            TextView content =  findViewById(R.id.postContent);
            content.setText(PostSession.getSessionContent());

            //sets Location TextView
            if(!PostSession.getSessionGPS().isEmpty()){
                TextView location =  findViewById(R.id.location);
                location.setText("Uploaded from " + PostSession.getSessionGPS());
            }

            //sets Postimage ImageView
            if(PostSession.getSessionIMG() != null){

                    ImageView postImage = findViewById(R.id.postImage);
                    postImage.setImageBitmap(PostSession.getSessionIMG());
            }

            //sets uploaded by user TextView
            TextView createdBy =  findViewById(R.id.createdBy);
            createdBy.setText("Uploaded by "+PostSession.getSessionName());

            //sets Timestamp TextView
            TextView timestamp =  findViewById(R.id.timestamp);
            Date currDate = new Date();
            SimpleDateFormat time = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
            String stamp = time.format(currDate);
            if(stamp.substring(0,10).equals(PostSession.getSessionStamp().substring(0,10))){
                timestamp.setText("Uploaded at "+ PostSession.getSessionStamp().substring(11,16));
            }else{
                timestamp.setText("Uploaded "+ PostSession.getSessionStamp().substring(8,10) +". "+ PostSession.getSessionStamp().substring(5,7) +". "+ PostSession.getSessionStamp().substring(0,4));
            }




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

                        for (RemoteComments remoteComment : response.body()) {
                            Comments localComments = new Comments();
                            localComments.commentContent = remoteComment.getContent();
                            localComments.timeCreated = remoteComment.getStamp();
                            localComments.postID = remoteComment.getPost_id();
                            localComments.userID = remoteComment.getUser_id();
                            commentsDao.createNewComment(localComments);
                            readCommentsLocal();

                            i.incrementAndGet();
                        }
                        setFeed();

                    }

                }

                @Override
                public void onFailure(Call<List<RemoteComments>> call, Throwable t) {
                    System.out.println("Failure : " +t.getMessage());
                }
            });







        }






        //if not logged in, go to Frontpage (mainacivity)
        else {
            Intent intent = new Intent(this, MainActivity.class);


            startActivity(intent);
        }
    }

    public void readCommentsLocal(){
        for(int i = 0; i< commentsDao.getCommentUserIDFromPostID(PostSession.sessionPostID).length;i++) {
            String result = "";

            boolean done = false;
            String name = usersDao.getUsernameFromID(commentsDao.getCommentUserIDFromPostID(PostSession.sessionPostID)[i]);
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
        }

        commentContents = commentsDao.getCommentsFromPostID(PostSession.sessionPostID);


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
