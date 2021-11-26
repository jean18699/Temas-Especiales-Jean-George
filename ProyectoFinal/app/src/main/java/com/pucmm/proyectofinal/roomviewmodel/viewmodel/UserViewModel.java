package com.pucmm.proyectofinal.roomviewmodel.viewmodel;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.pucmm.proyectofinal.roomviewmodel.database.AppDatabase;
import com.pucmm.proyectofinal.roomviewmodel.model.User;

import java.util.List;

public class UserViewModel extends ViewModel {

    private LiveData<List<User>> userListLiveData;

    public UserViewModel(@NonNull AppDatabase appDatabase){
       // userListLiveData = appDatabase.userDao().findAll();
    }

    public LiveData<List<User>> getUserListLiveData() {
        return userListLiveData;
    }
}
