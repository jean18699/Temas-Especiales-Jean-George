package com.pucmm.proyectofinal.roomviewmodel.services;

import com.pucmm.proyectofinal.roomviewmodel.model.User;
import com.pucmm.proyectofinal.roomviewmodel.model.UserLogin;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;

public interface UserApiService {
    
    @GET("users")
    Call<List<User>> getAll();

    @GET("users/{userId}")
    Call<User> getUser();

    @PUT("users")
    Call<Void> update(@Body User user);

    @POST("users")
    Call<User> create(@Body User user);

    @PUT("users/change")
    Call<Void> changePassword(@Body User user);

    @POST("users/login")
    Call<User> login(@Body UserLogin user);




}
