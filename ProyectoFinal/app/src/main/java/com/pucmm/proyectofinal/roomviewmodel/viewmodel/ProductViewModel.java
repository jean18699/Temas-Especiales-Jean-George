package com.pucmm.proyectofinal.roomviewmodel.viewmodel;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.pucmm.proyectofinal.roomviewmodel.database.AppDatabase;
import com.pucmm.proyectofinal.roomviewmodel.model.Product;

import java.util.List;

public class ProductViewModel extends ViewModel {

    private LiveData<List<Product>> productListLiveData;

    public ProductViewModel(@NonNull AppDatabase appDatabase){
        productListLiveData = appDatabase.productDao().findAll();
    }

    public LiveData<List<Product>> getProductListLiveData() {
        return productListLiveData;
    }
}
