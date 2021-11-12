package com.pucmm.proyectofinal.roomviewmodel.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import com.pucmm.proyectofinal.R;
import com.pucmm.proyectofinal.databinding.ActivityLoginBinding;
import com.pucmm.proyectofinal.databinding.FragmentLoginBinding;
import com.pucmm.proyectofinal.roomviewmodel.database.AppDatabase;
import com.pucmm.proyectofinal.roomviewmodel.database.AppExecutors;
import com.pucmm.proyectofinal.roomviewmodel.fragments.LoginFragment;
import com.pucmm.proyectofinal.roomviewmodel.model.User;

public class LoginActivity extends AppCompatActivity {



    private @NonNull ActivityLoginBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater()); //binding permite utilizar los elementos del layout directamente
        setContentView(binding.getRoot()); //binding.getRoot hace referencia al xml del layout



        getSupportFragmentManager().beginTransaction()
                .setReorderingAllowed(true)
                .replace(binding.main.getId(), LoginFragment.newInstance())
                .commit();

       // AppDatabase.getInstance(getApplicationContext()).userDao().insert(new User("jean18699","a","b","c","d"));

       /* binding.btnLogin.setOnClickListener(v ->{

            checkLogin();

            Intent intent = new Intent(this, UserListActivity.class);
            startActivity(intent);
        });
*/
    }



}