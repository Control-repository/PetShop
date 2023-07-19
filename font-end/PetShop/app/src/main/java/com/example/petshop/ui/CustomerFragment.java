package com.example.petshop.ui;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.petshop.R;
import com.example.petshop.adapter.UserAdapter;
import com.example.petshop.interfaces.ItemClickListener;
import com.example.petshop.models.AppMessage;
import com.example.petshop.models.User;
import com.example.petshop.utils.ApiService;
import com.example.petshop.utils.RetroClient;
import com.example.petshop.viewmodel.UserViewModel;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class CustomerFragment extends Fragment {
    private UserViewModel userViewModel;
    private User thisUser;
    private TextView tv_show;
    private RecyclerView recyclerView;
    private UserAdapter userAdapter;
    public CustomerFragment(){}

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_customer,container,false);
        userViewModel = new ViewModelProvider(requireActivity()).get(UserViewModel.class);
        userAdapter = new UserAdapter(new ArrayList<>());
        userViewModel.getUserData().observe(getViewLifecycleOwner(),user->{
            if(user!=null){
                thisUser = user;
            }
        });
        loadListUser();

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        tv_show = view.findViewById(R.id.tv_show);
        recyclerView = view.findViewById(R.id.recycler_user);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        userViewModel.getListUserData().observe(getViewLifecycleOwner(),users -> {
            if(!users.isEmpty()){
                userAdapter.setList(users);
                recyclerView.setAdapter(userAdapter);
                tv_show.setVisibility(View.GONE);
            }else{
                tv_show.setVisibility(View.VISIBLE);
            }
        });

        userAdapter.setOnItemClickListener(new ItemClickListener() {
            @Override
            public void onItemUserClick(User item) {
                showBottomDialog(item);
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

    }

    private void loadListUser(){
        RetroClient.setContext(requireContext());
        //
        ApiService apiService = RetroClient.getApiService();
        //Kiểm tra và truyền vào call
        Call<AppMessage> call = apiService.getAllUser();
        //
        call.enqueue(new Callback<AppMessage>() {
            @Override
            public void onResponse(Call<AppMessage> call, Response<AppMessage> response) {
                if(response.isSuccessful()){
                    AppMessage appMessage = response.body();
                    Log.i("CHECK APP",appMessage.toString());
                    if(appMessage!=null){
                        userViewModel.setListUserData(appMessage.getListUser());
                    }
                }else{
                    Toast.makeText(requireContext(), "Failed to get User", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<AppMessage> call, Throwable t) {
                Toast.makeText(requireContext(), "Some thing wrong!", Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void showBottomDialog(User item){
        // Tạo instance của Dialog
        BottomSheetDialog dialog = new BottomSheetDialog(requireContext());
        // Thiết lập layout của Dialog
        View view = getLayoutInflater().inflate(R.layout.bottom_sheet_layout, null);
        dialog.setContentView(view);

        // Thiết lập animation vào và ra cho Dialog
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        //mapping nút nhấn
        LinearLayout layout_show = view.findViewById(R.id.layout_show);
        LinearLayout layout_edit = view.findViewById(R.id.layout_edit);
        LinearLayout layout_delete = view.findViewById(R.id.layout_delete);
        userViewModel.getUserData().observe(getViewLifecycleOwner(),user->{
            if(user.getRole()==1){
                layout_edit.setVisibility(View.GONE);
                layout_delete.setVisibility(View.GONE);
            }else{
                layout_edit.setVisibility(View.VISIBLE);
                layout_delete.setVisibility(View.VISIBLE);
            }
        });

        //click delete
        layout_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickDelete_object(item);
            }
        });
        // Hiển thị Dialog
        dialog.show();
    }

    public void clickDelete_object(User user){
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle("Delete user");
        builder.setMessage("Are you sure delete this user?");

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create().show();
    }
}