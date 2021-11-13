package com.pucmm.proyectofinal.roomviewmodel.model;

import androidx.room.Embedded;
import androidx.room.Junction;
import androidx.room.Relation;

import java.util.List;

//Clase para retornar todos los productos dada una categoria especificada
public class ProductsByCategory {

    @Embedded
    public Category category;

    @Relation(
            parentColumn = "categoryId",
            entityColumn = "productId",
            associateBy = @Junction(ProductCategoryCrossRef.class)
    )
    public List<Product> products;
}
