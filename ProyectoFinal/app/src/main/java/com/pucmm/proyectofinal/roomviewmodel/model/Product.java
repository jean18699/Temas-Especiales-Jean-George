package com.pucmm.proyectofinal.roomviewmodel.model;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;
import java.net.URI;
import java.util.UUID;


@Entity(tableName = "Products")
public class Product implements Serializable {

    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "productId")
    private String productId;

    @ColumnInfo(name = "description")
    private String description;

    @ColumnInfo(name = "category")
    private String category;

    @ColumnInfo(name = "price")
    private Double price;

    @ColumnInfo(name = "active")
    private boolean active;

    public Product(){

    }

    public Product(String description, Double price, String category) {
        this.productId = UUID.randomUUID().toString();
        this.description = description;
        this.category = category;
        this.price = price;
        this.active = true;
    }



    @NonNull
    public String getProductId() {
        return productId;
    }

    public void setProductId(@NonNull String productId) {
        this.productId = productId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}
