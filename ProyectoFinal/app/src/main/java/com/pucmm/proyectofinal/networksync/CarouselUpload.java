package com.pucmm.proyectofinal.networksync;

import android.net.Uri;

import com.pucmm.proyectofinal.roomviewmodel.model.Carousel;

public class CarouselUpload {
    public Uri uri;
    public Carousel carousel;

    public CarouselUpload(Uri uri, Carousel carousel) {
        this.uri = uri;
        this.carousel = carousel;
    }
}
