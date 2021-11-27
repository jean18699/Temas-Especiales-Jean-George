package com.pucmm.proyectofinal.roomviewmodel.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.snackbar.Snackbar;
import com.pucmm.proyectofinal.R;
import com.pucmm.proyectofinal.roomviewmodel.database.AppDatabase;
import com.pucmm.proyectofinal.roomviewmodel.database.AppExecutors;
import com.pucmm.proyectofinal.roomviewmodel.model.Product;
import com.pucmm.proyectofinal.roomviewmodel.model.User;

public class ProductEditActivity extends AppCompatActivity {

    private Button btnEdit;
    private EditText editProductDescription, editProductPrice;
    private AppDatabase database;
    private Product product;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_edit);
        btnEdit = findViewById(R.id.btnEditProduct);
        editProductDescription = findViewById(R.id.editProductDescription);
        editProductPrice = findViewById(R.id.editProductPrice);
        database = AppDatabase.getInstance(getApplicationContext());
        product = (Product) getIntent().getSerializableExtra("product");
        populateUI(product);


        btnEdit.setOnClickListener(v -> {
            //Validando que todos los campos esten completos antes de registrar la categoria
            if(editProductDescription.getText().equals("") || editProductPrice.getText().equals(""))
            {
                Snackbar.make(findViewById(R.id.main_product_edit), "Please complete all the fields", Snackbar.LENGTH_LONG).show();
            }else
            {
                updateProduct();
            }

        });

    }

    private void populateUI(Product product){
        editProductDescription.setText(product.getDescription());
        editProductPrice.setText(product.getPrice().toString());
    }

    public void updateProduct(){

        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {

                //Formateando el string de categoria
                if(product != null){
                    product.setDescription(editProductDescription.getText().toString());
                    product.setPrice(Float.parseFloat(editProductPrice.getText().toString()));
                    database.productDao().update(product);
                    finish();
                    return;
                }


            }
        });
    }

}