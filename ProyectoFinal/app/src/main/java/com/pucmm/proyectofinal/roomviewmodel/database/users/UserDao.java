package com.pucmm.proyectofinal.roomviewmodel.database.users;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.pucmm.proyectofinal.roomviewmodel.model.User;

import java.util.List;

@Dao
public interface UserDao {

    //CRUD BASICO

    @Query("SELECT * FROM users ORDER BY username")
    List<User> findAll();

    @Insert
    void insert(User user);

    @Update
    void update(User user);

    @Delete
    void delete(User user);

}
