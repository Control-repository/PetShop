package com.example.petshop.untils;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetroClient {
    private static final String ROOT_URL = "http:192.168.15.136:3000/";

    private static Retrofit getRetrofitInstance(){
        return new Retrofit.Builder().baseUrl(ROOT_URL).addConverterFactory(GsonConverterFactory.create()).build();
    }

    public static ApiService getApiService(){
        return getRetrofitInstance().create(ApiService.class);
    }
}
