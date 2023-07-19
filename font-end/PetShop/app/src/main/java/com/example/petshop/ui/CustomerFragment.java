package com.example.petshop.ui;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.example.petshop.models.AppMessage;
import com.example.petshop.models.User;
import com.example.petshop.untils.ApiService;
import com.example.petshop.untils.RetroClient;
import com.example.petshop.viewmodel.UserViewModel;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class CustomerFragment extends Fragment {
    private UserViewModel userViewModel;
    private TextView tv_show;
    private RecyclerView recyclerView;
    private UserAdapter userAdapter;
    public CustomerFragment(){}

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_customer,container,false);
        userViewModel = new ViewModelProvider(requireActivity()).get(UserViewModel.class);
        userAdapter = new UserAdapter(new ArrayList<>());

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

    private void showBottomDialog(){
        
    }
}