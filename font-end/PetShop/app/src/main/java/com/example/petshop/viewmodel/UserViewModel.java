package com.example.petshop.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.petshop.models.User;

public class UserViewModel extends ViewModel {

    private final MutableLiveData<User> selectUser = new MutableLiveData<User>();

    public void setData(User user){
        selectUser.setValue(user);
    }

    public LiveData<User> getData(){
        return selectUser;
    }
}
