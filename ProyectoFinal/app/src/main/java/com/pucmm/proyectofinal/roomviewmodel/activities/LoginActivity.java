package com.pucmm.proyectofinal.roomviewmodel.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import com.pucmm.proyectofinal.R;
import com.pucmm.proyectofinal.databinding.ActivityLoginBinding;
import com.pucmm.proyectofinal.roomviewmodel.database.AppDatabase;
import com.pucmm.proyectofinal.roomviewmodel.database.AppExecutors;
import com.pucmm.proyectofinal.roomviewmodel.model.User;

public class LoginActivity extends AppCompatActivity {

    private ActivityLoginBinding binding;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater()); //binding permite utilizar los elementos del layout directamente
        setContentView(binding.getRoot()); //binding.getRoot hace referencia al xml del layout

       // AppDatabase.getInstance(getApplicationContext()).userDao().insert(new User("jean18699","a","b","c","d"));

        binding.btnLogin.setOnClickListener(v ->{

            checkLogin();

           // AppDatabase.getInstance(getApplicationContext()).userDao().insert(new User("jean18699","a","b","c","d")); //guardar en la base de datos
            Intent intent = new Intent(this, UserListActivity.class);
            startActivity(intent);
        });

    }

    public void checkLogin(){
        //App executors nos ayuda a ejecutar consultas de manera segura
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                AppDatabase.getInstance(getApplicationContext()).userDao().insert(new User("emiliox","a","b","c","d"));
            }
        });
    }

}