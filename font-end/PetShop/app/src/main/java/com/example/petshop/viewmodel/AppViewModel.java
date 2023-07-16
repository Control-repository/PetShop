package com.example.petshop.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.petshop.models.Product;
import com.example.petshop.models.User;

import java.util.List;

public class AppViewModel extends ViewModel {

    private final MutableLiveData<User> selectUser = new MutableLiveData<User>();

    public void setData(User user){
        selectUser.setValue(user);
    }

    public LiveData<User> getData(){
        return selectUser;
    }

    private final MutableLiveData<List<Product>> selectProducts = new MutableLiveData<>();
    public void setListProduct(List<Product> list){
        selectProducts.setValue(list);
    }
    public LiveData<List<Product>> getListProduct(){
        return selectProducts;
    }
}
