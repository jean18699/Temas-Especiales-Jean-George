package com.pucmm.practica1;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import java.util.ArrayList;

public class SummaryActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_summary);

        Bundle extras = getIntent().getExtras();
        TextView txtFraseNombre = findViewById(R.id.txtFraseNombre);
        TextView txtGeneroFecha = findViewById(R.id.txtGeneroFecha);
        TextView txtGustosLenguajes = findViewById(R.id.txtGustosLenguajes);


        if(extras != null){

            String gusto;
            ArrayList<String> listaLenguajes = getIntent().getStringArrayListExtra("lenguajes");
            String lenguajes;

            if(listaLenguajes != null)
            {
                if(!listaLenguajes.isEmpty())
                {
                    lenguajes = " Mis lenguajes favoritos son: ";
                    for(String lenguaje : extras.getStringArrayList("lenguajes"))
                    {
                        lenguajes += lenguaje + ", ";
                    }
                    lenguajes += ".";

                }else
                {
                    lenguajes = " No tengo lenguajes favoritos";
                }
            }else
            {
                lenguajes = " No tengo lenguajes favoritos";
            }


            txtFraseNombre.setText("Hola!, Mi nombre es: "+extras.getString("nombre")+" "+extras.getString("apellido"));
            txtGeneroFecha.setText("Soy del genero " + extras.getString("genero")+", y naci en la fecha de "+extras.getString("fecha"));


            if(extras.getString("gustar").equalsIgnoreCase("Si"))
            {
                gusto = "Me gusta programar.";

            }else
            {
                gusto = "No me gusta programar.";
            }


            txtGustosLenguajes.setText(gusto + lenguajes);

        }


    }

}