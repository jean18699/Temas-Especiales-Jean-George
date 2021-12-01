package com.pucmm.proyectofinal.roomviewmodel.activities;

import android.app.Activity;
import android.content.ClipData;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.Spinner;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.StorageReference;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.pucmm.proyectofinal.databinding.ActivityProductManagerBinding;
import com.pucmm.proyectofinal.networksync.CarouselUpload;
import com.pucmm.proyectofinal.networksync.NetResponse;
import com.pucmm.proyectofinal.roomviewmodel.model.Carousel;
import com.pucmm.proyectofinal.roomviewmodel.model.ProductWithCarousel;

import com.google.android.material.snackbar.Snackbar;
import com.pucmm.proyectofinal.R;
import com.pucmm.proyectofinal.networksync.FirebaseNetwork;
import com.pucmm.proyectofinal.roomviewmodel.database.AppDatabase;
import com.pucmm.proyectofinal.roomviewmodel.database.AppExecutors;
import com.pucmm.proyectofinal.roomviewmodel.model.Category;
import com.pucmm.proyectofinal.roomviewmodel.model.Product;
import com.pucmm.proyectofinal.utils.KProgressHUDUtils;
import com.shashank.sony.fancytoastlib.FancyToast;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class ProductManagerActivity extends AppCompatActivity {

    private ActivityProductManagerBinding binding;
    private AppDatabase database;
    private int position = 0;
    private ProductWithCarousel managedProduct;
    private ArrayList<Drawable> drawables;
    private List<Uri> files;
    private static final String PATH_UPLOAD = "images/";
    private static final String URL_DOWNLOAD = String.format("gs://android-da673.appspot.com/%s", PATH_UPLOAD);

    final private int REQUEST_CODE_ASK_PERMISSIONS = 123;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityProductManagerBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        managedProduct = (ProductWithCarousel) getIntent().getSerializableExtra("product");
        database = AppDatabase.getInstance(getApplicationContext());
        files = new ArrayList<>();
        drawables = new ArrayList<>();
        getAvailableCategories();

        //Si el producto esta registrado...
        if (managedProduct != null) {

            binding.editProductDescription.setText(managedProduct.product.getDescription());
            binding.editProductPrice.setText(String.valueOf(managedProduct.product.getPrice()));

            if (managedProduct.carousels != null && !managedProduct.carousels.isEmpty()) {
                final KProgressHUD progressDialog = new KProgressHUDUtils(this).showDownload();
                FirebaseNetwork.obtain().downloads(managedProduct.carousels, new NetResponse<List<Bitmap>>() {
                    @Override
                    public void onResponse(List<Bitmap> response) throws FileNotFoundException {
                        for (Bitmap bitmap : response) {
                            drawables.add(new BitmapDrawable(getResources(), bitmap));
                        }
                        binding.image.setImageDrawable(drawables.get(0));
                        progressDialog.dismiss();
                    }

                    @Override
                    public void onFailure(Throwable t) {
                        FancyToast.makeText(getApplicationContext(), t.getMessage(), FancyToast.LENGTH_LONG, FancyToast.ERROR, false).show();
                        progressDialog.dismiss();
                    }
                });
            }
        }

        binding.image.setFactory(() -> new ImageView(getApplicationContext()));
        binding.btnUploadImage.setOnClickListener(v -> photoOptions());

        binding.btnPreviousImage.setOnClickListener(v -> {
            if (position > 0) {
                binding.image.setImageDrawable(drawables.get(--position));
            } else {
                FancyToast.makeText(getApplicationContext(), "First Image Already Shown", FancyToast.LENGTH_SHORT, FancyToast.WARNING, false).show();
            }
        });

        binding.btnNextImage.setOnClickListener(v -> {
            if (position < drawables.size() - 1) {
                binding.image.setImageDrawable(drawables.get(++position));
            } else {
                FancyToast.makeText(getApplicationContext(), "Last Image Already Shown", FancyToast.LENGTH_SHORT, FancyToast.WARNING, false).show();
            }
        });

        binding.btnDeleteImage.setOnClickListener(v -> {
            if (drawables.size() > 0) {
                if (position >= files.size()) {
                    position--;
                }
                binding.image.setImageDrawable(drawables.get(position));
                drawables.remove(position);
            } else {
                binding.image.setImageDrawable(null);
            }
        });

        binding.btnRegisterProduct.setOnClickListener(v -> {
            //Validando que todos los campos esten completos antes de registrar el producto
            if (binding.editProductDescription.getText().equals("") || binding.editProductPrice.getText().equals("")) {
                Snackbar.make(findViewById(R.id.main_product_register), "Please complete all the fields", Snackbar.LENGTH_LONG).show();
            } else {
                registerEditProduct();
            }
        });
    }


    private void photoOptions() {
        // initialising intent
        Intent intent = new Intent();
        // setting type to select to be image
        intent.setType("image/*");
        // allowing multiple image to be selected
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        intent.setAction(Intent.ACTION_GET_CONTENT);

        pickAndChoosePictureResultLauncher.launch(intent);
    }

    private ActivityResultLauncher<Intent> pickAndChoosePictureResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        try {
                            final ClipData clipData = result.getData().getClipData();
                            if (clipData != null) {
                                for (int i = 0; i < clipData.getItemCount(); i++) {
                                    final Uri uri = clipData.getItemAt(i).getUri();
                                    final InputStream inputStream = getApplicationContext().getContentResolver().openInputStream(uri);
                                    final Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                                    drawables.add(new BitmapDrawable(getResources(), bitmap));
                                    files.add(uri);
                                }
                                // setting 1st selected image into image switcher
                            } else {
                                final Uri uri = result.getData().getData();
                                final InputStream inputStream = getContentResolver().openInputStream(uri);
                                final Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                                drawables.add(new BitmapDrawable(getResources(), bitmap));
                                files.add(uri);
                            }

                            binding.image.setImageDrawable(drawables.get(0));
                            position = 0;

                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        }
                    }
                }
            });


    @RequiresApi(api = Build.VERSION_CODES.N)
    public void registerEditProduct() {

        if (managedProduct == null) {
            managedProduct = new ProductWithCarousel();
        }

        managedProduct.product.setCategory(binding.spnCategory.getSelectedItem().toString());
        managedProduct.product.setDescription(binding.editProductDescription.getText().toString());
        managedProduct.product.setPrice(Double.valueOf(binding.editProductPrice.getText().toString()));


        AppExecutors.getInstance().diskIO().execute(() -> {

            if (database.productDao().findProductById(managedProduct.product.getProductId()) != null) {
                database.productDao().update(managedProduct.product);
            } else {
                database.productDao().insert(managedProduct.product);
            }

            List<CarouselUpload> uploads = new ArrayList<>();
            final List<Carousel> carousels = new ArrayList<>();


            database.productDao().deleteCarousels(managedProduct.product.getProductId());



            for (int index = 0; index < files.size(); index++) {
                Carousel carousel = new Carousel(managedProduct.product.getProductId(), index, String.format("products/%s/%s.jpg", managedProduct.product.getProductId(), index));
                carousels.add(carousel);
                uploads.add(new CarouselUpload(files.get(index), carousel));
            }

            database.productDao().insertCarousels(carousels);

            if (drawables != null && !drawables.isEmpty() && managedProduct.product.getProductId() != null) {
                final KProgressHUD progress = new KProgressHUDUtils(this).showDownload();
                FirebaseNetwork.obtain().uploads(uploads, new NetResponse<Void>() {
                    @Override
                    public void onResponse(Void response) {
                        FancyToast.makeText(getApplicationContext(), "Successfully upload images", FancyToast.LENGTH_LONG, FancyToast.SUCCESS, false).show();
                        progress.dismiss();
                    }

                    @Override
                    public void onFailure(Throwable t) {
                        progress.dismiss();
                        FancyToast.makeText(getApplicationContext(), t.getMessage(), FancyToast.LENGTH_LONG, FancyToast.ERROR, false).show();
                    }
                });
            }
            finish();
        });
    }


    public void getAvailableCategories() {

        AppExecutors.getInstance().diskIO().execute(() -> {

            List<String> categoryArray = new ArrayList<String>();

            for (Category category : database.categoryDao().findCategories()) {
                categoryArray.add(category.getName());
            }

            ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, categoryArray);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            binding.spnCategory.setAdapter(adapter);

            //Cargando la categoria
            if (managedProduct != null) {
                for (int i = 0; i < categoryArray.size(); i++) {
                    if (managedProduct.product.getCategory().equals(categoryArray.get(i))) {
                        binding.spnCategory.setSelection(i);
                    }
                }
            }

        });
    }

}