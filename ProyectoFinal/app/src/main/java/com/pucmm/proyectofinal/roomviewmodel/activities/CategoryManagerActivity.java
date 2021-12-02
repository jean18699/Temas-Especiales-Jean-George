package com.pucmm.proyectofinal.roomviewmodel.activities;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;


import com.google.android.material.snackbar.Snackbar;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.pucmm.proyectofinal.R;
import com.pucmm.proyectofinal.databinding.ActivityCategoryManagerBinding;


import com.pucmm.proyectofinal.networksync.FirebaseNetwork;
import com.pucmm.proyectofinal.networksync.NetResponse;
import com.pucmm.proyectofinal.roomviewmodel.database.AppDatabase;
import com.pucmm.proyectofinal.roomviewmodel.database.AppExecutors;
import com.pucmm.proyectofinal.roomviewmodel.model.Category;

import com.pucmm.proyectofinal.utils.CommonUtil;
import com.pucmm.proyectofinal.utils.KProgressHUDUtils;
import com.shashank.sony.fancytoastlib.FancyToast;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.function.Consumer;


public class CategoryManagerActivity extends AppCompatActivity {

    private ActivityCategoryManagerBinding binding;
    private AppDatabase database;
    private Uri uri;
    private Category managedCategory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCategoryManagerBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


      /*  btnRegister = findViewById(R.id.btn_registerCategory);
        editCategory = findViewById(R.id.editCategoryRegisterDescription);*/
        database = AppDatabase.getInstance(getApplicationContext());
        AppExecutors.getInstance().diskIO().execute(() -> {
            managedCategory = database.categoryDao().findCategoryById(getIntent().getIntExtra("categoryId",0));
            if (managedCategory != null) {
                binding.txtTitleCategory.setText("Edit Category");
                binding.editCategoryName.setText(managedCategory.getName());
                CommonUtil.downloadImage(managedCategory.getImage(), binding.categoryImage);

            }
        });

        // managedCategory = (Category) getIntent().getSerializableExtra("category");

       // System.out.println(managedCategory.getId());



        binding.btnRegisterCategory.setOnClickListener(v -> {

            //Validando que todos los campos esten completos antes de registrar la categoria
            if (binding.editCategoryName.getText().equals("")) {
                Snackbar.make(findViewById(R.id.main_category_register), "Please complete all the fields", Snackbar.LENGTH_LONG).show();
            } else {
                registerEditCategory();
            }
        });

        binding.categoryImage.setOnClickListener(v -> {
            uploadImage();
        });


    }

    public void uploadImage() {
        // initialising intent
        Intent intent = new Intent();
        // setting type to select to be image
        intent.setType("image/*");
        // solo se puede seleccionar 1 imagen
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, false);
        intent.setAction(Intent.ACTION_GET_CONTENT);

        pickAndChoosePictureResultLauncher.launch(intent);
    }

    public void registerEditCategory() {

        if (managedCategory == null) {
            managedCategory = new Category();
        }

        //Formateando el string de categoria
        String categoryName = formatString(binding.editCategoryName.getText().toString());
        managedCategory.setName(categoryName);


        //final KProgressHUD progressDialog = new KProgressHUDUtils(this).showConnecting();

        AppExecutors.getInstance().diskIO().execute(() -> {

            //Verificando si categoria ya existe por su nombre
            if (database.categoryDao().findCategoryByName(categoryName) != null) {
                Snackbar.make(findViewById(R.id.main_category_register), "This category is already registered", Snackbar.LENGTH_LONG).show();
                return;
            }

            //System.out.println(managedCategory.getId());
            if (managedCategory.getId() == null) {
                database.categoryDao().insert(managedCategory);
               // managedCategory.setId(id);
            } else {
                database.categoryDao().update(managedCategory);
            }

            if (uri != null && managedCategory.getId() != null) {
                FirebaseNetwork.obtain().upload(uri, String.format("categories/%s.jpg", managedCategory.getId()),
                    new NetResponse<String>() {
                        @Override
                        public void onResponse(String response) {
                            FancyToast.makeText(getApplicationContext(), "Successfully upload image", FancyToast.LENGTH_LONG, FancyToast.SUCCESS, false).show();
                            managedCategory.setImage(response);
                            AppExecutors.getInstance().diskIO().execute(() -> {
                                database.categoryDao().update(managedCategory);
                            });
                        }

                        @Override
                        public void onFailure(Throwable t) {
                            FancyToast.makeText(getApplicationContext(), t.getMessage(), FancyToast.LENGTH_LONG, FancyToast.ERROR, false).show();
                        }
                     });
            }
        });

        finish();
    }


    private final Consumer<KProgressHUD> consumer = new Consumer<KProgressHUD>() {
        @Override
        public void accept(KProgressHUD progressDialog) {
            FirebaseNetwork.obtain().upload(uri, String.format("categories/%s.jpg", managedCategory.getId()),
                    new NetResponse<String>() {
                        @Override
                        public void onResponse(String response) {
                            FancyToast.makeText(getApplicationContext(), "Successfully upload image", FancyToast.LENGTH_LONG, FancyToast.SUCCESS, false).show();
                            managedCategory.setImage(response);
                            AppExecutors.getInstance().diskIO().execute(() -> {
                                database.categoryDao().update(managedCategory);
                            });
                            progressDialog.dismiss();
                        }

                        @Override
                        public void onFailure(Throwable t) {
                            progressDialog.dismiss();
                            FancyToast.makeText(getApplicationContext(), t.getMessage(), FancyToast.LENGTH_LONG, FancyToast.ERROR, false).show();
                        }
                    });
        }
    };


    private ActivityResultLauncher<Intent> pickAndChoosePictureResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        try {
                            uri = result.getData().getData();
                            InputStream inputStream = getApplicationContext().getContentResolver().openInputStream(uri);
                            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);

                            binding.categoryImage.setImageBitmap(bitmap);
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        }
                    }
                }
            });


    public String formatString(String string) {
        String formatString = string.toLowerCase();
        StringBuilder sb = new StringBuilder(formatString);
        sb.setCharAt(0, Character.toUpperCase(sb.charAt(0)));
        return sb.toString();
    }
}