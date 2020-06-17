package com.example.test.dao;

import com.example.test.tables.JoinPost;
import com.example.test.tables.RemotePosts;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;


public interface RemotePostDAO {

    @GET("posts")
    Call<List<RemotePosts>>getAllPost();

    @GET("posts")
    Call<List<RemotePosts>>getPostFromId(@Query("id") int id);

    @GET("posts")
    Call<List<RemotePosts>>getPostsDESC(@Query("order") String orderBy, @Query("limit") int limit, @Query("offset") int offset);

}