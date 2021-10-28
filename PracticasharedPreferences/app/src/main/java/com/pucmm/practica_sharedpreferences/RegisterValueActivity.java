package com.pucmm.practica_sharedpreferences;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.ClipData;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.pucmm.practica_sharedpreferences.placeholder.PlaceholderContent;

public class RegisterValueActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_value);

        Button btnRegistrar = findViewById(R.id.btnRegistrar);
        EditText editValor = findViewById(R.id.editValor);


        SharedPreferences sharedPreferences = getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();


        btnRegistrar.setOnClickListener(v -> {
            editor.putString(String.valueOf(PlaceholderContent.ITEMS.size()+1),editValor.getText().toString());
            editor.commit();

            PlaceholderContent.PlaceholderItem item = new PlaceholderContent.PlaceholderItem(String.valueOf(PlaceholderContent.ITEMS.size()+1),editValor.getText().toString());
            PlaceholderContent.ITEMS.add(item);

            editValor.setText("");//limpiando

            Toast.makeText(getApplicationContext(),"Su valor fue registrado existosamente. Retroceda para ver los cambios.", Toast.LENGTH_LONG).show();

        });


    }
}