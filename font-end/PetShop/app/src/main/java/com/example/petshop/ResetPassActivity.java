package com.example.petshop;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.petshop.models.AppMessage;
import com.example.petshop.models.ResetPasswordRequest;
import com.example.petshop.untils.ApiService;
import com.example.petshop.untils.CheckInput;
import com.example.petshop.untils.RetroClient;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.Objects;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ResetPassActivity extends AppCompatActivity {
    private TextInputLayout ip_username,ip_new_password,ip_token;
    private Button btn_reset_password;
    private TextView tv_login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);
        //Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        //mapping
        ip_username = findViewById(R.id.ip_username);
        ip_new_password = findViewById(R.id.ip_password);
        ip_token = findViewById(R.id.ip_token);
        btn_reset_password = findViewById(R.id.btn_reset);
        tv_login = findViewById(R.id.tv_signin);

        tv_login.setOnClickListener(v->{
            clickBackLogin();
        });

        btn_reset_password.setOnClickListener(this::clickToResetPassword);
    }

    private void clickToResetPassword(View view) {
        String username = ip_username.getEditText().getText().toString().trim();
        String password = ip_new_password.getEditText().getText().toString().trim();
        String token = ip_token.getEditText().getText().toString().trim();

        if(CheckInput.validateInput(username,ip_username)&& CheckInput.validateInput(password,ip_new_password)
                &&CheckInput.validateInput(token,ip_token)){

            ApiService apiService = RetroClient.getApiService();

            ResetPasswordRequest request = new ResetPasswordRequest(token,password,username);

            Call<AppMessage> call = apiService.resetPassword(request);
            call.enqueue(new Callback<AppMessage>() {
                @Override
                public void onResponse(Call<AppMessage> call, Response<AppMessage> response) {
                    Log.i("CHECK CODE",response.code()+"");

                    //phần trả về giá trị
                    if(response.isSuccessful()){
                        //Hiển thị thông báo
                        Toast.makeText(ResetPassActivity.this, "Reset Password successfully!", Toast.LENGTH_SHORT).show();
                        //Chuyển sang màn hình đăng nhập khi thành công đổi mật khẩu
                        startActivity(new Intent(ResetPassActivity.this,LoginActivity.class));
                        finish();
                    }
                    if(response.errorBody()!=null){
                        try {
                            ResponseBody errorBody = response.errorBody();
                            Gson gson = new Gson();
                            AppMessage message = gson.fromJson(errorBody.string(),AppMessage.class);
                            String errorMessage = message.getMessage();
                            //Đối với sai token
                            if(response.code()==404){
                                ip_token.setError(errorMessage);
                            }else{
                                ip_token.setError(null);
                            }
                            //Hiển thị thông báo mỗi lỗi
                            Toast.makeText(ResetPassActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
                        }catch (Exception e){
                            e.printStackTrace();
                        }

                    }

                }

                @Override
                public void onFailure(Call<AppMessage> call, Throwable t) {

                }
            });

        }
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
    private void clickBackLogin(){
        startActivity(new Intent(ResetPassActivity.this,LoginActivity.class));
        finish();
    }
}