package com.example.petshop.ui;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;


import com.example.petshop.R;
import com.example.petshop.models.User;


public class ProductFragment extends Fragment {
    User user;

    public ProductFragment(){}

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        Bundle bundle = getArguments();
        if(bundle!=null){
            user =(User) bundle.getSerializable("User");
            Log.i("CHECK USER FRAGMENT "+ ProductFragment.class.getSimpleName(), user.toString());
        }
        return inflater.inflate(R.layout.fragment_product,container,false);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

    }
}