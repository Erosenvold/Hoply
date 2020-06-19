package com.example.test;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.test.dao.RemoteUserDAO;
import com.example.test.tables.RemoteUsers;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class CreateUserActivity extends AppCompatActivity {

    RemoteUserDAO remoteUsersDAO;
    private static String strUsername, strPassword, strUserID;


    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create);


    }
    public void createUser(View view){
        TextView errorMsg = findViewById(R.id.createUserError);

        remoteUsersDAO = RemoteClient.getRetrofitInstance().create(RemoteUserDAO.class);




        EditText username = findViewById(R.id.createUsername);
        strUsername = username.getText().toString();
        EditText password = findViewById(R.id.createPassword);
        strPassword = password.getText().toString();
        EditText userID = findViewById(R.id.createUserID);
        strUserID = userID.getText().toString();

        Call<List<RemoteUsers>> getUserFromId = remoteUsersDAO.getUserFromId("eq."+strUserID);
        getUserFromId.enqueue(new Callback<List<RemoteUsers>>() {
            @Override
            public void onResponse(Call<List<RemoteUsers>> call, Response<List<RemoteUsers>> response) {

                if(response.body().size()==0){
                    //create user
                    insertUser();

                }else{
                    errorMsg.setVisibility(View.VISIBLE);
                    errorMsg.setText("An account with this name already exists");
                }

            }

            @Override
            public void onFailure(Call<List<RemoteUsers>> call, Throwable t) {

            }
        });

    }

    public void insertUser(){
        String stamp;
        Date currDate = new Date();
        SimpleDateFormat time = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
        stamp = time.format(currDate);
        Call<RemoteUsers> insertUser = remoteUsersDAO.insertUser(strUserID, strUsername+"@PWD["+strPassword+"]",
                stamp, "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJyb2xlIjoiYXBwMjAyMCJ9.PZG35xIvP9vuxirBshLunzYADEpn68wPgDUqzGDd7ok");
        insertUser.enqueue(new Callback<RemoteUsers>() {
            @Override
            public void onResponse(Call<RemoteUsers> call, Response<RemoteUsers> response) {

            }

            @Override
            public void onFailure(Call<RemoteUsers> call, Throwable t) {
                System.out.println("Failure, no response : " + t.getMessage());
            }
        });
        Intent intent = new Intent(CreateUserActivity.this, LoginActivity.class);
        startActivity(intent);
    }


}
