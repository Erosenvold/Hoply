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

/**
 * this class controls the ReadPostActivity
 */
public class ReadPostActivity extends AppCompatActivity {

    /**
     * Class Attributes
     */
    RecyclerView rv;
    RemoteCommentsDAO remoteCommentsDAO;
    CommentsDao commentsDao;
    UsersDao usersDao;
    String commentContents[],usernames[], userIds[];
    public static AppDatabase database;

    /**
     * This method is called when the user is sent to ReadPostActivity
     * @param savedInstanceState can contain a saved state of the UI.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        //if logged in.
        if (LogSession.isLoggedIn()) {
            //sets contentview to activity_readpost and gets database
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_readpost);
            this.database = MainActivity.getDB();
            //initializes local database.
            usersDao = database.getAllUsers();
            commentsDao = database.getAllComments();
            commentsDao.deleteAllComments();

            //finds the TextView that will contain the postContent,
            //and sets it with information from the session log.
            TextView content =  findViewById(R.id.postContent);
            content.setText(PostSession.getSessionContent());

            //If there is a location logged:
            //finds the TextView that will contain the location,
            //and sets it with information from the session log.
            if(!PostSession.getSessionGPS().isEmpty()){
                TextView location =  findViewById(R.id.location);
                location.setText("Uploaded from " + PostSession.getSessionGPS());
            }

            //If there is an image logged:
            //finds the ImageView that will contain the image,
            //and sets it with information from the session log.
            if(PostSession.getSessionIMG() != null){
                    ImageView postImage = findViewById(R.id.postImage);
                    postImage.setImageBitmap(PostSession.getSessionIMG());
            }

            //finds the TextView that will contain the username of the poster,
            //and sets it with information from the session log.
            TextView createdBy =  findViewById(R.id.createdBy);
            createdBy.setText("Uploaded by "+PostSession.getSessionName());

            //finds the TextView that will contain the timestamp,
            //and sets it with information from the session log.
            TextView timestamp =  findViewById(R.id.timestamp);
            Date currDate = new Date();
            SimpleDateFormat time = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
            String stamp = time.format(currDate);
            if(stamp.substring(0,10).equals(PostSession.getSessionStamp().substring(0,10))){
                timestamp.setText("Uploaded at "+ PostSession.getSessionStamp().substring(11,16));
            }else{
                timestamp.setText("Uploaded "+ PostSession.getSessionStamp().substring(8,10) +". "+ PostSession.getSessionStamp().substring(5,7) +". "+ PostSession.getSessionStamp().substring(0,4));
            }

            //Finds the RecyclerView
            rv = findViewById(R.id.comments);

            //Creates a call to the Remote database looking through the Comments with foreign key matching the PostSession's logged Id.
            remoteCommentsDAO = RemoteClient.getRetrofitInstance().create(RemoteCommentsDAO.class);
            Call<List<RemoteComments>> getCommentsFromPostId = remoteCommentsDAO.getCommentsFromPostId("eq."+PostSession.getSessionPostID());
            getCommentsFromPostId.enqueue(new Callback<List<RemoteComments>>() {
                //When a response is received, this method is called
                @Override
                public void onResponse(Call<List<RemoteComments>> call, Response<List<RemoteComments>> response) {
                    //If the content of the response isn't null. Retrieve the information from response.
                    //Then call setFeed() which instantiates the RecyclerView which is going to display all this information.
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
                //If the response fails print some information relating to why
                @Override
                public void onFailure(Call<List<RemoteComments>> call, Throwable t) {
                    System.out.println("Failure : " +t.getMessage());
                }
            });
        }

        //if not logged in, go to Frontpage (MainAcivity)
        else {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }
    }

    /**
     * This method reads the info from comments in the local database and adds
     * the commenter's name to usernames.
     * Then it sets the content corresponding to the userID and postID
     */
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


    /**
     * Instantiates the RecyclerView containing the feed elements.
     */
    public void setFeed(){
        rv.setLayoutManager(new LinearLayoutManager(this));
        CommentAdapter commentAdapter = new CommentAdapter(this,usernames,commentContents);
        rv.setAdapter((commentAdapter));
    }

    /**
     * This method is called when the Create Comment button is clicked.
     * @param view the Create Comment button.
     */
    public void createComment(View view){

        //finds the EditText object which will take the comment
        //then what is written in that EditText.
        EditText comment = findViewById(R.id.commentField);
        String strComment = comment.getText().toString();

        //if strComment isn't empty or whitespace only.
        if(!strComment.trim().isEmpty()){
            //create timestamp
            Date currDate = new Date();
            SimpleDateFormat time = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
            String stamp = time.format(currDate);

            //Create an insertion in the Remote Database
            Call<RemoteComments> insertComment = remoteCommentsDAO.insertComment(LogSession.getSessionID(), Integer.parseInt(PostSession.getSessionPostID()),
                    strComment, stamp, "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJyb2xlIjoiYXBwMjAyMCJ9.PZG35xIvP9vuxirBshLunzYADEpn68wPgDUqzGDd7ok");
            insertComment.enqueue(new Callback<RemoteComments>() {
                //if a response is received - call refreshPage().
                @Override
                public void onResponse(Call<RemoteComments> call, Response<RemoteComments> response) {
                    refreshPage();
                }
                //if it fails also refreshPage();
                @Override
                public void onFailure(Call<RemoteComments> call, Throwable t) {
                    System.out.println("failure : "+ t.getMessage());
                    refreshPage();
                }
            });
        }
    }

    /**
     * This method is called when the Home button is clicked.
     * Sends the user to the FeedActivity.
     * @param view the Home button
     */
    public void sendToFeed(View view){
        Intent intent = new Intent(this,FeedActivity.class);
        startActivity(intent);
    }

    /**
     * Sends the user to a new instance of ReadPostActivity
     */
    public void refreshPage(){
        Intent intent = new Intent(this,ReadPostActivity.class);
        startActivity(intent);
    }
}
