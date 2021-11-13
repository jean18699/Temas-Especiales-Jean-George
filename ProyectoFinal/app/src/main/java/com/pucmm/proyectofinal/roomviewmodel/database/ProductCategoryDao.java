package com.pucmm.proyectofinal.roomviewmodel.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

import com.pucmm.proyectofinal.roomviewmodel.model.CategoriesByProduct;
import com.pucmm.proyectofinal.roomviewmodel.model.Product;
import com.pucmm.proyectofinal.roomviewmodel.model.ProductsByCategory;

import java.util.List;
import java.util.UUID;

@Dao
public interface ProductCategoryDao {

    @Transaction
    @Query("SELECT * FROM products WHERE id = :productID")
    LiveData<List<CategoriesByProduct>> findCategoriesByProduct(String productID);

    @Transaction
    @Query("SELECT * FROM categories WHERE name = :categoryName")
    LiveData<List<ProductsByCategory>> findProductsByCategory(String categoryName);

}
