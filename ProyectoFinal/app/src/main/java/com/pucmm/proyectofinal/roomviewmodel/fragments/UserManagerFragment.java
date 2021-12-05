package com.pucmm.proyectofinal.roomviewmodel.fragments;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;

import com.google.android.material.snackbar.Snackbar;

import com.kaopiz.kprogresshud.KProgressHUD;
import com.pucmm.proyectofinal.R;
import com.pucmm.proyectofinal.databinding.FragmentUserManagerBinding;
import com.pucmm.proyectofinal.networksync.FirebaseNetwork;
import com.pucmm.proyectofinal.networksync.NetResponse;
import com.pucmm.proyectofinal.roomviewmodel.model.User;
import com.pucmm.proyectofinal.roomviewmodel.model.UserChangePassword;
import com.pucmm.proyectofinal.roomviewmodel.services.UserApiService;
import com.pucmm.proyectofinal.utils.CommonUtil;
import com.pucmm.proyectofinal.utils.KProgressHUDUtils;
import com.shashank.sony.fancytoastlib.FancyToast;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.function.Consumer;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link UserManagerFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class UserManagerFragment extends Fragment {

    private Retrofit retrofit;
    private FragmentUserManagerBinding binding;
    private User user;
    private Uri uri;
    private boolean editing;


    public UserManagerFragment() {
        // Required empty public constructor
    }

   public static UserManagerFragment newInstance(User user) {
        UserManagerFragment fragment = new UserManagerFragment();
        Bundle args = new Bundle();
        args.putSerializable("user",user);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            user = (User) getArguments().getSerializable("user");
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding =  FragmentUserManagerBinding.inflate(inflater,container,false);
        View view = binding.getRoot();

        binding.lblUserTitle.setText("Create Account");
        binding.userImage.setVisibility(View.GONE);

        if(user != null){

            binding.userImage.setVisibility(View.VISIBLE);
            System.out.println("MIRA FECHA:" + user.getBirthday());
          /*  String dateSubstring = user.getBirthday().substring(0,10);
            String [] splitDate = dateSubstring.split("-");
            String day = splitDate[2], month = splitDate[1], year = splitDate[0];*/
            binding.lblUserTitle.setText("Manage Account");
            binding.editPassword.setText(user.getPassword());
            binding.editEmail.setText(user.getEmail());
            binding.chkSeller.setChecked(user.getRol().equals(User.ROL.SELLER)? true : false);
            binding.editFirstName.setText(user.getFirstName());
            binding.editLastName.setText(user.getLastName());
            binding.editPhoneNumber.setText(user.getContact());
            binding.lblUserTitle.setText("Manage Account");
            //binding.editBirthDay.updateDate(Integer.valueOf(year),Integer.valueOf(month),Integer.valueOf(day));
            CommonUtil.downloadImage(user.getPhoto(), binding.userImage);
            editing = true;

        }

        retrofit = new Retrofit.Builder()
                .baseUrl("http://137.184.110.89:7002/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        binding.userImage.setOnClickListener(v->{
            uploadImage();
        });

        binding.btnRegisterUser.setOnClickListener(v -> {

            /**Validando que todos los campos esten completos antes de registrarse**/
            if(binding.editPassword.getText().toString().equals("") || binding.editEmail.getText().toString().equals("") ||
                    binding.editFirstName.getText().toString().equals("") || binding.btnRegisterUser.getText().toString().equals("") ||
                    binding.editPhoneNumber.getText().toString().equals(""))
            {
                Snackbar.make(getView(), "Please complete all the fields", Snackbar.LENGTH_LONG).show();
            }else
            {
                registerEditUser();
            }

        });

        return view;
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

    private ActivityResultLauncher<Intent> pickAndChoosePictureResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        try {
                            uri = result.getData().getData();
                            InputStream inputStream = getActivity().getContentResolver().openInputStream(uri);
                            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                            binding.userImage.setImageBitmap(bitmap);
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        }
                    }
                }
            });


    public void registerEditUser(){

        if(user == null){
            user = new User();
        }

        user.setFirstName(binding.editFirstName.getText().toString());
        user.setLastName(binding.editLastName.getText().toString());
        user.setContact(binding.editPhoneNumber.getText().toString());
        user.setBirthday(getBirthday(binding.editBirthDay));
        user.setEmail(binding.editEmail.getText().toString());
        user.setPassword(binding.editPassword.getText().toString());
        user.setRol(binding.chkSeller.isChecked() ? User.ROL.SELLER : User.ROL.CUSTOMER);

        final KProgressHUD progressDialog = new KProgressHUDUtils(getContext()).showConnecting();

        if(editing){

            final Call <User> userUpdateCall = retrofit.create(UserApiService.class).update(user);
            final Call <User> changePasswordCall = retrofit.create(UserApiService.class).changePassword(new UserChangePassword(user.getEmail(),user.getPassword()));
            call(userUpdateCall, res1 -> progressDialog.dismiss());
            call(changePasswordCall, res1 -> progressDialog.dismiss());
            progressDialog.dismiss();

        }else
        {
            final Call <User> userCreateCall = retrofit.create(UserApiService.class).create(user);
            call(userCreateCall, res1 -> progressDialog.dismiss());
            progressDialog.dismiss();
         
        }

        if(uri != null){
            FirebaseNetwork.obtain().upload(uri, String.format("profile/%s.jpg", user.getUid()),
                    new NetResponse<String>() {
                        @Override
                        public void onResponse(String response) {
                            FancyToast.makeText(getContext(), "Successfully upload image", FancyToast.LENGTH_LONG, FancyToast.SUCCESS, false).show();
                            user.setPhoto(response);
                            final Call<User> userUpdateCall = retrofit.create(UserApiService.class).update(user);
                            progressDialog.dismiss();
                            call(userUpdateCall, res1 -> progressDialog.dismiss());
                        }

                        @Override
                        public void onFailure(Throwable t) {

                            FancyToast.makeText(getActivity().getApplicationContext(), t.getMessage(), FancyToast.LENGTH_LONG, FancyToast.ERROR, false).show();
                        }
                    });
        }



    }

    private void call(Call<User> call, Consumer<Boolean> error) {
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                switch (response.code()) {
                    case 201:
                        FancyToast.makeText(getContext(), "Successfully registered", FancyToast.LENGTH_LONG, FancyToast.SUCCESS, false).show();
                        getActivity().getSupportFragmentManager().beginTransaction()
                                .setReorderingAllowed(true)
                                .replace(R.id.main, LoginFragment.newInstance())
                                .commit();
                        error.accept(false);
                        break;
                    case 204:
                        FancyToast.makeText(getContext(), "Successfully updated", FancyToast.LENGTH_LONG, FancyToast.SUCCESS, false).show();
                        error.accept(false);
                        break;
                    default:
                        try {
                            error.accept(true);
                            FancyToast.makeText(getContext(), response.errorBody().string(), FancyToast.LENGTH_LONG, FancyToast.ERROR, false).show();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                error.accept(true);
                FancyToast.makeText(getContext(), t.getMessage(), FancyToast.LENGTH_LONG, FancyToast.ERROR, false).show();
            }
        });

    }

    private String getBirthday(DatePicker datePicker) {
        final int day = datePicker.getDayOfMonth();
        final int month = datePicker.getMonth();
        final int year = datePicker.getYear();

        final Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, day);

        final DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");

        return dateFormat.format(calendar.getTime());
    }

}