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

import java.util.concurrent.atomic.AtomicBoolean;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private final String[] perms = {"android.permission.FINE_LOCATION"};
    private final int permsRequestCode = 200;
    Button btnCancel;
    Button btnContinue;
    Switch swtLocation;
    boolean locationAccepted;
    Intent access;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        access = new Intent(MainActivity.this, AccessActivity.class);

        btnContinue = findViewById(R.id.btnContinue);
        btnCancel = findViewById(R.id.btnCancel);
        swtLocation = findViewById(R.id.swtLocation);

        btnContinue.setOnClickListener(v->{
            if(swtLocation.isChecked()){

                  ActivityCompat.requestPermissions(this,new String[]{"android.permission.ACCESS_FINE_LOCATION"},permsRequestCode);
            }else
            {
                startActivity(access);
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

            /*case R.id.btnContinue:
                if(swtLocation.isChecked()){
                       finish();
                    //ActivityCompat.requestPermissions(this,new String[]{ACCESS_FINE_LOCATION},permsRequestCode);
                }*/
        }
     }


     //Resultados de haber pedido los permisos
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch(requestCode)
        {
            case permsRequestCode:

                if(grantResults.length > 0)
                {
                    locationAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;

                    break;
                }else
                {
                    break;
                }
            default:
                access.putExtra("locationAccepted", false);
        }

        startActivity(access);

    }
}