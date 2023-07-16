package com.example.petshop;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Menu;

import com.example.petshop.models.User;
import com.example.petshop.ui.CustomerFragment;
import com.example.petshop.ui.HomeFragment;
import com.example.petshop.ui.PasswordFragment;
import com.example.petshop.ui.ProductFragment;
import com.example.petshop.ui.UserFragment;
import com.example.petshop.viewmodel.AppViewModel;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;

import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;

import com.example.petshop.databinding.ActivityMainBinding;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    AppViewModel userViewModel;
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
        Intent intent = getIntent();
        userViewModel = new ViewModelProvider(this).get(AppViewModel.class);
        if(intent !=null) {
            Bundle bundle = intent.getExtras();
            User user =(User) bundle.getSerializable("User");
            if(user!=null){
                userViewModel.setData(user);
            }
        }

        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_product, R.id.nav_customer, R.id.nav_password, R.id.nav_user,R.id.nav_logout,R.id.nav_exit
        )
                .setOpenableLayout(drawer)
                .build();

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);

        //logout account or exit app
        navigationView.setNavigationItemSelectedListener(item -> {
            if (item.getItemId() == R.id.nav_logout) {
                startActivity(new Intent(MainActivity.this, LoginActivity.class));
                finish();
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
            boolean isProductFragment = destination.getId() == R.id.nav_product;
            boolean isCustomerFragment = destination.getId() == R.id.nav_customer;
            // Hiển thị hoặc ẩn FAB tùy theo kết quả kiểm tra
            binding.appBarMain.fab.setVisibility(isProductFragment || isCustomerFragment ? View.VISIBLE : View.GONE);
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
}