package com.pucmm.practica_sharedpreferences;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;

import com.pucmm.practica_sharedpreferences.placeholder.PlaceholderContent;

import java.util.Map;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btnRegistrar = findViewById(R.id.btnRegistroValor);

        //Cargando los datos de la lista guardados localmente
        if (PlaceholderContent.ITEMS.isEmpty()) {
            cargarDatos();
        }



        getSupportFragmentManager().beginTransaction()
                .setReorderingAllowed(true)
                .replace(R.id.fragment_parent, ItemFragment.newInstance(2))
                .commit();

        btnRegistrar.setOnClickListener(v -> {
            Intent intent = new Intent(this, RegisterValueActivity.class);
            startActivity(intent);
        });

    }

    public void cargarDatos() {
        //Accediendo a los datos guardados localmente
        SharedPreferences sharedPreferences = getSharedPreferences("valores", Context.MODE_PRIVATE);

        for (Map.Entry<String, ?> entry : sharedPreferences.getAll().entrySet()) {
            PlaceholderContent.PlaceholderItem item = new PlaceholderContent.PlaceholderItem(entry.getKey(), entry.getValue().toString());
            PlaceholderContent.ITEMS.add(item);
        }
    }
}