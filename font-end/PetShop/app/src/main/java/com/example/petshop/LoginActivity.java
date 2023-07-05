package com.example.petshop;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class LoginActivity extends AppCompatActivity {
    private TextInputLayout ip_username,ip_password;
    private Button btn_SignIn;
    private TextView tv_miss_password;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ip_password = findViewById(R.id.ip_password);
        ip_username = findViewById(R.id.ip_username);
        btn_SignIn = findViewById(R.id.btn_SignIn);
        tv_miss_password = findViewById(R.id.tv_miss_password);

        btn_SignIn.setOnClickListener(v->{
            clickSignIn(v);
        });
    }

    private void clickSignIn(View v) {
        String username = ip_username.getEditText().getText().toString().trim();
        String password = ip_password.getEditText().getText().toString().trim();

        //Kiểm tra để trống của 2 input
        if(username.isEmpty()){
            ip_username.setError("Please enter your username");
        }else{
            ip_username.setError(null);
        }

        if(password.isEmpty()){
            ip_password.setError("Please enter your username");
        }else{
            ip_password.setError(null);
        }

        if(!username.isEmpty()&& !password.isEmpty()){
            
        }
    }


}