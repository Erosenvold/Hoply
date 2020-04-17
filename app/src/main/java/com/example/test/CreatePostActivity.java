package com.example.test;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.test.dao.PostDao;
import com.example.test.tables.Posts;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class CreatePostActivity extends AppCompatActivity {
    public static AppDatabase database;
    public static Bitmap imageBitmap; //Image Bitmap

    protected void onCreate(Bundle savedInstanceState) {
        if(LogSession.isLoggedIn()) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_createpost);

            this.database = MainActivity.getDB();
        } else{
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }
    }

    public void createPostBtn(View view){
        TextView errMsg = findViewById(R.id.createPostError);
        PostDao postDao = database.getAllPosts();

        EditText postTxt = findViewById(R.id.createPost);
        String strPostTxt = postTxt.getText().toString();
        if(!strPostTxt.trim().isEmpty()){
            Posts post = new Posts();
            post.userID = LogSession.getSessionID();
            post.postContent = strPostTxt;
            post.timeCreated = System.currentTimeMillis();
            post.postRating = 0;
            postDao.createNewPost(post);


            //Save profile Image in local Database
            if(imageBitmap != null) {

                ByteArrayOutputStream baos=new  ByteArrayOutputStream();
                imageBitmap.compress(Bitmap.CompressFormat.PNG,100, baos);
                byte [] arr=baos.toByteArray();
                String result= Base64.encodeToString(arr, Base64.DEFAULT);


                postDao.createNewPostImage(result, postDao.getPostID(post.userID, post.timeCreated));
            }


        }else{
            errMsg.setText("Remember to write something in your post");
            errMsg.setTextColor(Color.RED);
        }



        Intent intent = new Intent(this, ProfileActivity.class);

        startActivity(intent);
    }



    //UPLOAD IMAGE:

    public void UploadNewPostImageButton(View view){
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);

        intent.setType("image/*");
        startActivityForResult(intent,1);

    }


    @Override
    protected void onActivityResult(int requestCode,int resultCode,Intent data){
        super.onActivityResult(requestCode,resultCode,data);
        if(requestCode==1 && resultCode == RESULT_OK){
            Uri imageUri = data.getData();
            try {
                this.imageBitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
            } catch (IOException e) {
                e.printStackTrace();
            }
            System.out.println(imageUri);
            ImageView editImage = findViewById(R.id.PostImage);
            editImage.setImageURI(imageUri);


        }
    }
}