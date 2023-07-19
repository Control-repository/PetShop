package com.example.petshop;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
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
    TextInputLayout ip_username,ip_fullname,ip_password,ip_confirm_password,ip_email;
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
        ip_username =findViewById(R.id.ip_username);
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

        String username = ip_username.getEditText().getText().toString().trim();
        String fullname = ip_fullname.getEditText().getText().toString().trim();
        String password = ip_password.getEditText().getText().toString().trim();
        String email = ip_email.getEditText().getText().toString().trim();
        String confirm_password = ip_confirm_password.getEditText().getText().toString().trim();

        if(validateInput()){
            if(!password.equals(confirm_password)){
                ip_confirm_password.setError("Mật khẩu không chính xác");
                return;
            }else{
                ip_confirm_password.setError(null);
            }

            dialog.setMessage("Loading...");
            dialog.show();

            User user = new User();
            user.setFullName(fullname);
            user.setEmail(email);
            user.setUsername(username);
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
                            //Hiện thị lỗi tại các input tương ứng
                            if(response.code()== 409){
                                ip_username.setError(message);
                            }else{
                                ip_username.setError(null);
                            }

                            if(response.code() == 400){
                                ip_email.setError(message);
                            }else{
                                ip_email.setError(null);
                            }

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
    }

    private void clickBackLogin(){
        startActivity(new Intent(RegisterActivity.this,LoginActivity.class));
        finish();
    }

    private boolean validateInput() {

        String username = ip_username.getEditText().getText().toString().trim();
        String fullname = ip_fullname.getEditText().getText().toString().trim();
        String password = ip_password.getEditText().getText().toString().trim();
        String email = ip_email.getEditText().getText().toString().trim();
        String confirm_password = ip_confirm_password.getEditText().getText().toString().trim();

        if(CheckInput.validateInput(username,ip_username) &&
                CheckInput.validateInput(fullname,ip_fullname) &&
                CheckInput.validateInput(password,ip_password) &&
                CheckInput.validateInput(email,ip_email) &&
                CheckInput.validateInput(confirm_password,ip_confirm_password)){
            return true;
        }
        return false;
    }
}