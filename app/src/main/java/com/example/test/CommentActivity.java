/*package com.example.test;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.test.dao.CommentsDao;
import com.example.test.dao.UsersDao;
import com.example.test.tables.Comments;
import com.example.test.tables.Users;

public class CommentActivity extends AppCompatActivity {
    RecyclerView rv;

    String commentContent[],usernames[];

    public static AppDatabase database;
    @Override
    protected  void onCreate(Bundle savedInstanceState){
        if(LogSession.isLoggedIn()){
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_readpost);
            this.database = MainActivity.getDB();


            rv = findViewById(R.id.comments);
            UsersDao usersDao = database.getAllUsers();
            CommentsDao commentsDao = database.getAllComments();
            commentContent = new String[commentsDao.getCommentsFromPostID(PostSession.getSessionID()).length()];
            usernames = new String[commentContent.length];

            for(int i =0; i< commentContent.length;i++){
                usernames[i] = usersDao.getUsernameFromID(commentsDao.getCommentUserNameFromID(PostSession.getSessionID()));
                commentContent[i] = commentsDao.getCommentsFromPostID(PostSession.getSessionID());
            }
            CommentAdapter commentAdapter = new CommentAdapter(this,usernames,commentContent);
            rv.setAdapter((commentAdapter));
            rv.setLayoutManager(new LinearLayoutManager(this));
        } else{
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }
    }
}
*/