package com.example.test.dao;
import com.example.test.tables.RemoteComments;
import com.example.test.tables.RemotePosts;
import com.example.test.tables.RemoteUsers;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface RemoteCommentsDAO {


    @GET("comments")
    Call<List<RemoteComments>>getCommentsFromPostId(@Query("post_id") String id);

    @FormUrlEncoded
    @POST("comments")
    Call<RemoteComments> insertComment(@Field("user_id") String user_id,
                                 @Field("post_id") int post_id,
                                 @Field("content") String content,
                                 @Field("stamp") String stamp, @Header("Authorization")String token);

}
