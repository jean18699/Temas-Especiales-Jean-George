package com.pucmm.proyectofinal.roomviewmodel.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;


@Entity(tableName = "Users")
public class User {

    //@PrimaryKey(autoGenerate = true)
    //@ColumnInfo(name = "id")
   // private int id;
    @PrimaryKey
    @ColumnInfo(name = "username")
    private String username;
    @ColumnInfo(name = "password")
    private String password;

    @ColumnInfo(name = "email")
    private String email;

    @ColumnInfo(name = "name")
    private String name;

    @ColumnInfo(name = "lastname")
    private String lastname;

    public User(String username, String password, String email, String name, String lastname) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.name = name;
        this.lastname = lastname;
    }
}
