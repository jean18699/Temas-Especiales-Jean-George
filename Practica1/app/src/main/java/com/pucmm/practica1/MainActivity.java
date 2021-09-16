package com.pucmm.practica1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Puente entre actividades
        Intent summary = new Intent(MainActivity.this,SummaryActivity.class);



        Button btnEnviar = findViewById(R.id.btnEnviar);
        EditText editNombre = findViewById(R.id.editNombre);
        EditText editApellido = findViewById(R.id.editApellido);
        Spinner spnGenero =  findViewById(R.id.spnGenero);


        List<String> generos = new ArrayList<>();
        generos.add("Masculino");
        generos.add("Femenino");

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,android.R.layout.simple_spinner_dropdown_item,generos);
        spnGenero.setAdapter(adapter);


        btnEnviar.setOnClickListener(v -> {

            summary.putExtra("nombre",editNombre.getText().toString());
            summary.putExtra("apellido",editApellido.getText().toString());
            summary.putExtra("genero",spnGenero.getSelectedItem().toString());
            startActivity(summary);
        });



    }



}