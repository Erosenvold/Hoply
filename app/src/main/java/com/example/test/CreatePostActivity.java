package com.example.test;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.provider.Settings;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.test.dao.PostDao;
import com.example.test.tables.Posts;

import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.List;
import java.util.Locale;

// FIX TIME TO TIMESTAMP


public class CreatePostActivity extends AppCompatActivity {
    public static AppDatabase database;
    public static Bitmap imageBitmap; //Image Bitmap
    private static Geocoder geo;
    private static Context context;
    private static Location location;
    private static String provider;


    public static String currLocation;

    protected void onCreate(Bundle savedInstanceState) {

        context = this;

        if (LogSession.isLoggedIn()) {

            geo = new Geocoder(this, Locale.getDefault());

            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_createpost);

            this.database = MainActivity.getDB();
        } else {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void createPostBtn(View view) {
        TextView errMsg = findViewById(R.id.createPostError);
        PostDao postDao = database.getAllPosts();

        EditText postTxt = findViewById(R.id.createPost);
        String strPostTxt = "";


        if(!postTxt.getText().toString().contains("@")){
            strPostTxt = postTxt.getText().toString();
        }else{

            errMsg.setText("Your post content contains one or more illegal words");
            errMsg.setTextColor(Color.RED);
        }


        if (!strPostTxt.trim().isEmpty()) {
            Posts post = new Posts();
            post.userID = LogSession.getSessionID();
            post.postContent = strPostTxt;
            post.timeCreated = System.currentTimeMillis();
            post.postRating = 0;

            post.location = currLocation;
            System.out.println(currLocation);

            String result = "";

            //Save profile Image in local Database
            if (imageBitmap != null) {

                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                imageBitmap = imageBitmap.createScaledBitmap(imageBitmap,500,500,false);



                imageBitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
                byte[] arr = baos.toByteArray();
                result = Base64.encodeToString(arr, Base64.DEFAULT);



//                postDao.createNewPostImage(result, postDao.getPostID(post.userID, post.timeCreated));
            }

            post.postContent = strPostTxt;
            post.postContent = post.postContent+"@GPS["+post.location+"]";
            post.postContent = post.postContent+"@IMG["+result +"]";


            postDao.createNewPost(post);



            //Needs to insert post
            Intent intent = new Intent(this, ProfileActivity.class);

            startActivity(intent);

        } else {
            errMsg.setText("Remember to write something in your post");
            errMsg.setTextColor(Color.RED);
        }



    }


    //UPLOAD IMAGE:

    public void UploadNewPostImageButton(View view) {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);

        intent.setType("image/*");
        startActivityForResult(intent, 1);

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK) {
            Uri imageUri = data.getData();
            try {
                this.imageBitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
            } catch (IOException e) {
                e.printStackTrace();
            }

            ImageView editImage = findViewById(R.id.PostImage);
            editImage.setImageURI(imageUri);


        }
    }

    // All of this is getting the current location, make sure to allow FINE location in manifest and set a location on the emulator

    public void getLocation(View view) {

        final CheckBox locationCheck = (CheckBox) view;



        //Checks if checkbox is checked

        if (locationCheck.isChecked()) {

            if (ActivityCompat.checkSelfPermission(CreatePostActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                List<Address> addresses;
                LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                Criteria criteria = new Criteria();
                criteria.setAccuracy(Criteria.ACCURACY_FINE);
                criteria.setCostAllowed(false);
                provider = locationManager.getBestProvider(criteria, false);

                location = locationManager.getLastKnownLocation(provider);

                try {
                    addresses = geo.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                    currLocation = addresses.get(0).getLocality();

                } catch (IOException e) {
                    e.printStackTrace();
                }

            } else {
                ActivityCompat.requestPermissions(CreatePostActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 44);
                locationCheck.setChecked(false);
            }
        }


    }
}