package com.example.petshop.untils;

import com.example.petshop.models.AppMessage;
import com.example.petshop.models.ForgotPasswordRequest;
import com.example.petshop.models.ResetPasswordRequest;
import com.example.petshop.models.User;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiService {
    //Kiểm tra đăng nhập
    @FormUrlEncoded
    @POST("/auth/signin")
    Call<AppMessage> signIn(@Field("username") String username, @Field("password") String password);

    //Reset Password user khi quên
    @FormUrlEncoded
    @PUT("/auth/reset-password/{resetToken}")
    Call<AppMessage> resetPassword(@Field("password") String password, @Path("resetToken") String token);

    //Gửi token xác nhận
    @FormUrlEncoded
    @POST("/auth/forgot-password")
    Call<AppMessage> forgotPassword(@Field("email") String request);

    //Đăng kí tài khoản mới
    @POST("/auth/register")
    Call<AppMessage> registerUser(@Body User user);

    //Lấy tất cả người dùng
    @GET("/auth/all/full")
    Call<AppMessage> getAllUser();

    //Lấy tất cả user trừ user đang sử dụng
    @GET("/auth/all/current")
    Call<AppMessage> getAllCurrentUser();

    //lấy danh sách sản phẩm
    @GET("/products/all")
    Call<AppMessage> getAllProduct(@Query("search") String query);

}
