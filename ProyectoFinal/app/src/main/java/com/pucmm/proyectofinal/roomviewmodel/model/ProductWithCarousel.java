package com.pucmm.proyectofinal.roomviewmodel.model;

import androidx.room.Embedded;
import androidx.room.Ignore;
import androidx.room.Relation;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ProductWithCarousel implements Serializable {

    @Embedded
    public Product product;
    @Relation(
            parentColumn = "productId",
            entityColumn = "product",
            entity = Carousel.class
    )
    public List<Carousel> carousels;

    public ProductWithCarousel() {
        this.product = new Product();
        this.carousels = new ArrayList<>();
    }

    @Ignore
    public ProductWithCarousel(Product product, List<Carousel> carousels) {
        this.product = product;
        this.carousels = carousels;
    }
}
