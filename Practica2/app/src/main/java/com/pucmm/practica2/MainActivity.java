package com.pucmm.practica2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Switch;


import static android.Manifest.permission.ACCESS_FINE_LOCATION;

import java.util.concurrent.atomic.AtomicBoolean;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private final String[] perms = {"android.permission.FINE_LOCATION"};
    private final int permsRequestCode = 200;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btnCancel = findViewById(R.id.btnCancel);
        Button btnContinue = findViewById(R.id.btnContinue);
        Switch swtLocation = findViewById(R.id.swtLocation);

        btnContinue.setOnClickListener(v->{
            if(swtLocation.isChecked()){

                ActivityCompat.requestPermissions(this,new String[]{ACCESS_FINE_LOCATION},permsRequestCode);
            }

        });



    }

    //Eventos de click
    @Override
    public void onClick(View v){
        switch (v.getId()){

            case R.id.btnCancel:

                finish();
                break;


        }
     }


     //Resultados de haber pedido los permisos
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch(requestCode)
        {
            case 200:

                boolean locationAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                break;
        }
    }
}