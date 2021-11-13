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
public interface DatabaseDao {

    @Query("DELETE FROM Users")
    void eraseUsers();


}
