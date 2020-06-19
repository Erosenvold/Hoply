package com.example.test.dao;
import com.example.test.tables.RemoteUsers;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.Url;

public interface RemoteUserDAO {

    @GET("users")
    Call<List<RemoteUsers>>getAllUsers();

    @GET("users")
    Call<List<RemoteUsers>>getUserFromId(@Query("id") String id);

    @GET("users")
    Call<List<RemoteUsers>>getLimitedUsers(@Query("id") String id, @Query("limit") int limit );

    @FormUrlEncoded
    @PATCH("users")
    Call<RemoteUsers> updateUser (@Query("id") String id, @Field("name") String name, @Header("Authorization") String bearerToken);

    @FormUrlEncoded
    @POST("users")
    Call<RemoteUsers> insertUser(@Field("id") String id,
                                 @Field("name") String name,
                                 @Field("stamp") String stamp,@Header("Authorization")String token);

}
