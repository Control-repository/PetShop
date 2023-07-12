package com.example.petshop.untils;

import android.text.TextUtils;

import com.google.android.material.textfield.TextInputLayout;

public class CheckInput {
    public static boolean validateInput(String str, TextInputLayout input){
        if(TextUtils.isEmpty(str)){
            input.setError("Vui lòng nhập dữ liệu");
            return false;
        }else{
            input.setError(null);
            return true;
        }
    }
}
