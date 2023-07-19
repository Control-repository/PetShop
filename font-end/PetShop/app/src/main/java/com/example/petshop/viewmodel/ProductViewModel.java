package com.example.petshop.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.petshop.models.Product;

import java.util.List;

public class ProductViewModel extends ViewModel {
    private final MutableLiveData<List<Product>> selectProducts = new MutableLiveData<>();
    public void setListProduct(List<Product> list){
        selectProducts.setValue(list);
    }
    public LiveData<List<Product>> getListProduct(){
        return selectProducts;
    }
}
