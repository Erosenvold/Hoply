package com.example.test;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.example.test.dao.RemoteUserDAO;
import com.example.test.dao.UsersDao;
import com.example.test.tables.RemoteUsers;
import com.example.test.tables.Users;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

//Creates a new user in remote DB, then local DB. Sends to login page
public class CreateUserActivity extends AppCompatActivity {

    RemoteUserDAO remoteUsersDAO;
    private static String strUsername, strPassword, strUserID;
    public AppDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create);
        this.database = MainActivity.getDB();
    }
    //Checks if given username + ID is null or empty.
    //If not then inserts user in remote DB and local DB (with method insertUser())
    public void createUser(View view){

        TextView errorMsg = findViewById(R.id.createUserError);
        remoteUsersDAO = RemoteClient.getRetrofitInstance().create(RemoteUserDAO.class);
        EditText username = findViewById(R.id.createUsername);
        strUsername = username.getText().toString();
        EditText password = findViewById(R.id.createPassword);
        strPassword = password.getText().toString();
        EditText userID = findViewById(R.id.createUserID);
        strUserID = userID.getText().toString();
        //Checks for null or empty input
        if(strUsername != null && !strUsername.isEmpty() && !strUserID.isEmpty()){
            Call<List<RemoteUsers>> getUserFromId = remoteUsersDAO.getUserFromId("eq."+strUserID);
            //Calls remote DB
            getUserFromId.enqueue(new Callback<List<RemoteUsers>>() {
                @Override
                public void onResponse(Call<List<RemoteUsers>> call, Response<List<RemoteUsers>> response) {
                    //Checks if user already exists
                    if(response.body().size()==0){
                        //Method that inserts user called here
                        insertUser();

                    }else{
                        errorMsg.setVisibility(View.VISIBLE);
                        errorMsg.setTextColor(Color.RED);
                        errorMsg.setText("An account with this name already exists");
                    }

                }

                @Override
                public void onFailure(Call<List<RemoteUsers>> call, Throwable t) {
                    //Error message       System.out.println("Failure : "+ t.getMessage());
                }
            });
        }else{
            //Gives response to user if username or ID is empty
            errorMsg.setVisibility(View.VISIBLE);
            errorMsg.setTextColor(Color.RED);
            errorMsg.setText("Please enter something!");
        }
    }
    //Inserts user in remote DB then local DB
    public void insertUser() {

        String stamp;
        Date currDate = new Date();
        SimpleDateFormat time = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
        stamp = time.format(currDate);


        Call<Void> insertUser = remoteUsersDAO.insertUser(strUserID, strUsername + "@PWD[" + strPassword + "]",
                stamp, "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJyb2xlIjoiYXBwMjAyMCJ9.PZG35xIvP9vuxirBshLunzYADEpn68wPgDUqzGDd7ok");
        insertUser.enqueue(new Callback<Void>() {
            //Inserts user in remote DB
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                //Inserts user in local DB
                UsersDao usersDao = database.getAllUsers();
                Users user = new Users();
                user.id = strUserID;
                user.username = strUsername+ "@PWD[" + strPassword + "]";
                user.timeCreated = stamp;
                usersDao.createNewUser(user);
                System.out.println("local user name : "+usersDao.getUsernameFromID(strUserID));

                //sends user to LoginActivity
                Intent intent = new Intent(CreateUserActivity.this, LoginActivity.class);
                startActivity(intent);
            }
            @Override
            public void onFailure(Call<Void> call, Throwable t) {
            //Error message       System.out.println("Failure : " + t.getMessage());
            }
        });

    }

}