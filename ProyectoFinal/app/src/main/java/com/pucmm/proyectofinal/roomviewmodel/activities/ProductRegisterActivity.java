package com.pucmm.proyectofinal.roomviewmodel.activities;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.snackbar.Snackbar;
import com.pucmm.proyectofinal.R;
import com.pucmm.proyectofinal.roomviewmodel.database.AppDatabase;
import com.pucmm.proyectofinal.roomviewmodel.database.AppExecutors;
import com.pucmm.proyectofinal.roomviewmodel.model.Product;

public class ProductRegisterActivity extends AppCompatActivity {

    private Button btnRegister;
    private EditText editProductDescription;
    private EditText editProductPrice;
    private AppDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_register);
        btnRegister = findViewById(R.id.btn_registerProduct);
        editProductDescription = findViewById(R.id.editProductDescription);
        editProductPrice = findViewById(R.id.editProductPrice);
        database = AppDatabase.getInstance(getApplicationContext());

        btnRegister.setOnClickListener(v -> {
            //Validando que todos los campos esten completos antes de registrar el producto
            if(editProductDescription.getText().equals("") || editProductPrice.getText().equals(""))
            {
                Snackbar.make(findViewById(R.id.main_product_register), "Please complete all the fields", Snackbar.LENGTH_LONG).show();
            }else
            {
                registerProduct();
            }

        });

    }

    public void registerProduct(){

        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {

                //Registrando la nuevo producto
                database.productDao().insert(new Product(editProductDescription.getText().toString(),Float.valueOf(editProductPrice.getText().toString()),null));

                //Volviendo a la lista de productos
                finish();




            }
        });
    }

}