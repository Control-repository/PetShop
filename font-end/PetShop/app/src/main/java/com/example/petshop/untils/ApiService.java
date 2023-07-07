package com.example.petshop.untils;

import com.example.petshop.models.ResetPasswordRequest;
import com.example.petshop.models.SignInMessage;
import com.example.petshop.models.User;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface ApiService {

    @FormUrlEncoded
    @POST("/auth/signin")
    Call<SignInMessage> signIn(@Field("username") String username, @Field("password") String password);

    @POST("/auth/reset-password")
    Call<ResponseBody> resetPassword(@Body ResetPasswordRequest request);
}
