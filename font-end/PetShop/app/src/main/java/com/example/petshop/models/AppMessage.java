package com.example.petshop.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class AppMessage {
    @SerializedName("message")
    private String message;
    @SerializedName("products")
    private List<Product> productList;
    @SerializedName("token")
    private String token;
    @SerializedName("user")
    private User user;
    @SerializedName("users")
    private List<User> listUser;

    public List<User> getListUser() {
        return listUser;
    }

    public void setListUser(List<User> listUser) {
        this.listUser = listUser;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public AppMessage(){}
    public AppMessage(String message) {
        this.message = message;
    }

    public List<Product> getProductList() {
        return productList;
    }

    public void setProductList(List<Product> productList) {
        this.productList = productList;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
