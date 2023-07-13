package com.example.petshop.untils;

import com.example.petshop.models.AppMessage;
import com.example.petshop.models.ResetPasswordRequest;
import com.example.petshop.models.User;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Query;

public interface ApiService {

    @FormUrlEncoded
    @POST("/auth/signin")
    Call<AppMessage> signIn(@Field("username") String username, @Field("password") String password);

    @PUT("/auth/reset-password")
    Call<AppMessage> resetPassword(@Body ResetPasswordRequest request);
    @POST("/auth/register")
    Call<AppMessage> registerUser(@Body User user);
    @GET("/products/all")
    Call<AppMessage> getAllProduct(@Query("search") String query);

}
