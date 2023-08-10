package com.example.petshop.utils;

import com.example.petshop.models.AppMessage;
import com.example.petshop.models.Product;
import com.example.petshop.models.User;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiService {
    /*
    * Gọi bên auth
     */
    //Kiểm tra đăng nhập
    @FormUrlEncoded
    @POST("/auth/signin")
    Call<AppMessage> signIn(@Field("email") String username, @Field("password") String password);
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
    @DELETE("/auth/delete/user/{email}")
    Call<AppMessage> deleteUser(@Path("email")String username);
    /*
    * Gọi bên phía sản phẩm
    */
    //lấy danh sách sản phẩm
    @GET("/products/all")
    Call<AppMessage> getAllProduct(@Query("search") String query);
    //thêm mới sản phẩm
    @Multipart
    @POST("/products/insert")
    Call<AppMessage> insertProductWithImage(
            @Part("name") RequestBody name,
            @Part("category") RequestBody category,
            @Part("price") RequestBody price,
            @Part("quantity") RequestBody quantity,
            @Part("description") RequestBody description,
            @Part MultipartBody.Part image);
    @POST("/products/insert")
    Call<AppMessage> insertProductWithoutImage(@Body Product product);

    @Multipart
    @PUT("/products/update/{id}")
    Call<AppMessage> updateProductWithImage(
            @Path("id") int id,
            @Part("name") RequestBody name,
            @Part("category") RequestBody category,
            @Part("price") RequestBody price,
            @Part("quantity") RequestBody quantity,
            @Part("description") RequestBody description,
            @Part MultipartBody.Part image);
    @PUT("/products/update/{id}")
    Call<AppMessage> updateProductWithoutImage(@Path("id") int id,@Body Product product);
    @DELETE("/products/delete/{id}")
    Call<AppMessage> deleteProduct(@Path("id") int id);

}
