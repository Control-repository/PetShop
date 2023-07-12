package com.example.petshop.ui;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;


import com.example.petshop.R;
import com.example.petshop.models.User;

public class HomeFragment extends Fragment {
    public HomeFragment(){}
    User user;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_home,container,false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if(getArguments()!=null){
            user =(User) getArguments().getSerializable("User");
            Log.i("CHECK USER FRAGMENT "+ HomeFragment.class.getSimpleName(), user.toString());
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}