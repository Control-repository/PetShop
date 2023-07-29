package com.example.petshop.utils;

import android.content.Context;
import android.content.SharedPreferences;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetroClient {
    private static final String ROOT_URL = "http:192.168.15.136:3000/";
    private static Context context;
    private static Retrofit getRetrofitInstance(){
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder()
                .connectTimeout(30, TimeUnit.SECONDS) // Thời gian kết nối tối đa (30 giây)
                .readTimeout(30, TimeUnit.SECONDS) // Thời gian đọc dữ liệu tối đa (30 giây)
                .writeTimeout(30, TimeUnit.SECONDS); // Thời gian ghi dữ liệu tối đa (30 giây);
        httpClient.addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request originalRequest = chain.request();

                // Đọc cookie "token" từ SharedPreferences
                SharedPreferences sharedPreferences = context.getSharedPreferences("Request", Context.MODE_PRIVATE);
                String token = sharedPreferences.getString("token", "");

                // Thêm cookie vào header của yêu cầu
                Request newRequest = originalRequest.newBuilder()
                        .header("Cookie", "token=" + token)
                        .build();

                return chain.proceed(newRequest);
            }
        });

        return new Retrofit.Builder().baseUrl(ROOT_URL).addConverterFactory(GsonConverterFactory.create()).client(httpClient.build()).build();
    }
    
    public static void setContext(Context context){
       RetroClient.context = context;
    }
    
    public static ApiService getApiService(){
        return getRetrofitInstance().create(ApiService.class);
    }
}
