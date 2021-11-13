package com.pucmm.proyectofinal.roomviewmodel.model;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Embedded;
import androidx.room.Entity;
import androidx.room.PrimaryKey;


@Entity(tableName = "Products")
public class Product {

    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "id")
    private String uid;

    @ColumnInfo(name = "description")
    private String description;

    @ColumnInfo(name = "price")
    private Float price;

    @ColumnInfo(name = "image", typeAffinity = ColumnInfo.BLOB)
    private byte[] image;




    public Product(@NonNull String uid, String description, Float price, byte[] image, Category category) {
        this.uid = uid;
        this.description = description;
        this.price = price;
        this.image = image;
    }

    @NonNull
    public String getUid() {
        return uid;
    }

    public void setUid(@NonNull String uid) {
        this.uid = uid;
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
