package com.pucmm.proyectofinal.roomviewmodel.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.material.snackbar.Snackbar;
import com.pucmm.proyectofinal.R;
import com.pucmm.proyectofinal.roomviewmodel.database.AppDatabase;
import com.pucmm.proyectofinal.roomviewmodel.database.AppExecutors;
import com.pucmm.proyectofinal.roomviewmodel.model.User;
import com.pucmm.proyectofinal.roomviewmodel.services.UserApiService;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link UserRegisterFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class UserRegisterFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER


    // TODO: Rename and change types of parameters
    private Button btnRegister;
    private EditText editPassword;
    private EditText editEmail;
    private EditText editFirstName;
    private EditText editLastName;
    private EditText editContact;
    private EditText editBirthday;
    private AppDatabase database;
    private Retrofit retrofit;

    public UserRegisterFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *

     * @return A new instance of fragment UserRegisterFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static UserRegisterFragment newInstance() {
        UserRegisterFragment fragment = new UserRegisterFragment();
        Bundle args = new Bundle();
        

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_user_register, container, false);

        btnRegister = view.findViewById(R.id.btn_registerUser);
        editEmail = view.findViewById(R.id.editEmail);
        editPassword = view.findViewById(R.id.editPassword);
        editFirstName = view.findViewById(R.id.editFirstName);
        editLastName = view.findViewById(R.id.editLastName);
        editContact = view.findViewById(R.id.editPhoneNumber);
        editBirthday = view.findViewById(R.id.editBirthDay);
        database = AppDatabase.getInstance(getActivity().getApplicationContext());
        retrofit = new Retrofit.Builder()
                .baseUrl("http://137.184.110.89:7002/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        btnRegister.setOnClickListener(v -> {
            //Validando que todos los campos esten completos antes de registrarse
            if(editPassword.getText().toString().equals("") || editEmail.getText().toString().equals("") ||
                    editFirstName.getText().toString().equals("") || editLastName.getText().toString().equals("") ||
                    editContact.getText().toString().equals("") || editBirthday.getText().toString().equals(""))
            {
                Snackbar.make(getView(), "Please complete all the fields", Snackbar.LENGTH_LONG).show();
            }else
            {
                registerUser();
            }

        });

        return view;
    }

    public void registerUser(){

        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {

                User user = new User(
                        editPassword.getText().toString(),
                        editEmail.getText().toString(),
                        User.ROLE.CUSTOMER,
                        editFirstName.getText().toString(),
                        editLastName.getText().toString(),
                        editContact.getText().toString(),
                        editBirthday.getText().toString()
                );

                Call<User> userCall = retrofit.create(UserApiService.class).create(user);
                userCall.enqueue(new Callback<User>() {
                    @Override
                    public void onResponse(Call<User> call, Response<User> response) {
                       getActivity().getSupportFragmentManager().beginTransaction()
                                .setReorderingAllowed(true)
                                .replace(R.id.main, LoginFragment.newInstance())
                                .commit();
                    }

                    @Override
                    public void onFailure(Call<User> call, Throwable t) {
                        System.err.println(t.getLocalizedMessage());
                        Snackbar.make(getView(), "This email is already registered", Snackbar.LENGTH_LONG).show();
                    }
                });

                   /* if()
                     {
                         //Volviendo al Login luego de registrar el usuario
                         getActivity().getSupportFragmentManager().beginTransaction()
                                 .setReorderingAllowed(true)
                                 .replace(R.id.main, LoginFragment.newInstance())
                                 .commit();
                     }
                     else   //Si hubo un error es por que ya el email estaba registrado
                     {
                         Snackbar.make(getView(), "This email is already registered", Snackbar.LENGTH_LONG).show();
                     }*/
            }
        });
    }

}