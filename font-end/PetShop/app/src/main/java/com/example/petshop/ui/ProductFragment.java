package com.example.petshop.ui;

import android.annotation.SuppressLint;
import android.app.SearchManager;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.example.petshop.R;
import com.example.petshop.adapter.ProductAdapter;
import com.example.petshop.models.AppMessage;
import com.example.petshop.models.Product;
import com.example.petshop.models.User;
import com.example.petshop.untils.ApiService;
import com.example.petshop.untils.RetroClient;
import com.example.petshop.viewmodel.UserViewModel;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ProductFragment extends Fragment {
    private User user;
    private UserViewModel userViewModel;
    public ProductFragment(){}
    private RecyclerView recycler_product;

    private String searchQuery;
    private ProductAdapter productAdapter;
    private Context context;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        context = requireContext();
        View view =inflater.inflate(R.layout.fragment_product,container,false);
        //

        //get value of user
        userViewModel = new ViewModelProvider(requireActivity()).get(UserViewModel.class);
        userViewModel.getData().observe(getActivity(),item -> {
            user = item;
            Log.i("CHECK USER FRAGMENT",user.toString());
        });
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recycler_product = view.findViewById(R.id.recycler_product);
        recycler_product.setLayoutManager(new GridLayoutManager(getContext(),2));
        if(user!=null){
            RetroClient.setContext(requireContext());
            ApiService apiService = RetroClient.getApiService();
            Call<AppMessage> call =apiService.getAllProduct(searchQuery);
            call.enqueue(new Callback<AppMessage>() {
                @Override
                public void onResponse(Call<AppMessage> call, Response<AppMessage> response) {
                    Gson gson = new Gson();
                    if(response.isSuccessful()){
                        AppMessage body = response.body();
                        List<Product> listProduct = body.getProductList();

                        reloadAdapter(listProduct);

                        Toast.makeText(context, "Download List Product successfully!", Toast.LENGTH_SHORT).show();
                    }
                        ResponseBody errorBody = response.errorBody();
                        if(errorBody!=null){
                            AppMessage appMessage = gson.fromJson(errorBody.toString(),AppMessage.class);
                            Toast.makeText(context,appMessage.getMessage(),Toast.LENGTH_SHORT).show();
                        }
                }

                @Override
                public void onFailure(Call<AppMessage> call, Throwable t) {
                    Toast.makeText(context, "Something wrong!", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
        inflater.inflate(R.menu.main_search,menu);

        MenuItem item = menu.findItem(R.id.menu_search);
        SearchView searchView = (SearchView) item.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                searchQuery = newText;
                return false;
            }
        });
        // Set the text hint
        searchView.setQueryHint("Tên sản phẩm cần tìm");

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    private void reloadAdapter(List<Product> list){
        productAdapter = new ProductAdapter(list);
        recycler_product.setLayoutManager(new GridLayoutManager(context,2));
        recycler_product.setAdapter(productAdapter);

    }
}