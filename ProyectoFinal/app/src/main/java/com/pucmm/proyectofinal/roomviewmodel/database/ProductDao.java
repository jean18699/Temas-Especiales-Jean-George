package com.pucmm.proyectofinal.roomviewmodel.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.pucmm.proyectofinal.roomviewmodel.model.Category;
import com.pucmm.proyectofinal.roomviewmodel.model.Product;

import java.util.List;
import java.util.UUID;

@Dao
public interface ProductDao {

    //LiveData permite siempre ver en la UI los cambios realizados en la data
    @Query("SELECT * FROM Products")
    LiveData<List<Product>> findAll();

    @Query("SELECT * FROM Products WHERE id = :productID")
    Product findProductById(String productID);

    @Insert
    void insert(Product product);

    @Update
    void update(Product product);

    @Delete
    void delete(Product product);

}
