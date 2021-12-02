package com.pucmm.proyectofinal.roomviewmodel.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

import com.pucmm.proyectofinal.roomviewmodel.model.Carousel;
import com.pucmm.proyectofinal.roomviewmodel.model.Category;
import com.pucmm.proyectofinal.roomviewmodel.model.Product;
import com.pucmm.proyectofinal.roomviewmodel.model.ProductWithCarousel;

import java.util.List;
import java.util.UUID;

@Dao
public interface ProductDao {

    //LiveData permite siempre ver en la UI los cambios realizados en la data
  //  @Query("SELECT * FROM Products")
   // LiveData<List<Product>> findAll();

    @Transaction
    @Query("SELECT * FROM Products WHERE productId = :productID")
    Product findProductById(String productID);

    @Transaction
    @Query("SELECT * FROM products ORDER BY productId")
    LiveData<List<ProductWithCarousel>> findAll();

    @Transaction
    @Query("SELECT * FROM products ORDER BY productId")
    List<ProductWithCarousel> getProducts();

    @Transaction
    @Query("DELETE FROM carousel WHERE product = :uid")
    void deleteCarousels(String uid);

    @Transaction
    @Insert
    void insertCarousels(List<Carousel> carousels);

    @Transaction
    @Update
    void updateCarousels(List<Carousel> carousels);

    @Transaction
    @Insert
    void insert(Product product);

    @Transaction
    @Update
    void update(Product product);

    @Transaction
    @Delete
    void delete(Product product);

}
