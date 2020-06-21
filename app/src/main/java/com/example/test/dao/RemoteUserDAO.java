package com.example.test.dao;

import com.example.test.tables.RemoteUsers;

import java.util.List;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Query;
//Data Access Object for Remote Users DB
public interface RemoteUserDAO {

    //Returns list of users
    @GET("users")
    Call<List<RemoteUsers>>getAllUsers();

    //Returns list of users with given ID
    @GET("users")
    Call<List<RemoteUsers>>getUserFromId(@Query("id") String id);

    //Updates username with a given ID
    @FormUrlEncoded
    @PATCH("users")
    Call<RemoteUsers> updateUser (@Query("id") String id, @Field("name") String name, @Header("Authorization") String bearerToken);

    //Inserts new user into remote DB
    @FormUrlEncoded
    @POST("users")
    Call<Void> insertUser(@Field("id") String id,
                                 @Field("name") String name,
                                 @Field("stamp") String stamp,@Header("Authorization")String token);

}
