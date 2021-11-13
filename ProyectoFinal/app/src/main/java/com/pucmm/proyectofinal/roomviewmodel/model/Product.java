package com.pucmm.proyectofinal.roomviewmodel.model;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.UUID;


@Entity(tableName = "Products")
public class Product {

    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "productId")
    private String productId;

    @ColumnInfo(name = "description")
    private String description;

    @ColumnInfo(name = "price")
    private Float price;

    @ColumnInfo(name = "image", typeAffinity = ColumnInfo.BLOB)
    private byte[] image;




    public Product(String description, Float price, byte[] image) {
        this.productId = UUID.randomUUID().toString();
        this.description = description;
        this.price = price;
        this.image = image;
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

    public Float getPrice() {
        return price;
    }

    public void setPrice(Float price) {
        this.price = price;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }





}
