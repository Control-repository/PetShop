package com.example.petshop.utils;

import com.example.petshop.models.AppMessage;
import com.example.petshop.models.User;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiService {
    /*
    * Gọi bên auth
     */
    //Kiểm tra đăng nhập
    @FormUrlEncoded
    @POST("/auth/signin")
    Call<AppMessage> signIn(@Field("username") String username, @Field("password") String password);
    @GET("/auth/logout")
    Call<Void> getLogout();
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
    //update thông tin user
    @PUT("/auth/update/infor")
    Call<AppMessage> setUserInformation(@Body User user);
    //reset password for current user
    @FormUrlEncoded
    @PUT("/auth/update/password")
    Call<AppMessage> setUserPassword(@Field("password")String password);
    //Delete user
    @DELETE("/auth/delete/user/{username}")
    Call<AppMessage> deleteUser(@Path("username")String username);
    /*
    * Gọi bên phía sản phẩm
    */
    //lấy danh sách sản phẩm
    @GET("/products/all")
    Call<AppMessage> getAllProduct(@Query("search") String query);

}
