package com.example.petshop.utils;

import android.app.Activity;
import android.content.Context;
import android.view.inputmethod.InputMethodManager;

import androidx.fragment.app.FragmentActivity;

import com.example.petshop.R;

public class UtilClass {
    public static void hideKeyboard(FragmentActivity context){
        if(context.getCurrentFocus()!=null && context.getCurrentFocus().getWindowToken()!=null){
            InputMethodManager inputMethodManager =(InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(context.getCurrentFocus().getWindowToken(),0);
        }

    }
    public static void hideKeyboard(Activity context){
        if(context.getCurrentFocus()!=null && context.getCurrentFocus().getWindowToken()!=null) {
            InputMethodManager inputMethodManager =(InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(context.getCurrentFocus().getWindowToken(),0);
        }


    }
}
