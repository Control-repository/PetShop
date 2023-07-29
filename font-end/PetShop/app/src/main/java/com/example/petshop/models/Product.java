package com.example.petshop.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Product implements Serializable {
    @SerializedName("id")
    int id;
    @SerializedName("name")
    String name;
    @SerializedName("category")
    String category;
    @SerializedName("price")
    double price;
    @SerializedName("quantity")
    int quantity;
    @SerializedName("description")
    String description;
    @SerializedName("imageURL")
    String imageURL;
    @SerializedName("user_username")
    String user_username;

    public Product(){}

    public Product(int id, String name, String category, double price, int quantity, String description, String imageURL, String user_username) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.price = price;
        this.quantity = quantity;
        this.description = description;
        this.imageURL = imageURL;
        this.user_username = user_username;
    }
    public Product( String name, String category, double price, int quantity) {
        this.name = name;
        this.category = category;
        this.price = price;
        this.quantity = quantity;
    }
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public String getUser_username() {
        return user_username;
    }

    public void setUser_username(String user_username) {
        this.user_username = user_username;
    }

    @Override
    public String toString() {
        return "Product{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", category='" + category + '\'' +
                ", price=" + price +
                ", quantity=" + quantity +
                ", description='" + description + '\'' +
                ", imageURL='" + imageURL + '\'' +
                ", user_username='" + user_username + '\'' +
                '}';
    }
}
