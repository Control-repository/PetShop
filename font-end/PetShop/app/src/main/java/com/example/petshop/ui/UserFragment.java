package com.example.petshop.ui;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.example.petshop.R;
import com.example.petshop.models.User;
import com.example.petshop.viewmodel.UserViewModel;
import com.google.android.material.textfield.TextInputLayout;

public class UserFragment extends Fragment {
    User user;
    private UserViewModel userViewModel;
    public UserFragment(){
    }
    private boolean isOpen =false;
    private TextInputLayout ip_fullname,ip_email,ip_phone;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user, container, false);
        ip_fullname = view.findViewById(R.id.ip_name_user);
        ip_email = view.findViewById(R.id.ip_email_user);
        ip_phone = view.findViewById(R.id.ip_phone_user);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ip_fullname.setEnabled(isOpen);
        ip_email.setEnabled(isOpen);
        ip_phone.setEnabled(isOpen);

    }

    public void onDestroyView() {
        super.onDestroyView();
    }

    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.menu_user, menu);
        super.onCreateOptionsMenu(menu, inflater);

    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.menu_edit){
            changeCheck();
        }
        if(id == R.id.menu_delete){
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle("Delete user");
            builder.setMessage("Are you sure you want to delete?");
            builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            });
            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            });
            AlertDialog dialog = builder.create();
            dialog.show();
        }
                return super.onOptionsItemSelected(item);

    }

    private boolean changeCheck(){
        return !isOpen;
    }
}