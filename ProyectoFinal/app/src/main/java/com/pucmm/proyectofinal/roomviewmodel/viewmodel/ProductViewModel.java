package com.pucmm.proyectofinal.roomviewmodel.viewmodel;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.pucmm.proyectofinal.roomviewmodel.database.AppDatabase;
import com.pucmm.proyectofinal.roomviewmodel.model.Product;
import com.pucmm.proyectofinal.roomviewmodel.model.ProductWithCarousel;

import java.util.List;

public class ProductViewModel extends ViewModel {

    private LiveData<List<ProductWithCarousel>> productListLiveData;

    public ProductViewModel(@NonNull AppDatabase appDatabase){
        productListLiveData = appDatabase.productDao().findAll();
    }

    public LiveData<List<ProductWithCarousel>> getProductListLiveData() {
        return productListLiveData;
    }
}
