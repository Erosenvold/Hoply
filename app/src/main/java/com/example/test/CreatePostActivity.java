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
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.provider.Settings;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.test.dao.PostDao;
import com.example.test.tables.Posts;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Locale;

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

    public void createPostBtn(View view) {
        TextView errMsg = findViewById(R.id.createPostError);
        PostDao postDao = database.getAllPosts();

        EditText postTxt = findViewById(R.id.createPost);
        String strPostTxt = postTxt.getText().toString();
        if (!strPostTxt.trim().isEmpty()) {
            Posts post = new Posts();
            post.userID = LogSession.getSessionID();
            post.postContent = strPostTxt;
            post.timeCreated = System.currentTimeMillis();
            post.postRating = 0;
                        /*Remember to use .split("/",2) on the returned string when showing location to get the latitude and longitude
                System.out.println(currLocation);
                c
                System.out.println("latitude: " + location[0]);
                System.out.println("longitude: " + location[1]);
                */

//            getLocation(findViewById(R.id.checkBox));
            post.location = currLocation;
            System.out.println(currLocation);
             postDao.createNewPost(post);


            //Save profile Image in local Database
            if (imageBitmap != null) {

                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                imageBitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
                byte[] arr = baos.toByteArray();
                String result = Base64.encodeToString(arr, Base64.DEFAULT);


                postDao.createNewPostImage(result, postDao.getPostID(post.userID, post.timeCreated));
            }

            //Needs to insert post


        } else {
            errMsg.setText("Remember to write something in your post");
            errMsg.setTextColor(Color.RED);
        }


        Intent intent = new Intent(this, ProfileActivity.class);

        startActivity(intent);
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
            System.out.println(imageUri);
            ImageView editImage = findViewById(R.id.PostImage);
            editImage.setImageURI(imageUri);


        }
    }

    // All of this is getting the current location, make sure to allow FINE location in manifest and set a location on the emulator

    public void getLocation(View view) {

        final CheckBox locationCheck = (CheckBox) view;


//        boolean permissionReceived = ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;

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