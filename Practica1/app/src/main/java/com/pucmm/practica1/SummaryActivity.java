package com.pucmm.practica1;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

public class SummaryActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_summary);

        Bundle extras = getIntent().getExtras();
        TextView txtFraseNombre = findViewById(R.id.txtFraseNombre);


        if(extras != null){
            txtFraseNombre.setText("Hola!, Mi nombre es: "+extras.getString("nombre"));

        }


    }
}