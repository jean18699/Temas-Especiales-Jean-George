package com.pucmm.practica_sharedpreferences;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btnRegistrar = findViewById(R.id.btnRegistroValor);

        getSupportFragmentManager().beginTransaction()
                .setReorderingAllowed(true)
                .replace(R.id.fragment_parent, ItemFragment.newInstance(2))
                .commit();

        btnRegistrar.setOnClickListener(v ->{
            Intent intent = new Intent(this,RegisterValueActivity.class);
            startActivity(intent);
        });

    }
}