package com.example.petshop.ui;

import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.petshop.R;
import com.example.petshop.models.AppMessage;
import com.example.petshop.models.User;
import com.example.petshop.utils.ApiService;
import com.example.petshop.utils.RetroClient;
import com.example.petshop.viewmodel.UserViewModel;
import com.google.android.material.textfield.TextInputLayout;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class PasswordFragment extends Fragment {
    public PasswordFragment(){}
    User thisUser;
    private TextInputLayout ip_password,ip_new_password,ip_check_password;
    private UserViewModel userViewModel;
    private Button btn_change,btn_cancel;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_password, container, false);
        //mapping
        ip_password = view.findViewById(R.id.ip_old_password);
        ip_new_password = view.findViewById(R.id.ip_new_password);
        ip_check_password = view.findViewById(R.id.ip_check_new_passowrd);
        btn_cancel = view.findViewById(R.id.btn_cancel);
        btn_change = view.findViewById(R.id.btn_change);
        //get user current
        userViewModel = new ViewModelProvider(requireActivity()).get(UserViewModel.class);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        userViewModel.getUserData().observe(getViewLifecycleOwner(),user ->{
            if(user!=null){
                thisUser = user;
            }
        });
        //clear password
        btn_cancel.setOnClickListener(v->{
            clearText();
        });
        //change password
        btn_change.setOnClickListener(v->{
            sendToChangePassword();
        });
    }

    private void clearText() {
        ip_password.getEditText().setText("");
        ip_new_password.getEditText().setText("");
        ip_check_password.getEditText().setText("");
    }

    private void sendToChangePassword() {
        if(!validateInput()){
            return;
        }
        String newPassword = ip_new_password.getEditText().getText().toString().trim();

        ProgressDialog dialog =new ProgressDialog(requireContext());
            dialog.setMessage("Loading...");

            RetroClient.setContext(requireContext());
            //apiservice
            ApiService apiService = RetroClient.getApiService();
            Call<AppMessage> call = apiService.setUserPassword(newPassword);

            call.enqueue(new Callback<AppMessage>() {
                @Override
                public void onResponse(Call<AppMessage> call, Response<AppMessage> response) {
                    dialog.dismiss();
                    if(response.isSuccessful()){
                        Toast.makeText(requireContext(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                        clearText();
                        thisUser.setPassword(newPassword);
                        userViewModel.setUserData(thisUser);
                    }
                }

                @Override
                public void onFailure(Call<AppMessage> call, Throwable t) {
                    dialog.dismiss();
                    Toast.makeText(requireContext(), "Something wrong!", Toast.LENGTH_SHORT).show();
                }
            });
    }

    private boolean validateInput(){
        String oldPassword = ip_password.getEditText().getText().toString().trim();
        String newPassword = ip_new_password.getEditText().getText().toString().trim();
        String checkPassword = ip_check_password.getEditText().getText().toString().trim();

        if(TextUtils.isEmpty(oldPassword)){
            ip_password.setError("Please input your current password!");
            return false;
        }else if(thisUser!=null && !oldPassword.equals(thisUser.getPassword())){
                    ip_password.setError("Password don't match!");
                    return false;
        }else{
            ip_password.setError(null);

        }
        if(TextUtils.isEmpty(newPassword)){
            ip_new_password.setError("Please input your new password!");
            return false;
        }else if(newPassword.length()<6){
            ip_new_password.setError("Password need more 6 characters!");
            return false;
        }else{
            ip_new_password.setError(null);
        }
        if(TextUtils.isEmpty(checkPassword)){
            ip_check_password.setError("Please input your new password!");
            return false;
        }else if(!newPassword.equals(checkPassword)){
            ip_check_password.setError("New password don't mach!");
            return false;
        }else{
            ip_check_password.setError(null);
        }


        return true;
    }
    public void onDestroyView() {
        super.onDestroyView();
    }

}