package com.example.petshop;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.petshop.models.AppMessage;
import com.example.petshop.models.SignInMessage;
import com.example.petshop.models.User;
import com.example.petshop.untils.ApiService;
import com.example.petshop.untils.RetroClient;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;

import java.io.IOException;
import java.io.Serializable;
import java.util.Objects;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {
    private TextInputLayout ip_username,ip_password;
    private Button btn_SignIn;
    private TextView tv_miss_password,tv_register;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        //mapping
        ip_password = findViewById(R.id.ip_password);
        ip_username = findViewById(R.id.ip_username);
        btn_SignIn = findViewById(R.id.btn_SignIn);
        tv_miss_password = findViewById(R.id.tv_miss_password);
        tv_register = findViewById(R.id.tv_register);

        btn_SignIn.setOnClickListener(this::clickSignIn);

        tv_miss_password.setOnClickListener(v->{
            startActivity(new Intent(LoginActivity.this,ResetPassActivity.class));
            finish();
        });
        tv_register.setOnClickListener(v->{
            startActivity(new Intent(LoginActivity.this,RegisterActivity.class));
            finish();
        });

    }

    private void clickSignIn(View v) {
        final ProgressDialog dialog = new ProgressDialog(LoginActivity.this);

        String username = ip_username.getEditText().getText().toString().trim();
        String password = ip_password.getEditText().getText().toString().trim();

        //Kiểm tra để trống của 2 input
        if(username.isEmpty()){
            ip_username.setError("Please enter your username");
        }else{
            ip_username.setError(null);
        }

        if(password.isEmpty()){
            ip_password.setError("Please enter your password");
        }else{
            ip_password.setError(null);
        }

        if(!username.isEmpty()&& !password.isEmpty()){
            dialog.setMessage("Loading...");
            dialog.show();
            ApiService apiService = RetroClient.getApiService();

            //Calling JSOn
            Call<SignInMessage> call = apiService.signIn(username,password);
            call.enqueue(new Callback<SignInMessage>() {
                @Override
                public void onResponse(Call<SignInMessage> call, Response<SignInMessage> response) {
                    dialog.dismiss();
                    if(response.isSuccessful()){

                        SignInMessage signInMessage = response.body();
                        assert signInMessage != null;
                        User user = signInMessage.getUser();

                        if (user != null) {
                            Bundle bundle = new Bundle();
                            bundle.putSerializable("User", (Serializable) user);
                            Toast.makeText(LoginActivity.this,"Login complete!", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            intent.putExtras(bundle);
                            startActivity(intent);
                            finish();
                        }
                    }else{
                        ResponseBody errorBody = response.errorBody();
                        try {
                            Gson gson = new Gson();
                            AppMessage message = gson.fromJson(errorBody.string(),AppMessage.class);
                            String errorMessage = message.getMessage();
                            Toast.makeText(getApplicationContext(), errorMessage, Toast.LENGTH_SHORT).show();
                        }catch (IOException e){
                            e.printStackTrace();
                        }
                    }

                }

                @Override
                public void onFailure(Call<SignInMessage> call, Throwable t) {
                    dialog.dismiss();
                    Toast.makeText(LoginActivity.this, "Login Failed!", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }


}