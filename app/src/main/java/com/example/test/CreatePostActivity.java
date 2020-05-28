package com.example.test;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.content.pm.PackageManager;
import android.graphics.Color;
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

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.example.test.dao.PostDao;
import com.example.test.tables.Posts;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class CreatePostActivity extends AppCompatActivity {

    public static AppDatabase database;

    public static Bitmap imageBitmap; //Image Bitmap

    public static String currLocation;

    protected void onCreate(Bundle savedInstanceState) {
        if (LogSession.isLoggedIn()) {
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
                String[] location = currLocation.split("/",2);
                System.out.println("latitude: " + location[0]);
                System.out.println("longitude: " + location[1]);
                */
            post.location = currLocation;

            postDao.createNewPost(post);


            //Save profile Image in local Database
            if(imageBitmap != null) {

                ByteArrayOutputStream baos=new  ByteArrayOutputStream();
                imageBitmap.compress(Bitmap.CompressFormat.PNG,100, baos);
                byte [] arr=baos.toByteArray();
                String result= Base64.encodeToString(arr, Base64.DEFAULT);


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

    // All of this is getting the current location, make sure to allow FINE location in manifest and set a location on the emulator

    public void getLocation(View view) {
        CheckBox locationCheck = (CheckBox) view;
//Checks if checkbox is checked
        if (locationCheck.isChecked()) {

            LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

            Criteria criteria = new Criteria();
            criteria.setAccuracy(Criteria.ACCURACY_FINE);
            criteria.setCostAllowed(false);
            String provider = locationManager.getBestProvider(criteria,false);

            try {
                Location location = locationManager.getLastKnownLocation(provider);

                MyLocationListener myListener = new MyLocationListener();
                if(location != null){
                    //This is the important method, sets the location in variable currLocation if it isn't null
                    myListener.onLocationChanged(location);
                } else{
                    Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    startActivity(intent);
                }
                locationManager.requestLocationUpdates(provider,500,1,myListener);
            }catch(SecurityException e){
                System.out.println("SecurityException: " + e);
            }
            //     System.out.println(latitude + longitude);
        }else{
            currLocation = "";
        }
    }
    public class MyLocationListener implements LocationListener{
        @Override
        public void onLocationChanged(Location location){
            //Here you set the latitude and longitude and saves them together in currLocation
            String latitude = String.valueOf(location.getLatitude());
            String longitude = String.valueOf(location.getLongitude());
            currLocation  = latitude + "/" + longitude;
        }
        //Implementing an interface requires these methods to work
        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onProviderDisabled(String provider) {

        }

    }
}