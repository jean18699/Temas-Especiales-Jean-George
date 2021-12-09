package com.pucmm.proyectofinal.roomviewmodel.model;


import java.io.Serializable;

//POJO para enviar al la ruta /users/login del API Usuarios que requiere solo email y password
public class UserLogin implements Serializable {

    private String email;
    private String password;

    public UserLogin(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
