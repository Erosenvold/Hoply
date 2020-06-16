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
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.example.test.dao.PostDao;
import com.example.test.tables.Posts;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;

import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.atomic.AtomicReference;

// FIX TIME TO TIMESTAMP

public class CreatePostActivity extends AppCompatActivity {
    public static AppDatabase database;
    public static Bitmap imageBitmap; //Image Bitmap
    private static Geocoder geo;
    private static Context context;
    private static FusedLocationProviderClient flpClient;

    public static String currLocation;

    protected void onCreate(Bundle savedInstanceState) {


        currLocation = "";
        context = this;
        flpClient = LocationServices.getFusedLocationProviderClient(this);

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
        String strPostTxt = postTxt.getText().toString();


        //Checks if the text contains String regex used to wrap content.
        if(postTxt.getText().toString().contains("@") || postTxt.getText().toString().contains("GPS[") || postTxt.getText().toString().contains("IMG[")) {
            errMsg.setText("Your post content contains one or more illegal words");
            errMsg.setTextColor(Color.RED);


        //checks if textfield was left null or filled with whitespace. If not, adds Timestamp, userID and content to a new Post.
        }else if (!strPostTxt.trim().isEmpty()) {
            Posts post = new Posts();
            post.userID = LogSession.getSessionID();
            Date currDate = new Date();

            SimpleDateFormat time = new SimpleDateFormat("yyyy-MM-dd'T'HH.mm.ss.SSSXXX");
            System.out.println(time.format(currDate));
            //FIX THIS
            post.timeCreated = System.currentTimeMillis();
            post.postContent = strPostTxt;


            //If user has chosen an image, add image to content.
            if (imageBitmap != null) {

                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                imageBitmap = imageBitmap.createScaledBitmap(imageBitmap,500,500,false);

                imageBitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
                byte[] arr = baos.toByteArray();
                String result = Base64.encodeToString(arr, Base64.DEFAULT);


                post.postContent = post.postContent+"@IMG["+result +"]";


            }

            //Add location to content
            if(currLocation != null){
                post.postContent = post.postContent+"@GPS["+currLocation+"]";
            }



            // Creates posts
            postDao.createNewPost(post);


            //Needs to insert post
            Intent intent = new Intent(this, ProfileActivity.class);

            startActivity(intent);

        }else {
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
                AtomicReference<List<Address>> addresses = new AtomicReference<>();

                //LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                Criteria criteria = new Criteria();
                criteria.setAccuracy(Criteria.ACCURACY_FINE);
                criteria.setCostAllowed(false);
                //provider = locationManager.getBestProvider(criteria, false);
                //location = locationManager.getLastKnownLocation(provider);

                flpClient.getLastLocation().addOnSuccessListener(CreatePostActivity.this, l -> {
                    if(l != null){
                        try {
                            addresses.set(geo.getFromLocation(l.getLatitude(), l.getLongitude(), 1));

                            currLocation = addresses.get().get(addresses.get().size()-1).getLocality();

                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });



            } else {
                ActivityCompat.requestPermissions(CreatePostActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 44);
                locationCheck.setChecked(false);

            }
        }


    }
}