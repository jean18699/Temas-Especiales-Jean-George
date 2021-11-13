package com.pucmm.proyectofinal.roomviewmodel.model;

import androidx.room.Embedded;
import androidx.room.Junction;
import androidx.room.Relation;

import java.util.List;

//Clase para retornar todos las categorias dado un producto especificado
public class CategoriesByProduct {

    @Embedded
    public Product product;

    @Relation(
            parentColumn = "productId",
            entityColumn = "categoryId",
            associateBy = @Junction(ProductCategoryCrossRef.class)
    )
    public List<Category> categories;
}
