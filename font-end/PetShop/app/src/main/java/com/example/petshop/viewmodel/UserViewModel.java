package com.example.petshop.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.petshop.models.Product;
import com.example.petshop.models.User;

import java.util.List;

public class UserViewModel extends ViewModel {
    //get one user
    private final MutableLiveData<User> selectUser = new MutableLiveData<User>();
    public void setUserData(User user){
        selectUser.setValue(user);
    }
    public LiveData<User> getUserData(){
        return selectUser;
    }

    //get all user without user current
    private final MutableLiveData<List<User>> selectListUser = new MutableLiveData<>();
    public void setListUserData(List<User> user) {selectListUser.setValue(user);}
    public LiveData<List<User>> getListUserData(){return selectListUser;}
}
