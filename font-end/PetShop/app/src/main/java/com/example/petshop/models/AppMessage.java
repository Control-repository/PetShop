package com.example.petshop.models;

import com.google.gson.annotations.SerializedName;

public class AppMessage {
    @SerializedName("message")
    private String message;

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
}
