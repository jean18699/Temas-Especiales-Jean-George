package com.pucmm.proyectofinal.roomviewmodel.model;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Embedded;
import androidx.room.Entity;
import androidx.room.Junction;
import androidx.room.PrimaryKey;
import androidx.room.Relation;

import java.util.UUID;

//Tabla many to many para relacionar los productos y las categorias
@Entity(primaryKeys = {"id","name"})
public class ProductCategoryCrossRef {

    @NonNull
    public String id;
    @NonNull
    public String name;

}
