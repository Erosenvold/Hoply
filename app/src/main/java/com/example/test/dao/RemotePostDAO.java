package com.example.test.dao;

import com.example.test.tables.RemotePosts;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Query;


public interface RemotePostDAO {

    @GET("posts")
    Call<List<RemotePosts>>getAllPosts();

    @GET("posts")
    Call<List<RemotePosts>>getPostFromId(@Query("id") String id);

    @GET("posts")
    Call<List<RemotePosts>>getPostsDESC(@Query("order") String orderBy, @Query("limit") int limit, @Query("offset") int offset);

    @FormUrlEncoded
    @POST("posts")
    Call<RemotePosts> setPost (@Field("id") int id, @Field("user_id") String user_id, @Field("content") String content, @Field("stamp") String stamp, @Header("Authorization") String bearerToken);


}