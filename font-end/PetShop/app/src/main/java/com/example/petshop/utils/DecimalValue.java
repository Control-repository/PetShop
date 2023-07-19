package com.example.petshop.utils;


import java.text.DecimalFormat;

public class DecimalValue {


    public static String formatPrice(Double number){
        DecimalFormat decimalFormat = new DecimalFormat("#,###.##");
        return decimalFormat.format(number);
    }
}
