package com.pucmm.proyectofinal.roomviewmodel.activities;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Toast;


import com.google.android.material.snackbar.Snackbar;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.pucmm.proyectofinal.R;
import com.pucmm.proyectofinal.databinding.ActivityCategoryManagerBinding;


import com.pucmm.proyectofinal.networksync.FirebaseNetwork;
import com.pucmm.proyectofinal.networksync.NetResponse;
import com.pucmm.proyectofinal.roomviewmodel.database.AppDatabase;
import com.pucmm.proyectofinal.roomviewmodel.database.AppExecutors;
import com.pucmm.proyectofinal.roomviewmodel.model.Category;

import com.pucmm.proyectofinal.roomviewmodel.model.ProductWithCarousel;
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
    private boolean editImage;
    /**Para obtener la referencia de cual fue el antiguo nombre de la categoria en el caso de que lo cambie**/
    private String oldCategoryName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCategoryManagerBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        database = AppDatabase.getInstance(getApplicationContext());
        managedCategory = (Category) getIntent().getSerializableExtra("category");
        AppExecutors.getInstance().diskIO().execute(() -> {

            if (managedCategory != null) {
                oldCategoryName = managedCategory.getName();
                binding.txtTitleCategory.setText("Edit Category");
                binding.editCategoryName.setText(managedCategory.getName());
                binding.btnRegisterCategory.setText("Edit");
                CommonUtil.downloadImage(managedCategory.getImage(), binding.categoryImage);

            }
        });

        binding.btnRegisterCategory.setOnClickListener(v -> {

            //Validando que todos los campos esten completos antes de registrar la categoria
            if (binding.editCategoryName.getText().equals("")) {
                Snackbar.make(findViewById(R.id.main_category_register), "Please complete all the fields", Snackbar.LENGTH_LONG).show();
            } else {
                registerEditCategory();
            }
        });

        binding.categoryImage.setOnClickListener(v -> {
            editImage = true;
            requestImagePermissions();

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

    public void requestImagePermissions(){
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                1);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 1: {

                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                   uploadImage();
                } else {
                    Toast.makeText(this, "Permission denied to read your External storage", Toast.LENGTH_SHORT).show();
                }
                return;
            }
        }
    }

    public void registerEditCategory() {

        if (managedCategory == null) {
            managedCategory = new Category();
        }

        final KProgressHUD progressDialog = new KProgressHUDUtils(this).showConnecting();

        AppExecutors.getInstance().diskIO().execute(() -> {
            //Formateando el string de categoria
            String categoryName = formatString(binding.editCategoryName.getText().toString());
            managedCategory.setName(categoryName);

            //Verificando si categoria ya existe por su nombre
            if (database.categoryDao().findCategoryByName(categoryName) != null) {
                Snackbar.make(findViewById(R.id.main_category_register), "This category is already registered", Snackbar.LENGTH_LONG).show();
                progressDialog.dismiss();
            }else
            {
                if (managedCategory.getId() == null) {
                   int id = (int) database.categoryDao().insert(managedCategory);
                   managedCategory.setId(id);
                } else {
                    database.categoryDao().update(managedCategory);

                    if(!oldCategoryName.equals(managedCategory.getName()))
                    {
                        /**Editando la categoria para todos los productos que la tienen seleccionada**/
                        for(ProductWithCarousel product : database.productDao().getProducts()){
                            if(product.product.getCategory().equalsIgnoreCase(oldCategoryName)){
                                // System.out.println(product.product.getDescription());
                                product.product.setCategory(managedCategory.getName());
                                database.productDao().update(product.product);
                            }
                        }
                    }

                }


                if (uri != null && managedCategory.getId()!= 0 && editImage) {
                    consumer.accept(progressDialog);
                }else
                {
                    progressDialog.dismiss();
                }
                finish();
            }

        });


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