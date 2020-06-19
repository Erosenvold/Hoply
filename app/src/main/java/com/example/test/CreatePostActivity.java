package com.example.test;

import android.Manifest;
import android.app.Activity;
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
import com.example.test.dao.RemoteCommentsDAO;
import com.example.test.dao.RemotePostDAO;
import com.example.test.tables.Posts;
import com.example.test.tables.RemotePosts;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;

import org.json.JSONException;
import org.json.JSONObject;

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
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

// FIX TIME TO TIMESTAMP

public class CreatePostActivity extends AppCompatActivity {
    public static AppDatabase database;
    private static Bitmap imageBitmap; //Image Bitmap
    private static Geocoder geo;
    private static Context context;
    private static String stamp, content;
    private static FusedLocationProviderClient flpClient;
    private static AtomicBoolean found = new AtomicBoolean(false);

    private static AtomicInteger newUniqueId = new AtomicInteger(0);
    public static String currLocation;

    protected void onCreate(Bundle savedInstanceState) {

        currLocation = "";
        imageBitmap = null;

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


    public void createPostBtn(View view) {




        TextView errMsg = findViewById(R.id.createPostError);
        RemotePostDAO remotePostDao = RemoteClient.getRetrofitInstance().create(RemotePostDAO.class);


        EditText postTxt = findViewById(R.id.createPost);
        String strPostTxt = postTxt.getText().toString();


        //Checks if the text contains String regex used to wrap content.
        if(postTxt.getText().toString().contains("@") || postTxt.getText().toString().contains("GPS[") || postTxt.getText().toString().contains("IMG[")) {
            errMsg.setText("Your post content contains one or more illegal words");
            errMsg.setTextColor(Color.RED);


        //checks if textfield was left null or filled with whitespace. If not, adds Timestamp, userID and content to a new Post.
        }else if (!strPostTxt.trim().isEmpty()) {

            Date currDate = new Date();
            SimpleDateFormat time = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
            stamp = time.format(currDate);

            content = strPostTxt;


            //If user has chosen an image, add image to content.
            if (imageBitmap != null) {

                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                imageBitmap = imageBitmap.createScaledBitmap(imageBitmap,500,500,false);

                imageBitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
                byte[] arr = baos.toByteArray();
                String result = Base64.encodeToString(arr, Base64.DEFAULT);


                content = content+"@IMG["+result +"]";


            }

            //Add location to content
            if(currLocation != null){
                content = content+"@GPS["+currLocation+"]";
            }

            setUniqueId(1);


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


    public void setUniqueId(int i){
        int key = 1051;

        RemotePostDAO remotePostDAO = RemoteClient.getRetrofitInstance().create(RemotePostDAO.class);

        String stirng = "eq."+i*key%Integer.MAX_VALUE;

        Call<List<RemotePosts>> checkIds = remotePostDAO.getPostFromId(stirng);

        checkIds.enqueue(new Callback<List<RemotePosts>>() {
            @Override
            public void onResponse(Call<List<RemotePosts>> call, Response<List<RemotePosts>> response) {
                System.out.println(response);
                if(response.body().size() == 0){
                    newUniqueId.set(i*key%Integer.MAX_VALUE);
                    System.out.println("hej du har lavet en post med id "+ newUniqueId.get());

                    insertPost();

                }else{
                    setUniqueId(i+1);
                }
            }

            @Override
            public void onFailure(Call<List<RemotePosts>> call, Throwable t) {

                System.out.println(t.getMessage());

            }

        });

    }


    public void insertPost(){
        RemotePostDAO remotePostDAO = RemoteClient.getRetrofitInstance().create(RemotePostDAO.class);
        Call<RemotePosts> insertPost = remotePostDAO.setPost(newUniqueId.get(), LogSession.getSessionID(), content, stamp,
                "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJyb2xlIjoiYXBwMjAyMCJ9.PZG35xIvP9vuxirBshLunzYADEpn68wPgDUqzGDd7ok");

        insertPost.enqueue(new Callback<RemotePosts>() {
            @Override
            public void onResponse(Call<RemotePosts> call, Response<RemotePosts> response) {
                if(response.isSuccessful()){
                    System.out.println("you Made a Post! wow such post");
                }else{
                    JSONObject jObjErr = null;
                    try {
                        jObjErr = new JSONObject(response.errorBody().string());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    System.out.println(jObjErr);
                    System.out.println("unsuccesful : " + response);
                }
            }

            @Override
            public void onFailure(Call<RemotePosts> call, Throwable t) {
                System.out.println("Failure : " + t.getMessage());
            }
        });
    }

    // All of this is getting the current location, make sure to allow FINE location in manifest and set a location on the emulator

    public void getLocation(View view) {

        final CheckBox locationCheck = (CheckBox) view;

        //Checks if checkbox is checked

        if (locationCheck.isChecked()) {
            if (ActivityCompat.checkSelfPermission(CreatePostActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                AtomicReference<List<Address>> addresses = new AtomicReference<>();
                Criteria criteria = new Criteria();
                criteria.setAccuracy(Criteria.ACCURACY_FINE);
                criteria.setCostAllowed(false);
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