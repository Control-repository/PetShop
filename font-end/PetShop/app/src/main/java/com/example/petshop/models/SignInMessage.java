package com.example.petshop.models;

import com.google.gson.annotations.SerializedName;

public class SignInMessage {
    @SerializedName("message")
    private String message;
    @SerializedName("user")
    private User user;
    public SignInMessage(String message) {
        this.message = message;
    }
    public SignInMessage(){

    }
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public SignInMessage(String message, User user) {
        this.message = message;
        this.user = user;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
