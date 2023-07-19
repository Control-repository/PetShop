package com.example.petshop.ui;

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
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.example.petshop.R;
import com.example.petshop.adapter.ProductAdapter;
import com.example.petshop.models.AppMessage;
import com.example.petshop.models.Product;
import com.example.petshop.untils.ApiService;
import com.example.petshop.untils.RetroClient;
import com.example.petshop.viewmodel.ProductViewModel;
import com.example.petshop.viewmodel.UserViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ProductFragment extends Fragment {
    private ProductViewModel productViewModel;
    private RecyclerView recyclerProduct;
    private ProductAdapter productAdapter;
    private String searchQuery;
    private boolean isFirstLoad = true;
    private TextView tv_show;
    private FloatingActionButton fab;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view =inflater.inflate(R.layout.fragment_product,container,false);
        // Lắng nghe sự thay đổi của LiveData để nhận thông tin User
        productViewModel = new ViewModelProvider(requireActivity()).get(ProductViewModel.class);
        // Khởi tạo các view và adapter
        productAdapter = new ProductAdapter(new ArrayList<>());
        loadProductList();
        //mapping
        fab = getActivity().findViewById(R.id.app_bar_main).findViewById(R.id.fab);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerProduct = view.findViewById(R.id.recycler_product);
        tv_show = view.findViewById(R.id.tv_show);

        recyclerProduct.setLayoutManager(new GridLayoutManager(getContext(), 2));

        productViewModel.getListProduct().observe(getViewLifecycleOwner(), list -> {
            Log.i("CHECK LIST",list.toString());
            if(!list.isEmpty()){
            if(isLoadList()){
                productAdapter.setProductList(list);
                recyclerProduct.setAdapter(productAdapter);
                isFirstLoad = false;
            }
                tv_show.setVisibility(View.GONE);

            }else{
                tv_show.setVisibility(View.VISIBLE);
            }
        });

        fab.setOnClickListener(v->{
            NavController navController = NavHostFragment.findNavController(this);
            navController.navigate(R.id.action_product_to_itemProduct);
        });
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
                if(newText!=null || isLoadList()){
                    loadProductList();
                }
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


    private boolean isLoadList(){
        return (searchQuery !=null || isFirstLoad);
    }
    private void loadProductList(){
            RetroClient.setContext(requireContext());
            ApiService apiService = RetroClient.getApiService();
            Call<AppMessage> call =apiService.getAllProduct(searchQuery);
            call.enqueue(new Callback<AppMessage>() {
                @Override
                public void onResponse(Call<AppMessage> call, Response<AppMessage> response) {
                    if (response.isSuccessful()) {
                        AppMessage body = response.body();
                        List<Product> productList = body.getProductList();
                        productViewModel.setListProduct(productList);
                    } else {
                        Toast.makeText(requireContext(), "Failed to get product", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<AppMessage> call, Throwable t) {
                    Toast.makeText(requireContext(), "Something wrong!", Toast.LENGTH_SHORT).show();
                }
            });
    }
}