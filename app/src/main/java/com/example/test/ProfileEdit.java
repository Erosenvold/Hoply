package com.example.test;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.View;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;

import com.example.test.dao.RemoteUserDAO;
import com.example.test.tables.RemoteUsers;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

//TEST IMAGES

public class ProfileEdit extends AppCompatActivity {



    public static Bitmap imageBitmap;
    static String userUpdate;
    static String result;
    @Override
    protected void onCreate(Bundle savedInstanceState){

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profedit);
        imageBitmap = null;



    }
    public void UploadNewImageButton(View view){
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);

        intent.setType("image/*");
        startActivityForResult(intent,1);

    }



    //saves new profileImage and ProfileText, starts profile acitivity
    public void DoneButton(View view) {



        //Save profile text in local Database
//        EditText ProfileTxt = findViewById(R.id.editText);
//        String ProfileTxtStr = ProfileTxt.getText().toString();
        String UserId = LogSession.getSessionID();
//        userDao.createNewProfileTxt(ProfileTxtStr, UserId);

        //Save profile Image in local Database
        if(imageBitmap != null) {

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            imageBitmap = imageBitmap.createScaledBitmap(imageBitmap,500,500,false);



            imageBitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
            byte[] arr = baos.toByteArray();
            result = Base64.encodeToString(arr, Base64.DEFAULT);

            userUpdate = LogSession.getSessionUsername()+ "@PWD["+LogSession.getSessionPassword()+"]"+ "@IMG["+result+"]";

        }

        RemoteUserDAO remoteUserDAO = RemoteClient.getRetrofitInstance().create(RemoteUserDAO.class);
        Call<RemoteUsers> updateUser = remoteUserDAO.updateUser("eq."+LogSession.getSessionID(), userUpdate,
                "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJyb2xlIjoiYXBwMjAyMCJ9.PZG35xIvP9vuxirBshLunzYADEpn68wPgDUqzGDd7ok");

        updateUser.enqueue(new Callback<RemoteUsers>() {
            @Override
            public void onResponse(Call<RemoteUsers> call, Response<RemoteUsers> response) {

                LogSession.setSessionIMG(result);
                goToProfile();

            }

            @Override
            public void onFailure(Call<RemoteUsers> call, Throwable t) {
                System.out.println("Failure : "+ t.getMessage());
            }
        });



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

            ImageView editImage = findViewById(R.id.ProfilePic);
            editImage.setImageURI(imageUri);


        }
    }


    public void goToProfile(){
        Intent intent = new Intent(this, ProfileActivity.class);
        startActivity(intent);
    }

}








