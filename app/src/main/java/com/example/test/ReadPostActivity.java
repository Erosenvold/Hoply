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
import com.example.test.dao.UsersDao;
import com.example.test.tables.Comments;

public class ReadPostActivity extends AppCompatActivity {
    RecyclerView rv;

    String commentContents[],usernames[];
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
            content.setText(postDao.getAllContent(PostSession.getSessionID()));

            //sets uploaded by user TextView
            TextView createdBy = (TextView) findViewById(R.id.createdBy);
            createdBy.setText("Uploaded by "+usersDao.getUsernameFromID(postDao.getUserID(PostSession.getSessionID())));

            //sets Timestamp TextView
            TextView timestamp = (TextView) findViewById(R.id.timestamp);

            timestamp.setText("Uploaded "+ postDao.getTimestampFromID(PostSession.getSessionID()));


            //sets Location TextView if not null
            if(postDao.getLocationFromID(PostSession.getSessionID()) != null) {

                TextView location = (TextView) findViewById(R.id.location);
                location.setText("Uploaded from " + postDao.getLocationFromID(PostSession.getSessionID()));

            }

            //DET VAR HER VI NÅEDE TIL. :) tilføj resten af post indhold og knapper plus kommentare



            rv = findViewById(R.id.comments);
            System.out.println("Post ID = "+PostSession.getSessionID());
            CommentsDao commentsDao = database.getAllComments();
            commentContents = commentsDao.getCommentsFromPostID(PostSession.getSessionID());
            usernames = commentsDao.getCommentUserNameFromID(PostSession.getSessionID());


            CommentAdapter commentAdapter = new CommentAdapter(this,usernames,commentContents);
            rv.setAdapter((commentAdapter));
            rv.setLayoutManager(new LinearLayoutManager(this));
        }






        //if not logged in, go to Frontpage (mainacivity)
        else {
            Intent intent = new Intent(this, MainActivity.class);


            startActivity(intent);
        }
    }

    public void createComment(View view){

        EditText comment = findViewById(R.id.commentField);
        String strComment = comment.getText().toString();
        if(!strComment.trim().isEmpty()){
            CommentsDao commentsDao = database.getAllComments();
            Comments newComment = new Comments();
            newComment.userID = LogSession.getSessionID();
            newComment.postID = PostSession.getSessionID();
            newComment.timeCreated = System.currentTimeMillis();
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
