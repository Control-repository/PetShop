package com.example.petshop.ui;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.petshop.R;
import com.example.petshop.models.User;

public class UserFragment extends Fragment {
    User user;

    public UserFragment(){
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Bundle bundle = getArguments();
        if(bundle!=null){
            user =(User) bundle.getSerializable("User");
            Log.i("CHECK USER FRAGMENT "+ UserFragment.class.getSimpleName(), user.toString());
        }
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_user, container, false);
    }
    public void onDestroyView() {
        super.onDestroyView();
    }

}