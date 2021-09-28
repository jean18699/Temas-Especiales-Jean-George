package com.pucmm.practica2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.Settings;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

public class AccessActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_access);

        ConstraintLayout layout = findViewById(R.id.access_layout);
        Button btnStorage = findViewById(R.id.btnStorage);
        Button btnLocation = findViewById(R.id.btnStorage);
        Button btnCamera = findViewById(R.id.btnCamera);
        Button btnPhone = findViewById(R.id.btnPhone);
        Button btnContacts = findViewById(R.id.btnContacts);
        Toast toastPermissionDenied = Toast.makeText(getApplicationContext(),"Permission required", Toast.LENGTH_SHORT);


        //Al presionar el boton de Storage...
        btnStorage.setOnClickListener(v-> {

            //Revisando si hay permiso
            if (checkPermision("android.permission.MANAGE_EXTERNAL_STORAGE")) {
                Snackbar snackStoragePermissionAccepted = Snackbar.make(layout, "Permission granted", Snackbar.LENGTH_SHORT)
                        .setAction("Open", v1 -> {

                            //Creando un intent para abrir los contactos
                            Intent storage = new Intent(Settings.ACTION_INTERNAL_STORAGE_SETTINGS);
                            startActivity(storage);
                        });
                snackStoragePermissionAccepted.show();

                //Si no hay permiso, mostramos el aviso toast
            } else {
                toastPermissionDenied.show();
            }
        });

        //Al presionar el boton de Location...
        btnLocation.setOnClickListener(v->{

            //Revisando si hay permiso
            if(checkPermision("android.permission.ACCESS_FINE_LOCATION"))
            {
                Snackbar snackLocationPermissionAccepted = Snackbar.make(layout,"Permission granted", Snackbar.LENGTH_SHORT)
                        .setAction("Open", v1 -> {

                            //Creando un intent para abrir google maps
                            Intent location = new Intent(android.content.Intent.ACTION_VIEW,
                                    Uri.parse("http://maps.google.com/maps"));
                            startActivity(location);
                        });
                snackLocationPermissionAccepted.show();

                //Si no hay permiso, mostramos el aviso toast
            }else
            {
                toastPermissionDenied.show();
            }


        });

        //Al presionar el boton de la camara...
        btnCamera.setOnClickListener(v->{

            //Revisando si hay permiso
            if(checkPermision("android.permission.CAMERA"))
            {
                Snackbar snackCameraPermissionAccepted = Snackbar.make(layout,"Permission granted", Snackbar.LENGTH_SHORT)
                        .setAction("Open", v1 -> {

                            //Creando un intent para abrir la camara
                            Intent camera = new Intent("android.media.action.IMAGE_CAPTURE");
                            startActivity(camera);
                        });
                snackCameraPermissionAccepted.show();

                //Si no hay permiso, mostramos el aviso toast
            }else
            {
                toastPermissionDenied.show();
            }


        });

        //Al presionar el boton de Phone...
        btnPhone.setOnClickListener(v->{

            //Revisando si hay permiso
            if(checkPermision("android.permission.CALL_PHONE"))
            {
                Snackbar snackPhonePermissionAccepted = Snackbar.make(layout,"Permission granted", Snackbar.LENGTH_SHORT)
                        .setAction("Open", v1 -> {

                            //Creando un intent para abrir el marcador del telefono
                            Intent camera = new Intent(Intent.ACTION_DIAL);
                            startActivity(camera);
                        });
                snackPhonePermissionAccepted.show();

                //Si no hay permiso, mostramos el aviso toast
            }else
            {
                toastPermissionDenied.show();
            }


        });



        //Al presionar el boton de Contacts...
        btnContacts.setOnClickListener(v-> {


            if (checkPermision("android.permission.READ_CONTACTS")) {
                Snackbar snackLocationPermissionAccepted = Snackbar.make(layout, "Permission granted", Snackbar.LENGTH_SHORT)
                        .setAction("Open", v1 -> {

                            //Creando un intent para abrir los contactos
                            Intent contacts = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
                            startActivity(contacts);
                        });
                snackLocationPermissionAccepted.show();


            } else {
                toastPermissionDenied.show();
            }
        });



    }

    //Funcion para determinar si la aplicacion obtuvo los permisos de aquel elemento en el permissionString
    private boolean checkPermision(String permissionString)
    {
        if(ContextCompat.checkSelfPermission(getApplicationContext(), permissionString) == PackageManager.PERMISSION_GRANTED)
        {
            return true;
        }
        else
        {
            return false;
        }
    }




}