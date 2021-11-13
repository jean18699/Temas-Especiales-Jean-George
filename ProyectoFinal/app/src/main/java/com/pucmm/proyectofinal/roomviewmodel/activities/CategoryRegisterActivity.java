package com.pucmm.proyectofinal.roomviewmodel.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.material.snackbar.Snackbar;
import com.pucmm.proyectofinal.R;
import com.pucmm.proyectofinal.roomviewmodel.database.AppDatabase;
import com.pucmm.proyectofinal.roomviewmodel.database.AppExecutors;
import com.pucmm.proyectofinal.roomviewmodel.model.Category;

public class CategoryRegisterActivity extends AppCompatActivity {

    private Button btnRegister;
    private EditText editCategory;
    private AppDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_register);
        btnRegister = findViewById(R.id.btn_registerCategory);
        editCategory = findViewById(R.id.editCategoryRegisterDescription);
        database = AppDatabase.getInstance(getApplicationContext());

        btnRegister.setOnClickListener(v -> {
            //Validando que todos los campos esten completos antes de registrar la categoria
            if(editCategory.getText().equals(""))
            {
                Snackbar.make(findViewById(R.id.main_category_register), "Please complete all the fields", Snackbar.LENGTH_LONG).show();
            }else
            {
                registerCategory();
            }

        });

    }

    public void registerCategory(){

        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {

                //Formateando el string de categoria
                String category_string = formatString(editCategory.getText().toString());

                //Verificando si categoria ya existe por su nombre
                if(database.categoryDao().findCategoryByName(category_string) != null){
                    Snackbar.make(findViewById(R.id.main_category_register), "This category is already registered", Snackbar.LENGTH_LONG).show();
                    return;
                }
                else
                {
                    //Registrando la nueva categoria
                    database.categoryDao().insert(new Category(
                            category_string, null
                    ));

                    //Volviendo a la lista de categorias
                    finish();

                }


            }
        });
    }

    public String formatString(String string){
        String formatString = string.toLowerCase();
        StringBuilder sb = new StringBuilder(formatString);
        sb.setCharAt(0, Character.toUpperCase(sb.charAt(0)));
        return sb.toString();
    }
}