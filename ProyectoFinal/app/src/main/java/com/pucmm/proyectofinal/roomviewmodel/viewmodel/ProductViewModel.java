package com.pucmm.proyectofinal.roomviewmodel.viewmodel;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.pucmm.proyectofinal.roomviewmodel.database.AppDatabase;
import com.pucmm.proyectofinal.roomviewmodel.model.Category;
import com.pucmm.proyectofinal.roomviewmodel.model.Product;
import com.pucmm.proyectofinal.roomviewmodel.model.ProductWithCarousel;

import java.util.List;

public class ProductViewModel extends ViewModel {

    private LiveData<List<ProductWithCarousel>> productListLiveData;

    public ProductViewModel(@NonNull AppDatabase appDatabase, Category category, String searchQuery){

        if(category != null){
            productListLiveData = appDatabase.productDao().findProductsByCategory(category.getName());

        }
        else if(searchQuery != null)
        {
            productListLiveData = appDatabase.productDao().findProductsByDescription(searchQuery);
        }
        else
        {
            productListLiveData = appDatabase.productDao().findAll();
        }


    }

    public LiveData<List<ProductWithCarousel>> getProductListLiveData() {
        return productListLiveData;
    }
}
