package com.pucmm.proyectofinal.roomviewmodel.database.users;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.pucmm.proyectofinal.roomviewmodel.model.User;

@Database(entities = {User.class},version = 1)
public abstract class UserDatabase extends RoomDatabase {

    private static final String DATABASE_NAME = "Users";
    private static UserDatabase instance;
    private static final Object LOCK = new Object();

    //Creamos el acceso a la base de datos con un SINGLETON
    public static UserDatabase getInstance(Context context){
        if(instance == null){
            synchronized (LOCK) //Para asegurarnos que no haya nada fuera de control y que todo este sincronizado
            {
                instance = Room.databaseBuilder(context.getApplicationContext(), UserDatabase.class, DATABASE_NAME).build();
            }
        }
        return instance;
    }

    public abstract UserDao userDao();

}
