package com.pucmm.proyectofinal.roomviewmodel.model;

import androidx.room.Embedded;
import androidx.room.Relation;

import java.io.Serializable;

public class CategoryAndProduct implements Serializable {
    @Embedded
    public Category category;
    @Relation(
            parentColumn = "uid",
            entityColumn = "category"
    )
    public Product product;
}
