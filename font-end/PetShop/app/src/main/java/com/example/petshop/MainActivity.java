package com.example.petshop;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.Menu;
import android.widget.TextView;
import android.widget.Toast;

import com.example.petshop.models.AppMessage;
import com.example.petshop.models.User;
import com.example.petshop.utils.ApiService;
import com.example.petshop.utils.RetroClient;
import com.example.petshop.viewmodel.UserViewModel;
import com.google.android.material.navigation.NavigationView;

import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;

import com.example.petshop.databinding.ActivityMainBinding;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    UserViewModel userViewModel;
    TextView fullname,username;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActivityMainBinding binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.appBarMain.toolbar);

        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        //get User when login successfully
        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);

        //send user information to header navigation
        View headerView = navigationView.getHeaderView(0);
        fullname = headerView.findViewById(R.id.user_fullname);
        username = headerView.findViewById(R.id.user_username);

        Intent intent =getIntent();
        if(intent!=null){
            Bundle bundle = intent.getExtras();
            User user =(User) bundle.getSerializable("User");
            userViewModel.setUserData(user);
            fullname.setText(user.getFullName());
            username.setText(user.getUsername());
        }




        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_product, R.id.nav_customer, R.id.nav_password, R.id.nav_user,
                R.id.nav_logout,R.id.nav_exit,R.id.nav_item_product

        )
                .setOpenableLayout(drawer)
                .build();

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);

        //logout account or exit app
        navigationView.setNavigationItemSelectedListener(item -> {
            if (item.getItemId() == R.id.nav_logout) {

                return true;
            }else if (item.getItemId() == R.id.nav_exit) {
                finishAffinity();
                return true;
            }else{
                boolean handled = NavigationUI.onNavDestinationSelected(item, navController);
                if (handled) {
                    drawer.closeDrawers(); // Đóng DrawerLayout sau khi điều hướng
                }
                return handled;
            }
        });

        navController.addOnDestinationChangedListener((controller, destination, arguments) -> {
            // Kiểm tra xem Fragment hiện tại có phải là ProductFragment hoặc CustomerFragment hay không
            boolean isFragment = destination.getId() == R.id.nav_product || destination.getId() == R.id.nav_customer;

            if(isFragment && binding.appBarMain.fab.getVisibility() == View.GONE){
                binding.appBarMain.fab.show();
            }else if(!isFragment && binding.appBarMain.fab.getVisibility() == View.VISIBLE){
                binding.appBarMain.fab.hide();
            }
        });

        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    private void logOut(){
        RetroClient.setContext(getApplicationContext());
        ApiService apiService = RetroClient.getApiService();
        Call<AppMessage> call = apiService.getLogout();
        call.enqueue(new Callback<AppMessage>() {
            @Override
            public void onResponse(Call<AppMessage> call, Response<AppMessage> response) {
                if(response.isSuccessful()){

                    Toast.makeText(MainActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    SharedPreferences sharedPreferences = getSharedPreferences("Request", Context.MODE_PRIVATE);
                    sharedPreferences.edit().remove("token").apply();
                    startActivity(new Intent(MainActivity.this, LoginActivity.class));
                    finish();
                }
            }

            @Override
            public void onFailure(Call<AppMessage> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Something wrong!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}