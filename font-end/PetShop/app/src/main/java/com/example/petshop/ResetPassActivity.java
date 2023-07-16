package com.example.petshop;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.AlertDialog;
import android.content.DialogInterface;
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
    private TextInputLayout ip_new_password,ip_token;
    private Button btn_reset_password;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);
        //mapping
        ip_new_password = findViewById(R.id.ip_password);
        ip_token = findViewById(R.id.ip_token);
        btn_reset_password = findViewById(R.id.btn_reset);


        btn_reset_password.setOnClickListener(this::clickToResetPassword);
    }

    private void clickToResetPassword(View view) {
        String password = ip_new_password.getEditText().getText().toString().trim();
        String token = ip_token.getEditText().getText().toString().trim();

        if(CheckInput.validateInput(password,ip_new_password)
                &&CheckInput.validateInput(token,ip_token)){

            ApiService apiService = RetroClient.getApiService();
            AlertDialog.Builder builder = new AlertDialog.Builder(ResetPassActivity.this);
            Call<AppMessage> call = apiService.resetPassword(password,token);
            call.enqueue(new Callback<AppMessage>() {
                @Override
                public void onResponse(Call<AppMessage> call, Response<AppMessage> response) {
                    Log.i("CHECK CODE",response.code()+"");
                    //phần trả về giá trị
                    if(response.isSuccessful()){
                            //Hiển thị thông báo
                            builder.setTitle("Notification");
                            builder.setMessage("Reset Password successfully!");
                            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                    //Chuyển sang màn hình đăng nhập khi thành công đổi mật khẩu
                                    clickBackLogin();
                                }
                            });
                            AlertDialog dialog = builder.create();
                            dialog.show();

                    }else{
                        ResponseBody errorBody = response.errorBody();
                        try {
                            Gson gson = new Gson();
                            AppMessage message = gson.fromJson(errorBody.string(),AppMessage.class);
                            String errorMessage = message.getMessage();
                            Toast.makeText(getApplicationContext(), errorMessage, Toast.LENGTH_SHORT).show();
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                }

                @Override
                public void onFailure(Call<AppMessage> call, Throwable t) {
                    Log.i("ERROR ",t.toString());
                }
            });

        }
    }
    private void clickBackLogin(){
        startActivity(new Intent(ResetPassActivity.this,LoginActivity.class));
        finish();
    }
}