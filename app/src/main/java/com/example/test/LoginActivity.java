package com.example.test;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.test.dao.RemoteUserDAO;
import com.example.test.tables.RemoteUsers;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

// Asger
public class LoginActivity extends AppCompatActivity {
    public TextView newUserMsg;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);





        Intent newUserIntent = getIntent();
        String newUserMessage = newUserIntent.getStringExtra("NEWUSERMSG");
        TextView newUserMsg = findViewById(R.id.loginJoinMsg);
        this.newUserMsg = newUserMsg;
        newUserMsg.setVisibility(View.VISIBLE);
        newUserMsg.setText(newUserMessage);
    }

    public void login(View view){
        EditText userName = findViewById(R.id.usernameInput);
        String strUsername = userName.getText().toString();
        EditText password = findViewById(R.id.passwordInput);
        String strPassword = password.getText().toString();



        RemoteUserDAO remoteUserDAO = RemoteClient.getRetrofitInstance().create(RemoteUserDAO.class);

        Call<List<RemoteUsers>> userFromId = remoteUserDAO.getUserFromId("eq."+strUsername);

        userFromId.enqueue(new Callback<List<RemoteUsers>>() {
            @Override
            public void onResponse(Call<List<RemoteUsers>> call, Response<List<RemoteUsers>> response) {

                if(response.body().size()==1){

                    for(RemoteUsers u : response.body()) {
                        String s = u.getName();


                        String[] tempString = s.split("@|]", -2);

                        String username = "";
                        String password = "";
                        String profileIMG = "";


                        for (String str : tempString) {
                            if (str.contains("PWD[")) {
                                password = str.substring(4, str.length());

//                                System.out.println(password);
                            } else if (str.contains("IMG[")) {
                                profileIMG = str.substring(4, str.length());
                            } else {
                                username = username+ str;
//                                System.out.println(username);
                            }

                        }
                        if (strPassword.equals(password)) {
                            LogSession.setSession(u.getId(), username, profileIMG, u.getStamp(), password);
                            Intent intent = new Intent(LoginActivity.this, ProfileActivity.class);
                            startActivity(intent);
                        } else {
                            newUserMsg.setText("Incorrect Username or Password!");
                            newUserMsg.setTextColor(Color.RED);
                        }

                    }

                }else{
                        newUserMsg.setText("Incorrect Username or Password!");
                        newUserMsg.setTextColor(Color.RED);
                    }

            }

            @Override
            public void onFailure(Call<List<RemoteUsers>> call, Throwable t) {

            }
        });
        
    }
}
