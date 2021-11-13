package com.pucmm.proyectofinal.roomviewmodel.model;

import androidx.room.Embedded;
import androidx.room.Entity;
import androidx.room.Junction;
import androidx.room.Relation;

import java.util.UUID;

//Tabla many to many para relacionar los productos y las categorias
@Entity(primaryKeys = {"productId","categoryName"})
public class ProductCategoryCrossRef {

    public UUID productId;
    public String categoryName;

}
