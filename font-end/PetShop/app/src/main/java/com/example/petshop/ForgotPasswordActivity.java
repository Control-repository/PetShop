package com.example.petshop;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import com.example.petshop.models.AppMessage;
import com.example.petshop.models.ForgotPasswordRequest;
import com.example.petshop.untils.ApiService;
import com.example.petshop.untils.CheckInput;
import com.example.petshop.untils.RetroClient;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ForgotPasswordActivity extends AppCompatActivity {
    private Button btn_send;
    private TextInputLayout ip_email;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        //mapping
        btn_send = findViewById(R.id.btn_send);
        ip_email = findViewById(R.id.ip_email);

        //
        btn_send.setOnClickListener(v->{
            String email = ip_email.getEditText().getText().toString();
            Log.i("CHECK button", "onclick");
            if(CheckInput.validateInput(email,ip_email)){
                //create dialog to get token
                AlertDialog.Builder builder = new AlertDialog.Builder(ForgotPasswordActivity.this);
                //truyền context
                RetroClient.setContext(getApplicationContext());
                //call to server
                ApiService apiService = RetroClient.getApiService();

                ForgotPasswordRequest request = new ForgotPasswordRequest(email);
                Log.i("CHECK button 1", "onclick");

                Call<AppMessage> call = apiService.forgotPassword(email);

                call.enqueue(new Callback<AppMessage>() {
                    @Override
                    public void onResponse(Call<AppMessage> call, Response<AppMessage> response) {
                        if(response.isSuccessful()){
                            AppMessage appMessage = response.body();
                            String token = appMessage.getToken();
                            String message = appMessage.getMessage();
                            Toast.makeText(ForgotPasswordActivity.this,message,Toast.LENGTH_SHORT).show();
                            builder.setMessage("Token to reset password: " + token);
                            builder.setTitle("Notification");
                            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                    startActivity(new Intent(ForgotPasswordActivity.this,ResetPassActivity.class));
                                    finish();
                                }
                            });
                            AlertDialog dialog = builder.create();
                            dialog.show();
                        }
                        if(response.errorBody()!=null){
                            Gson gson = new Gson();
                            try {
                                AppMessage appMessage =gson.fromJson(response.errorBody().toString(),AppMessage.class);
                                Toast.makeText(getApplicationContext(), appMessage.getMessage(), Toast.LENGTH_SHORT).show();
                            }catch (Exception ex){
                                ex.printStackTrace();
                            }
                         }
                    }

                    @Override
                    public void onFailure(Call<AppMessage> call, Throwable t) {
                        Log.i("ERROR",t.toString());
                    }
                });
            }
        });
    }
}