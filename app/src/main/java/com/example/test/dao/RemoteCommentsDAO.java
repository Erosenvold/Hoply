package com.example.test.dao;
import com.example.test.tables.RemoteComments;
import com.example.test.tables.RemotePosts;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface RemoteCommentsDAO {


    @GET("comments")
    Call<List<RemoteComments>>getCommentsFromPostId(@Query("post_id") String id);

}
