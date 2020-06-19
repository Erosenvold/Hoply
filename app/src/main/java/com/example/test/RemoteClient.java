package com.example.test;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

// Erik
public class RemoteClient {
    private static Retrofit retrofit;
    private static final String BASE_URL = "https://caracal.imada.sdu.dk/app2020/";

    public static Retrofit getRetrofitInstance(){
        if(retrofit == null){
            retrofit = new Retrofit.Builder().baseUrl(BASE_URL).addConverterFactory(GsonConverterFactory.create()).build();
        }
        return retrofit;
    }
}
