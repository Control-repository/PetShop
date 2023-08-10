package com.example.petshop.ui;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.petshop.LoginActivity;
import com.example.petshop.MainActivity;
import com.example.petshop.R;
import com.example.petshop.models.AppMessage;
import com.example.petshop.models.User;
import com.example.petshop.utils.ApiService;
import com.example.petshop.utils.RetroClient;
import com.example.petshop.viewmodel.UserViewModel;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserFragment extends Fragment {
    User thisUser;
    private UserViewModel userViewModel;
    public UserFragment(){
    }
    private TextInputLayout ip_fullname,ip_email,ip_phone;
    private Button btn_change;
    private ApiService apiService ;
    private  SharedPreferences sharedPreferences;
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
        btn_change = view.findViewById(R.id.btn_change);
        RetroClient.setContext(requireContext());
        apiService = RetroClient.getApiService();
        //phần lưu token
        sharedPreferences = requireContext().getSharedPreferences("Request", Context.MODE_PRIVATE);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        userViewModel = new ViewModelProvider(requireActivity()).get(UserViewModel.class);
        userViewModel.getUserData().observe(getViewLifecycleOwner(),user->{
            if(user!=null){
                thisUser = user;
                ip_fullname.getEditText().setText(user.getFullName());
                ip_phone.getEditText().setText(user.getPhone());
                ip_email.getEditText().setText(user.getEmail());
            }

        });
        phoneFocusListener();
        emailFocusListener();
        btn_change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(validateInput()) {
                    sendToChangeInformationUser();
                }
            }
        });
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
            changeEnable();
        }
        if(id == R.id.menu_delete){
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle("Delete user");
            builder.setMessage("Are you sure you want to delete?");
            builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    sendToDeleteUser();
                }
            });
            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            AlertDialog dialog = builder.create();
            dialog.show();
        }
                return super.onOptionsItemSelected(item);
    }

    private void changeEnable() {
        boolean isEnable = ip_fullname.isEnabled();
        ip_fullname.setEnabled(!isEnable);
        ip_email.setEnabled(!isEnable);
        ip_phone.setEnabled(!isEnable);
        btn_change.setVisibility(isEnable ?View.GONE:View.VISIBLE);
    }

    //Thay đổi thông tin của người dùng đang đăng nhập
    private void sendToChangeInformationUser() {
        ProgressDialog dialog = new ProgressDialog(requireContext());
        dialog.setMessage("Loading...");
        String fullname = ip_fullname.getEditText().getText().toString();
        String phone = ip_phone.getEditText().getText().toString();
        String email = ip_email.getEditText().getText().toString();


            RetroClient.setContext(requireContext());
            ApiService apiService = RetroClient.getApiService();

            User userChange = new User();
            userChange.setFullName(fullname);
            userChange.setPhone(phone);
            userChange.setEmail(email);
            Call<AppMessage> call = apiService.setUserInformation(userChange);
            call.enqueue(new Callback<AppMessage>() {
                @Override
                public void onResponse(Call<AppMessage> call, Response<AppMessage> response) {
                    dialog.dismiss();
                    if(response.isSuccessful()){
                        userViewModel.setUserData(response.body().getUser());
                        Toast.makeText(requireContext(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                        changeEnable();
                    }else{
                        ResponseBody errorBody = response.errorBody();
                        if(errorBody!=null){
                            try {
                                AppMessage appMessage = new Gson().fromJson(errorBody.string(), AppMessage.class);
                                Toast.makeText(requireContext(), appMessage.getMessage(), Toast.LENGTH_SHORT).show();
                            }catch (Exception e){
                                e.printStackTrace();
                            }
                        }
                    }
                }
                @Override
                public void onFailure(Call<AppMessage> call, Throwable t) {

                }
            });

    }

    //Xóa nguời dùng hiện tại và logout ra màn hình đăng nhập
    private void sendToDeleteUser(){
        if(thisUser!=null){
            Call<AppMessage> call = apiService.deleteUser(thisUser.getEmail());
            call.enqueue(new Callback<AppMessage>() {
                @Override
                public void onResponse(Call<AppMessage> call, Response<AppMessage> response) {
                    if (response.isSuccessful()) {
                        AppMessage message = response.body();
                        String messageStr = message.getMessage();

                        Call<Void> logoutCall = apiService.getLogout();
                        logoutCall.enqueue(new Callback<Void>() {
                            @Override
                            public void onResponse(Call<Void> call, Response<Void> response) {
                                if(response.isSuccessful()){
                                    Toast.makeText(requireContext(), messageStr, Toast.LENGTH_SHORT).show();
                                    sharedPreferences.edit().remove("token").apply();
                                    startActivity(new Intent(requireContext(), LoginActivity.class));
                                    requireActivity().finish();
                                }
                            }
                            @Override
                            public void onFailure(Call<Void> call, Throwable t) {
                                Toast.makeText(requireContext(), "Something wrong with logout!", Toast.LENGTH_SHORT).show();
                            }
                        });

                    }
                }

                @Override
                public void onFailure(Call<AppMessage> call, Throwable t) {
                    Toast.makeText(requireContext(), "Something wrong with delete!", Toast.LENGTH_SHORT).show();
                }
            });

        }
    }
    private boolean validateInput(){
        String fullnameEror = ip_fullname.getEditText().getError().toString();
        String phoneError = ip_phone.getEditText().getError().toString();
        String emailError = ip_email.getEditText().getError().toString();

        return fullnameEror == null && phoneError == null && emailError == null;
    }

    private void phoneFocusListener(){
        ip_phone.getEditText().setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                ip_phone.getEditText().setError(validPhone());
            }
        });
    }

    private CharSequence validPhone() {
        String phoneText = ip_phone.getEditText().getText().toString().trim();
        if(phoneText.length() !=10){
            return "phải đủ 10 số";
        }

        if(phoneText.matches(".*[0-9].*")){
            return "phải là số";
        }
        return null;
    }
    private void emailFocusListener(){
        ip_email.getEditText().setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                ip_email.getEditText().setError(validEmail());
            }
        });
    }

    private CharSequence validEmail() {
        String emailText = ip_email.getEditText().getText().toString().trim();

        if(!Patterns.EMAIL_ADDRESS.matcher(emailText).matches()){
            return "Sai định dạng email";
        }
        return null;
    }
}