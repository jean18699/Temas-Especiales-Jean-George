package com.pucmm.proyectofinal.roomviewmodel.activities;

import android.Manifest;
import android.app.Activity;
import android.content.ClipData;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
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
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.common.internal.Constants;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.pucmm.proyectofinal.networksync.CarouselUpload;
import com.pucmm.proyectofinal.networksync.NetResponse;
import com.pucmm.proyectofinal.roomviewmodel.model.Carousel;
import com.pucmm.proyectofinal.roomviewmodel.model.ProductWithCarousel;
import com.pucmm.proyectofinal.utils.FileUtil;

import com.google.android.material.snackbar.Snackbar;
import com.pucmm.proyectofinal.R;
import com.pucmm.proyectofinal.networksync.FirebaseNetwork;
import com.pucmm.proyectofinal.roomviewmodel.database.AppDatabase;
import com.pucmm.proyectofinal.roomviewmodel.database.AppExecutors;
import com.pucmm.proyectofinal.roomviewmodel.model.Category;
import com.pucmm.proyectofinal.roomviewmodel.model.Product;
import com.pucmm.proyectofinal.utils.KProgressHUDUtils;
import com.shashank.sony.fancytoastlib.FancyToast;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

public class ProductRegisterActivity extends AppCompatActivity {

    private Button btnRegister;
    private Button btnUploadImage;
    private Button btnPreviousImage;
    private Button btnNextImage;
    private Button btnDeleteImage;
    private EditText editProductDescription;
    private EditText editProductPrice;
    private Spinner spnCategories;
    private AppDatabase database;
    private ImageSwitcher image;
    private int position = 0;
    private ProductWithCarousel element;
    List<Uri> files;

    final private int REQUEST_CODE_ASK_PERMISSIONS = 123;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_register);

        btnRegister = findViewById(R.id.btn_registerProduct);
        //ImageView addImage = findViewById(R.id.iv_add_image);
        editProductDescription = findViewById(R.id.editProductDescription);
        editProductPrice = findViewById(R.id.editProductPrice);
        spnCategories = findViewById(R.id.spnCategory);
        btnUploadImage = findViewById(R.id.btnUploadImage);
        btnPreviousImage = findViewById(R.id.btnPreviousImage);
        btnDeleteImage = findViewById(R.id.btnDeleteImage);
        btnNextImage = findViewById(R.id.btnNextImage);
        image = findViewById(R.id.image);
        database = AppDatabase.getInstance(getApplicationContext());
        files = new ArrayList<>();

        //Para llenar el spinner de categorias
        getAvailableCategories();

        image.setFactory(()-> new ImageView(getApplicationContext()));



        btnUploadImage.setOnClickListener(v->photoOptions());

        btnPreviousImage.setOnClickListener(v->{
            if (position > 0) {
                image.setImageURI(files.get(--position));
            } else {
                FancyToast.makeText(getApplicationContext(), "First Image Already Shown", FancyToast.LENGTH_SHORT, FancyToast.WARNING, false).show();
            }
        });
        btnNextImage.setOnClickListener(v->{
            if (position < files.size() - 1) {
                image.setImageURI(files.get(++position));
            } else {
                FancyToast.makeText(getApplicationContext(), "Last Image Already Shown", FancyToast.LENGTH_SHORT, FancyToast.WARNING, false).show();
            }
        });

        btnDeleteImage.setOnClickListener(v->{
            if(files.size() > 1){
                files.remove(position);
                image.setImageURI(files.get(--position));
            }
            else if(files.size() == 1)
            {
                files.remove(position);
                position = 0;
                image.setImageURI(null);
            }
        });

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

       /* addImage.setOnClickListener(v->{
            requestPermission();
            addImage();
        });*/

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
                        final ClipData clipData = result.getData().getClipData();
                        if (clipData != null) {
                            for (int i = 0; i < clipData.getItemCount(); i++) {
                                // adding imageuri in array
                                final Uri uri = clipData.getItemAt(i).getUri();
                                files.add(uri);
                            }
                            // setting 1st selected image into image switcher
                        } else {
                            Uri uri = result.getData().getData();
                            files.add(uri);
                        }

                        image.setImageURI(files.get(0));
                        position = 0;
                    }
                }
            });


    @RequiresApi(api = Build.VERSION_CODES.N)
    public void registerProduct(){

        if (element == null) {
            element = new ProductWithCarousel();
        }

        Product product = new Product(editProductDescription.getText().toString(),Double.valueOf(editProductPrice.getText().toString()),
                spnCategories.getSelectedItem().toString());

        element.product = product;
       /* element.product.setDescription();
        element.product.setCategory(spnCategories.getSelectedItem().toString());
        element.product.setPrice(Double.valueOf(editProductPrice.getText().toString()));
        element.product.setActive(true);*/

        //final KProgressHUD progressDialog = new KProgressHUDUtils(this).showConnecting();


        AppExecutors.getInstance().diskIO().execute(() -> {

            List<CarouselUpload> uploads = new ArrayList<>();
            database.productDao().deleteCarousels(element.product.getProductId());
            final List<Carousel> carousels = new ArrayList<>();
            for (int index = 0; index < files.size(); index++) {
                Carousel carousel = new Carousel(element.product.getProductId(), index, String.format("products/%s/%s.jpg", element.product.getProductId(), index));
                carousels.add(carousel);
                uploads.add(new CarouselUpload(files.get(index), carousel));
            }
            database.productDao().insertCarousels(carousels);

            if (files != null && !files.isEmpty() && element.product.getProductId() != null) {
                FirebaseNetwork.obtain().uploads(uploads, new NetResponse<Void>() {
                    @Override
                    public void onResponse(Void response) {
                        FancyToast.makeText(getApplicationContext(), "Successfully upload images", FancyToast.LENGTH_LONG, FancyToast.SUCCESS, false).show();
                        //progress.dismiss();
                    }

                    @Override
                    public void onFailure(Throwable t) {
                      //  progress.dismiss();
                        FancyToast.makeText(getApplicationContext(), t.getMessage(), FancyToast.LENGTH_LONG, FancyToast.ERROR, false).show();
                    }
                });
                //function.apply(uploads).accept(progressDialog);
            } else {
               // progressDialog.dismiss();
            }

          /*  //Registrando el nuevo producto

            database.productDao().deleteCarousels();
            List<CarouselUpload> uploads = new ArrayList<>();


            database.productDao().insert(
                    new Product(editProductDescription.getText().toString(),Double.valueOf(editProductPrice.getText().toString()),
                            spnCategories.getSelectedItem().toString(),null));
*/
            //Volviendo a la lista de productos
            finish();
        });
    }

   /* private final Function<List<CarouselUpload>, Consumer<KProgressHUD>> function = uploads -> progress -> {
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

    };*/

    public void getAvailableCategories(){

        AppExecutors.getInstance().diskIO().execute(() -> {

            List<String> spinnerArray =  new ArrayList<String>();

            for(Category category : database.categoryDao().findCategories())
            {
                spinnerArray.add(category.getName());
            }

            ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item,spinnerArray);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spnCategories.setAdapter(adapter);

        });
    }



  /*  //===== add image in layout
    public void addImage() {
        LayoutInflater inflater=(LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View rowView=inflater.inflate(R.layout.image, null);
        selectedImage = rowView.findViewById(R.id.number_edit_text);
        selectImage(this);
    }

    //===== select image
    private void selectImage(Context context) {
        final CharSequence[] options = {"Take Photo", "Choose from Gallery", "Cancel"};

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setCancelable(false);
        builder.setTitle("Choose a Media");

        builder.setItems(options, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int item) {

                if (options[item].equals("Take Photo")) {
                    Intent takePicture = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(takePicture, 0);

                } else if (options[item].equals("Choose from Gallery")) {
                    Intent pickPhoto = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(pickPhoto, 1);//one can be replaced with any action code

                } else if (options[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    // this is all you need to grant your application external storage permision
    private void requestPermission(){
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},REQUEST_CODE_ASK_PERMISSIONS);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_CANCELED) {
            switch (requestCode) {
                case 0:
                    if (resultCode == RESULT_OK && data != null) {

                        Bitmap img = (Bitmap) data.getExtras().get("data");
                        selectedImage.setImageBitmap(img);
                        Picasso.get().load(getImageUri(this,img)).into(selectedImage);

                        String imgPath = FileUtil.getPath(this,getImageUri(this,img));

                        files.add(Uri.parse(imgPath));
                        Log.e("image", imgPath);
                    }

                    break;
                case 1:
                    if (resultCode == RESULT_OK && data != null) {
                        Uri img = data.getData();
                        Picasso.get().load(img).into(selectedImage);

                        String imgPath = FileUtil.getPath(this,img);

                        files.add(Uri.parse(imgPath));
                        Log.e("image", imgPath);

                    }
                    break;
            }
        }
    }

    //===== bitmap to Uri
    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "intuenty", null);
        Log.d("image uri",path);
        return Uri.parse(path);
    }
*/

}