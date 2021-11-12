package com.pucmm.proyectofinal.roomviewmodel.viewmodel;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.pucmm.proyectofinal.roomviewmodel.database.AppDatabase;
import com.pucmm.proyectofinal.roomviewmodel.model.Category;
import com.pucmm.proyectofinal.roomviewmodel.model.User;

import java.util.List;

public class CategoryViewModel extends ViewModel {

    private LiveData<List<Category>> categoryListLiveData;

    public CategoryViewModel(@NonNull AppDatabase appDatabase){
        categoryListLiveData = appDatabase.categoryDao().findAll();
    }

    public LiveData<List<Category>> getCategoryListLiveData() {
        return categoryListLiveData;
    }
}
