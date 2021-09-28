package com.pucmm.practica2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Switch;


import static android.Manifest.permission.ACCESS_FINE_LOCATION;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class MainActivity extends AppCompatActivity {


    private final int permsRequestCode = 200;
    Button btnCancel;
    Button btnContinue;
    Switch swtStorage;
    Switch swtLocation;
    Switch swtCamera;
    Switch swtPhone;
    Switch swtContacts;
    Intent access;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        access = new Intent(MainActivity.this, AccessActivity.class);
        List<String> perms = new ArrayList<>();
        btnContinue = findViewById(R.id.btnContinue);
        btnCancel = findViewById(R.id.btnCancel);
        swtStorage = findViewById(R.id.swtStorage);
        swtLocation = findViewById(R.id.swtLocation);
        swtCamera = findViewById(R.id.swtCamera);
        swtPhone = findViewById(R.id.swtPhone);
        swtContacts = findViewById(R.id.swtContacts);


        //Evento al presionar el boton de continuar. Se revisara cuales elementos fueron marcados para preguntar por permisos.
        btnContinue.setOnClickListener(v->{

            if(swtStorage.isChecked()){

                perms.add("android.permission.MANAGE_EXTERNAL_STORAGE");

            }
            if(swtLocation.isChecked()){
                perms.add("android.permission.ACCESS_FINE_LOCATION");

            }
            if(swtCamera.isChecked()){
                perms.add("android.permission.CAMERA");
            }
            if(swtPhone.isChecked())
            {
                perms.add("android.permission.CALL_PHONE");
            }

            if(swtContacts.isChecked())
            {
                perms.add("android.permission.READ_CONTACTS");
            }

            ActivityCompat.requestPermissions(this,perms.toArray(new String[0]),permsRequestCode);
         
        });

        //Cerrar la actividad al darle al boton Cancel.
        btnCancel.setOnClickListener(v->{
            finish();
        });


    }


     //Resultados de haber pedido los permisos
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        startActivity(access);

    }
}