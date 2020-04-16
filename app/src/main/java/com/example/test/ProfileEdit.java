package com.example.test;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;
import com.example.test.dao.UsersDao;
import java.io.ByteArrayOutputStream;
import java.io.IOException;



public class ProfileEdit extends AppCompatActivity {


    public static AppDatabase database;
    public static Bitmap imageBitmap;
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profedit);

        this.database = MainActivity.getDB();


    }
    public void UploadNewImageButton(View view){
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);

        intent.setType("image/*");
        startActivityForResult(intent,1);

    }



    //saves new profileImage and ProfileText, starts profile acitivity
    public void DoneButton(View view) {
        UsersDao userDao = database.getAllUsers();


        //Save profile text in local Database
        EditText ProfileTxt = findViewById(R.id.editText);
        String ProfileTxtStr = ProfileTxt.getText().toString();
        String UserId = LogSession.getSessionID();
        userDao.createNewProfileTxt(ProfileTxtStr, UserId);

        //Save profile Image in local Database
        if(imageBitmap != null) {

            ByteArrayOutputStream baos=new  ByteArrayOutputStream();
            imageBitmap.compress(Bitmap.CompressFormat.PNG,100, baos);
            byte [] arr=baos.toByteArray();
            String result= Base64.encodeToString(arr, Base64.DEFAULT);


            userDao.createNewProfileImage(result, LogSession.getSessionID());
        }


        Intent intent = new Intent(this, ProfileActivity.class);

        startActivity(intent);
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
            ImageView editImage = findViewById(R.id.ProfilePic);
            editImage.setImageURI(imageUri);


        }
    }

}








