package com.example.petshop;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.petshop.models.AppMessage;
import com.example.petshop.models.User;
import com.example.petshop.utils.ApiService;
import com.example.petshop.utils.CheckInput;
import com.example.petshop.utils.RetroClient;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.Objects;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity {
    Button btn_register;
    TextInputLayout ip_fullname,ip_password,ip_confirm_password,ip_email;
    TextView tv_signIn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        //Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        //mapping
        ip_fullname =findViewById(R.id.ip_fullname);
        ip_password =findViewById(R.id.ip_password);
        ip_confirm_password =findViewById(R.id.ip_repeat_password);
        ip_email =findViewById(R.id.ip_email);

        btn_register =findViewById(R.id.btn_register);
        tv_signIn = findViewById(R.id.tv_signin);

        tv_signIn.setOnClickListener(v->{
            clickBackLogin();
        });
        btn_register.setOnClickListener(this::clickToRegisterUser);
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if(id == android.R.id.home){
            clickBackLogin();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    private void clickToRegisterUser(View v) {
        ProgressDialog dialog = new ProgressDialog(RegisterActivity.this);
        if(!validateInput()){
            return;
        }
        String fullname = ip_fullname.getEditText().getText().toString().trim();
        String password = ip_password.getEditText().getText().toString().trim();
        String email = ip_email.getEditText().getText().toString().trim();


            dialog.setMessage("Loading...");
            dialog.show();

            User user = new User();
            user.setFullName(fullname);
            user.setEmail(email);
            user.setPassword(password);
            user.setRole(1);

            //retrofit
            RetroClient.setContext(getApplicationContext());
            ApiService apiService = RetroClient.getApiService();

            //Call
            Call<AppMessage> call = apiService.registerUser(user);
            call.enqueue(new Callback<AppMessage>() {
                @Override
                public void onResponse(Call<AppMessage> call, Response<AppMessage> response) {
                    dialog.dismiss();
                    if(response.body()!=null ) {
                        AppMessage message = response.body();
                        Toast.makeText(RegisterActivity.this, message.getMessage(), Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                        finish();
                    }
                    ResponseBody errorBody = response.errorBody();
                    if(errorBody!=null){
                        try {
                           String errorJson = errorBody.string();
                        Gson gson = new Gson();
                            // Sử dụng lớp mô hình (model class) để chuyển đổi dữ liệu phản hồi thành đối tượng tương ứng
                            AppMessage errorResponse = gson.fromJson(errorJson, AppMessage.class);
                            String message = errorResponse.getMessage();

                            // Sử dụng giá trị message
                            Toast.makeText(RegisterActivity.this,message,Toast.LENGTH_SHORT).show();
                        }
                        catch (IOException e) {
                            throw new RuntimeException(e);
                        }

                    }

                }

                @Override
                public void onFailure(Call<AppMessage> call, Throwable t) {
                    dialog.dismiss();
                    Toast.makeText(RegisterActivity.this, "Register failed!", Toast.LENGTH_SHORT).show();
                }
            });
    }

    private void clickBackLogin(){
        startActivity(new Intent(RegisterActivity.this,LoginActivity.class));
        finish();
    }

    private boolean validateInput() {
        String fullname = ip_fullname.getEditText().getText().toString().trim();
        String password = ip_password.getEditText().getText().toString().trim();
        String email = ip_email.getEditText().getText().toString().trim();
        String confirm_password = ip_confirm_password.getEditText().getText().toString().trim();

        if(TextUtils.isEmpty(fullname)){
            ip_fullname.setError("Please input your full name!");
            return false;
        }else{
            ip_fullname.setError(null);

        }
        if(TextUtils.isEmpty(email)){
            ip_email.setError("Please input your email!");
            return false;
        }else if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            ip_email.setError("Invaild email address!");
            return false;
        }else{
            ip_email.setError(null);

        }
        if(TextUtils.isEmpty(password)){
            ip_password.setError("Please input your password!");
            return false;
        }else if(password.length()<6){
            ip_password.setError("Password need more 6 characters!");
            return false;
        }else{
            ip_password.setError(null);

        }
        if(TextUtils.isEmpty(confirm_password)){
            ip_confirm_password.setError("Please input your password!");
            return false;
        }else if(confirm_password.length()<6){
            ip_confirm_password.setError("Password need more 6 characters!");
            return false;
        }else if(!confirm_password.equals(password)){
            ip_confirm_password.setError("Password don't match!");
            return false;
        }else{
            ip_confirm_password.setError(null);
        }
        return true;
    }
}