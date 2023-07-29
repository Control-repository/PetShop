package com.example.petshop.ui;

import android.app.AlertDialog;
import android.content.DialogInterface;
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
import androidx.fragment.app.FragmentResultListener;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.example.petshop.R;
import com.example.petshop.adapter.ProductAdapter;
import com.example.petshop.dialog.ProductBottomDialog;
import com.example.petshop.dialog.ProductInforDialog;
import com.example.petshop.interfaces.ItemClickListener;
import com.example.petshop.models.AppMessage;
import com.example.petshop.models.Product;
import com.example.petshop.models.User;
import com.example.petshop.utils.ApiService;
import com.example.petshop.utils.RetroClient;
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
    private UserViewModel userViewModel;
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
        Log.i("ON CREATE", "CREATE");

    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        // Lắng nghe kết quả trả về từ ItemProductFragment thông qua Fragment Result
        getParentFragmentManager().setFragmentResultListener("REQUEST_ADD_PRODUCT", this, new FragmentResultListener() {
            @Override
            public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle result) {
                if (requestKey.equals("REQUEST_ADD_PRODUCT")) {
                    String message = result.getString("message", "");
                    Log.i("ON CALL", "CALL ITEMFRAGMENT");
                    if (!message.isEmpty()) {
                        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show();
                        // Load lại danh sách sản phẩm sau khi ItemProductFragment hoàn thành tạo sản phẩm
                        loadProductList();
                    }
                }
            }
        });

        Log.i("ONCREATEVIEW", "CREATE VIEW");
        View view =inflater.inflate(R.layout.fragment_product,container,false);
        // Lắng nghe sự thay đổi của LiveData để nhận thông tin User
        productViewModel = new ViewModelProvider(requireActivity()).get(ProductViewModel.class);
        userViewModel = new ViewModelProvider(requireActivity()).get(UserViewModel.class);
        // Khởi tạo các view và adapter
        productAdapter = new ProductAdapter(new ArrayList<>());
        //mapping
        fab = getActivity().findViewById(R.id.app_bar_main).findViewById(R.id.fab);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerProduct = view.findViewById(R.id.recycler_product);
        Log.i("ONVIEWCREATED", "VIEW CREATE");
        tv_show = view.findViewById(R.id.tv_show);
        recyclerProduct.setLayoutManager(new GridLayoutManager(getContext(), 2));

        productViewModel.getListProduct().observe(getViewLifecycleOwner(), list -> {
            Log.i("ON LOAD", String.valueOf(isLoadList()));
            if(!list.isEmpty()){

                Log.i("ON LOAD AFTER", String.valueOf(isLoadList()));
                productAdapter.setProductList(list);
                recyclerProduct.setAdapter(productAdapter);
                tv_show.setVisibility(View.GONE);

            }else{
                tv_show.setVisibility(View.VISIBLE);
            }
        });

        //Load danh sách lần đầu
        loadProductList();

        fab.setOnClickListener(v->{
            NavController navController = NavHostFragment.findNavController(this);
            navController.navigate(R.id.action_to_itemProduct);
        });

        //click item product
        userViewModel.getUserData().observe(getViewLifecycleOwner(),user ->{
            if(user!=null){
                productAdapter.setOnItemClickListener(new ItemClickListener() {
                    @Override
                    public void OnItemClick(View view, int position) {
                        ProductBottomDialog dialog = new ProductBottomDialog(requireContext(),user,productAdapter.getItem(position));
                        dialog.setOnDeleteClickListener(new ProductBottomDialog.OnDeleteClickListener() {
                            @Override
                            public void OnItemClick(Product product) {
                                clickDelete_object(product);

                            }
                        });
                        dialog.setOnEditClickListener(new ProductBottomDialog.OnEditClickListener() {
                            @Override
                            public void OnItemClick(Product product) {
                                Bundle bundle = new Bundle();
                                bundle.putSerializable("product",product);
                                NavController navController = NavHostFragment.findNavController(ProductFragment.this);
                                navController.navigate(R.id.action_to_itemProduct,bundle);
                            }
                        });
                        dialog.setOnShowClickListener(new ProductBottomDialog.OnShowClickListener() {
                            @Override
                            public void OnItemClick(Product product) {
                                ProductInforDialog productInforDialog = new ProductInforDialog(requireContext(),product,NavHostFragment.findNavController(ProductFragment.this));
                                productInforDialog.show();
                            }
                        });
                        dialog.show();

                    }
                });

            }
        });

    }

    //Delete Product
    public void clickDelete_object(Product product){
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle("Delete user");
        builder.setMessage("Are you sure delete this user?");
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                RetroClient.setContext(requireContext());
                ApiService apiService = RetroClient.getApiService();
                Call<AppMessage> call = apiService.deleteProduct(product.getId());
                call.enqueue(new Callback<AppMessage>() {
                    @Override
                    public void onResponse(Call<AppMessage> call, Response<AppMessage> response) {
                        if(response.isSuccessful()){
                            Toast.makeText(requireContext(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                            reloadCurrentFragment();
                        }
                    }
                    @Override
                    public void onFailure(Call<AppMessage> call, Throwable t) {
                        Log.i("ERROR DELETE", t.getMessage());
                        Toast.makeText(requireContext(), "Some thing wrong!", Toast.LENGTH_SHORT).show();
                    }
                });
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

                    loadProductList();

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

    private void reloadCurrentFragment() {
        NavHostFragment.findNavController(ProductFragment.this).navigate(R.id.nav_product);
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
                        Log.i("LIST PRODUCT", productList.toString());

                        isFirstLoad = false;
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

    @Override
    public void onResume() {
        super.onResume();
        Log.i("ONRESUME PRODUCT","RESUME");
//        if(isLoadList()){
//            loadProductList();
//            isFirstLoad = false;
//        }

    }

    @Override
    public void onStop() {
        super.onStop();
        Log.i("ONSTOP PRODUCT","STOP");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i("ONDESTROY PRODUCT","DESTROY");

    }
}