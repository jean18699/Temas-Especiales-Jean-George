package com.pucmm.proyectofinal.roomviewmodel.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.pucmm.proyectofinal.roomviewmodel.model.Category;

import java.util.List;

@Dao
public interface CategoryDao {

    //LiveData permite siempre ver en la UI los cambios realizados en la data
    @Query("SELECT * FROM categories ORDER BY name")
    LiveData<List<Category>> findAll();

    @Query("SELECT * FROM categories ORDER BY name")
    List<Category> findCategories();

    @Query("SELECT * FROM categories WHERE categoryId = :id")
    Category findCategoryById(int id);

    @Query("SELECT * FROM Categories WHERE name = :categoryName")
    Category findCategoryByName(String categoryName);



    @Insert
    long insert(Category category);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void update(Category category);

    @Delete
    void delete(Category category);

}
