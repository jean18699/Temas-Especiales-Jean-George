package com.pucmm.proyectofinal.roomviewmodel.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.pucmm.proyectofinal.roomviewmodel.model.User;

import java.util.List;

@Dao
public interface UserDao {

    //LiveData permite siempre ver en la UI los cambios realizados en la data
   // @Query("SELECT * FROM users ORDER BY email")
    //LiveData<List<User>> findAll();

    //@Query("SELECT * FROM Users WHERE email = :userEmail")
    //User findUserByEmail(String userEmail);

   // @Query("SELECT * FROM Users WHERE username = :userID AND password = :userPassword")
    //User findUserByCredentials(String userID, String userPassword);
/*
    @Insert
    void insert(User user);

    @Update
    void update(User user);

    @Delete
    void delete(User user);
*/
}
