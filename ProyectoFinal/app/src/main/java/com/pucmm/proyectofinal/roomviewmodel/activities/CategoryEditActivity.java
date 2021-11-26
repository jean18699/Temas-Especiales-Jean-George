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
import com.pucmm.proyectofinal.roomviewmodel.model.Category;

public class CategoryEditActivity extends AppCompatActivity {

    private Button btnEdit;
    private EditText editCategoryName;
    private AppDatabase database;
    private String categoryName;
    private Intent intent;
    private Category category;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_edit);
        btnEdit = findViewById(R.id.btn_registerCategory);
        editCategoryName = findViewById(R.id.editUserEmail);
        database = AppDatabase.getInstance(getApplicationContext());
        intent = getIntent();

        categoryName = intent.getStringExtra("categoryName");
        editCategoryName.setText(categoryName);


        btnEdit.setOnClickListener(v -> {
            //Validando que todos los campos esten completos antes de registrar la categoria
            if(editCategoryName.getText().equals(""))
            {
                Snackbar.make(findViewById(R.id.main_category_edit), "Please complete all the fields", Snackbar.LENGTH_LONG).show();
            }else
            {
                updateCategory();
            }

        });

    }

    public void updateCategory(){

        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {

                //Formateando el string de categoria
              //  String category_string = formatString(categoryName);

                category = database.categoryDao().findCategoryByName(categoryName);
                if(category != null){
                    category.setName(formatString(editCategoryName.getText().toString()));
                    database.categoryDao().update(category);
                    finish();
                    return;
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