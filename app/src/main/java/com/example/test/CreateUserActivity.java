package com.example.test;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import com.example.test.dao.RemoteUserDAO;
import com.example.test.dao.UsersDao;
import com.example.test.tables.RemoteUsers;
import com.example.test.tables.Users;

import org.w3c.dom.Text;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class CreateUserActivity extends AppCompatActivity {
    public AppDatabase database;
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create);

        this.database = MainActivity.getDB();
    }
    public void createUser(View view){
        TextView errorMsg = findViewById(R.id.createUserError);

        RemoteUserDAO remoteUsersDAO = RemoteClient.getRetrofitInstance().create(RemoteUserDAO.class);




        EditText username = findViewById(R.id.createUsername);
        String strUsername = username.getText().toString();
        EditText password = findViewById(R.id.createPassword);
        String strPassword = password.getText().toString();
        EditText userID = findViewById(R.id.createUserID);
        String strUserID = userID.getText().toString();

        Call<List<RemoteUsers>> getUserFromId = remoteUsersDAO.getUserFromId("eq."+strUserID);
        getUserFromId.enqueue(new Callback<List<RemoteUsers>>() {
            @Override
            public void onResponse(Call<List<RemoteUsers>> call, Response<List<RemoteUsers>> response) {

                if(response.body().size()==0){
                    //create user
                    System.out.println("No user by this name");

                }else{
                    errorMsg.setVisibility(View.VISIBLE);
                    errorMsg.setText("An account with this email already exists");
                }

//                if(userDao.getUserID(strUserID) == null){
//                    if(!strUserID.trim().isEmpty() && !strUsername.trim().isEmpty() && !strPassword.trim().isEmpty()) {
//
//                        Users newUser = new Users();
//                        newUser.id = strUserID;
//                        newUser.username = strUsername;
//                        newUser.password = strPassword;
//
//                        newUser.username = newUser.username+"@PWD["+strPassword+"]";
//
//                        newUser.timeCreated = System.currentTimeMillis();
//                        userDao.createNewUser(newUser);
//
//                        Intent intent = new Intent(this, LoginActivity.class);
//                        intent.putExtra("NEWUSERMSG","Congrats on joining Hoply");
//
//                        startActivity(intent);
//
//                    }else{
//                        errorMsg.setVisibility(View.VISIBLE);
//                        errorMsg.setText("Please enter a username and a password");
//                    }
//
//                }else{
//
//                    errorMsg.setVisibility(View.VISIBLE);
//                    errorMsg.setText("An account with this email already exists");
//
//                }

            }

            @Override
            public void onFailure(Call<List<RemoteUsers>> call, Throwable t) {

            }
        });
//               if(userDao.getUserID(strUserID) == null){
//                   if(!strUserID.trim().isEmpty() && !strUsername.trim().isEmpty() && !strPassword.trim().isEmpty()) {
//
//                       Users newUser = new Users();
//                       newUser.id = strUserID;
//                       newUser.username = strUsername;
//                       newUser.password = strPassword;
//
//                       newUser.username = newUser.username+"@PWD["+strPassword+"]";
//
//                       newUser.timeCreated = System.currentTimeMillis();
//                       userDao.createNewUser(newUser);
//
//                       Intent intent = new Intent(this, LoginActivity.class);
//                       intent.putExtra("NEWUSERMSG","Congrats on joining Hoply");
//
//                       startActivity(intent);
//
//                   }else{
//                       errorMsg.setVisibility(View.VISIBLE);
//                       errorMsg.setText("Please enter a username and a password");
//                   }
//
//             }else{
//
//                   errorMsg.setVisibility(View.VISIBLE);
//                   errorMsg.setText("An account with this email already exists");
//
//               }


    }


}
