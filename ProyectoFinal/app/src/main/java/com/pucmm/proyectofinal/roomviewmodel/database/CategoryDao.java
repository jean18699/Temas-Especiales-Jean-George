package com.pucmm.proyectofinal.roomviewmodel.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.pucmm.proyectofinal.roomviewmodel.model.Category;
import com.pucmm.proyectofinal.roomviewmodel.model.User;

import java.util.List;

@Dao
public interface CategoryDao {

    //LiveData permite siempre ver en la UI los cambios realizados en la data
    @Query("SELECT * FROM users ORDER BY username")
    LiveData<List<Category>> findAll();

    @Query("SELECT * FROM Categories WHERE name = :categoryName")
    Category findCategoryByName(String categoryName);

    @Insert
    void insert(Category category);

    @Update
    void update(Category category);

    @Delete
    void delete(Category category);

}
