package com.pucmm.primerparcial;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.res.Configuration;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        int columnas;
        int orientacion = getResources().getConfiguration().orientation;

        if(orientacion == Configuration.ORIENTATION_PORTRAIT){
            setContentView(R.layout.activity_main); //cambiando la vista
            columnas = 2;
            getSupportFragmentManager().beginTransaction()
                    .setReorderingAllowed(true)
                    .replace(R.id.fragment_parent, ItemFragment.newInstance(columnas))
                    .commit();

        }else
        {
            setContentView(R.layout.activity_main_dual);
            columnas = 1;
            getSupportFragmentManager().beginTransaction()
                    .setReorderingAllowed(true)
                    .replace(R.id.fragment_list, ItemFragment.newInstance(columnas))
                    .commit();
        }
        


    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        int columnas;
        if(newConfig.orientation == Configuration.ORIENTATION_PORTRAIT){
            setContentView(R.layout.activity_main); //cambiando la vista
            columnas = 2;
            getSupportFragmentManager().beginTransaction()
                    .setReorderingAllowed(true)
                    .replace(R.id.fragment_parent, ItemFragment.newInstance(columnas))
                    .commit();
        }else
        {
            setContentView(R.layout.activity_main_dual);
            columnas = 1;
            getSupportFragmentManager().beginTransaction()
                    .setReorderingAllowed(true)
                    .replace(R.id.fragment_list, ItemFragment.newInstance(columnas))
                    .commit();
        }



    }
}