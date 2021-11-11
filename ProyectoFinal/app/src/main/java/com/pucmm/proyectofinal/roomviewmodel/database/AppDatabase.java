package com.pucmm.proyectofinal.roomviewmodel.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.pucmm.proyectofinal.roomviewmodel.model.User;

@Database(entities = {User.class},version = 1)
public abstract class AppDatabase extends RoomDatabase {

    private static final String DATABASE_NAME = "e-commerce";
    private static AppDatabase instance;
    private static final Object LOCK = new Object();

    //Creamos el acceso a la base de datos con un SINGLETON
    public static AppDatabase getInstance(Context context){
        if(instance == null){
            synchronized (LOCK) //Para asegurarnos que no haya nada fuera de control y que todo este sincronizado
            {
                instance = Room.databaseBuilder(context.getApplicationContext(), AppDatabase.class, DATABASE_NAME).build();
            }
        }
        return instance;
    }

    public abstract UserDao userDao();
    public abstract DatabaseDao databaseDao();

}
