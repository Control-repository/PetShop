package com.example.petshop.models;

public class AppMessage {
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
