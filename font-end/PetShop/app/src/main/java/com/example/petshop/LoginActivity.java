package com.example.petshop;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.petshop.models.AppMessage;
import com.example.petshop.models.User;
import com.example.petshop.utils.ApiService;
import com.example.petshop.utils.CheckInput;
import com.example.petshop.utils.RetroClient;
import com.example.petshop.viewmodel.UserViewModel;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;

import java.io.IOException;

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
            startActivity(new Intent(LoginActivity.this,ForgotPasswordActivity.class));
            finish();
        });
        tv_register.setOnClickListener(v->{
            startActivity(new Intent(LoginActivity.this,RegisterActivity.class));
        });

    }

    private void clickSignIn(View v) {
        final ProgressDialog dialog = new ProgressDialog(LoginActivity.this);

        String username = ip_username.getEditText().getText().toString().trim();
        String password = ip_password.getEditText().getText().toString().trim();

        if(!validate()){
            return;
        }
            dialog.setMessage("Loading...");
            dialog.show();
            //truyền context
            RetroClient.setContext(getApplicationContext());

            ApiService apiService = RetroClient.getApiService();
            //Calling JSOn
            Call<AppMessage> call = apiService.signIn(username,password);
            call.enqueue(new Callback<AppMessage>() {
                @Override
                public void onResponse(Call<AppMessage> call, Response<AppMessage> response) {
                    dialog.dismiss();
                    if(response.isSuccessful()){

                        AppMessage signInMessage = response.body();
                        assert signInMessage !=null;
                        User user = signInMessage.getUser();

                        if(response.body().getToken()!=null && user != null){
                            //Luư token đăng nhập protect
                            SharedPreferences sharedPreferences = v.getContext().getSharedPreferences("Request", Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString("token",response.body().getToken());
                            editor.apply();
                            //Lưu user vào viewmodel để sử dụng trong ứng dụng
                            Bundle bundle =new Bundle();
                            bundle.putSerializable("User",user);
                            Log.i("LOGIN USER ",user.toString());
                            //Thông báo đăng nhập thành công và chuyển sang màn hình chính
                            Toast.makeText(LoginActivity.this,"Login complete!", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            intent.putExtras(bundle);
                            startActivity(intent);
                            finish();
                        }

                    }else{
                        ResponseBody errorBody = response.errorBody();
                        try {
                            //Nhận thông báo lỗi trong quá trình đăng nhập
                            //Và hiện thị thông báo lên cho người dùng
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
                public void onFailure(Call<AppMessage> call, Throwable t) {
                    dialog.dismiss();
                    Toast.makeText(LoginActivity.this, "Login Failed!", Toast.LENGTH_SHORT).show();
                }
            });
    }

    private boolean validate(){
        String username = ip_username.getEditText().getText().toString().trim();
        String password = ip_password.getEditText().getText().toString().trim();
        if(TextUtils.isEmpty(username)){
            ip_username.setError("Please input your email!");
            return false;
        }else if(!Patterns.EMAIL_ADDRESS.matcher(username).matches()){
            ip_username.setError("Invalid email address!");
            return false;
        }
        if(TextUtils.isEmpty(password)){
            ip_password.setError("Please input your password!");
            return false;
        }
        ip_password.setError(null);
        ip_username.setError(null);

        return true;
    }
}