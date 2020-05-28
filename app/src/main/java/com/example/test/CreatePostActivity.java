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
        final CheckBox locationCheck = (CheckBox) view;
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        //Checks if checkbox is checked

        if (locationCheck.isChecked()) {

            if (ContextCompat.checkSelfPermission(

                    context, Manifest.permission.ACCESS_FINE_LOCATION) ==
                    PackageManager.PERMISSION_GRANTED) {
                Criteria criteria = new Criteria();
                criteria.setAccuracy(Criteria.ACCURACY_FINE);
                criteria.setCostAllowed(false);
                provider = locationManager.getBestProvider(criteria,false);
                location = locationManager.getLastKnownLocation(provider);
                // You can use the API that requires the permission.
//                performAction(...);
                System.out.println("Permission granted");

                try {


                    MyLocationListener myListener = new MyLocationListener();
                    if(location != null){
                        //This is the important method, sets the location in variable currLocation if it isn't null
                        myListener.onLocationChanged(location);
                        System.out.println("hello from if statement");

                    } else{
                        Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        startActivity(intent);
                    }
                    locationManager.requestLocationUpdates(provider,500,1,myListener);
                }catch(SecurityException e){
                    System.out.println("SecurityException: " + e);
                }

            }else {

                final AlertDialog.Builder alert = new AlertDialog.Builder(context);
                alert.setMessage("Would you like to give GPS permission to Hoply?").setCancelable(false).setPositiveButton("yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    }
                }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        locationCheck.setChecked(false);
                    }
                });
                final AlertDialog alertDialog = alert.create();
                alertDialog.show();
                System.out.println("Permission Denied");
            }

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



            System.out.println(location.toString());

            List<Address> addresses;


            //String[] location = postDao.getLocationFromID(PostSession.getSessionID()).split("/",2);

            try {
                addresses = geo.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                currLocation  = addresses.get(0).getLocality();
                System.out.println(currLocation);
            }catch (IOException e){
                e.printStackTrace();
            }



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