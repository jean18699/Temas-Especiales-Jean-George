package com.pucmm.proyectofinal.roomviewmodel.fragments;

import android.content.Intent;
import android.os.Bundle;


import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.material.snackbar.Snackbar;
import com.pucmm.proyectofinal.R;
import com.pucmm.proyectofinal.roomviewmodel.activities.MainActivity;
import com.pucmm.proyectofinal.roomviewmodel.database.AppDatabase;
import com.pucmm.proyectofinal.roomviewmodel.database.AppExecutors;
import com.pucmm.proyectofinal.roomviewmodel.model.User;
import com.pucmm.proyectofinal.roomviewmodel.model.UserLogin;
import com.pucmm.proyectofinal.roomviewmodel.services.UserApiService;

import java.io.IOException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link LoginFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LoginFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER


    // TODO: Rename and change types of parameters
    private AppDatabase appDatabase;
    private EditText editEmail;
    private EditText editPassword;
    public Retrofit retrofit; //conexion con la API Retrofit para el usuario.
    Call<List<User>> users;

    public LoginFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *

     * @return A new instance of fragment LoginFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static LoginFragment newInstance() {
        LoginFragment fragment = new LoginFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
           // mParam1 = getArguments().getString(ARG_PARAM1);
          //  mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_login, container, false);

        //Conectandose a la API de usuarios
        retrofit = new Retrofit.Builder()
                .baseUrl("http://137.184.110.89:7002/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        users = retrofit.create(UserApiService.class).getAll();

        //Uso las referencias directas en lugar del binding por que este ultimo no me respondia los eventos
        Button btnLogin = view.findViewById(R.id.btn_login);
        Button btnUserRegister = view.findViewById(R.id.btn_userRegister);
        Button btnRecovery = view.findViewById(R.id.btn_forgotPassword);
        editEmail = view.findViewById(R.id.editUserEmail);
        editPassword = view.findViewById(R.id.editPassword);
        appDatabase = AppDatabase.getInstance(getActivity().getApplicationContext());

        //Logeando al usuario
        btnLogin.setOnClickListener(v->{
            Login();
        });

        //Acceder al fragmento de registrar usuario
        btnUserRegister.setOnClickListener(v->{
            getActivity().getSupportFragmentManager().beginTransaction()
                    .setReorderingAllowed(true)
                    .replace(R.id.main, UserRegisterFragment.newInstance())
                    .addToBackStack(null)
                    .commit();
        });

        btnRecovery.setOnClickListener(v -> {
            getActivity().getSupportFragmentManager().beginTransaction()
                    .setReorderingAllowed(true)
                    .replace(R.id.main, UserRecoveryFragment.newInstance())
                    .commit();
        });




        return view;

    }

    public void Login(){

        //App executors nos ayuda a ejecutar consultas de manera segura
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                UserLogin user = new UserLogin(editEmail.getText().toString(),editPassword.getText().toString());

              // Call<User> retrofit.create(UserApiService.class).login(user);
                Call<User> userCall = retrofit.create(UserApiService.class).login(user);
                userCall.enqueue(new Callback<User>() {
                    @Override
                    public void onResponse(Call<User> call, Response<User> response) {
                        //Iniciando el Dashboard
                        Intent dashboard = new Intent(getActivity(), MainActivity.class);
                        dashboard.putExtra("user",(response.body()));
                        getActivity().startActivity(dashboard);
                        System.out.println(response.body());
                    }

                    @Override
                    public void onFailure(Call<User> call, Throwable t) {
                        System.err.println(t.getLocalizedMessage());
                        Snackbar.make(getView(), "Invalid email or password", Snackbar.LENGTH_LONG).show();
                    }
                });

                //Si el request de logeo fue exitoso podemos continuar
                /*if(true){
                    Snackbar.make(getView(), "Logged succesfully!", Snackbar.LENGTH_LONG).show();

                    //Iniciando el Dashboard
                    Intent dashboard = new Intent(getActivity(), MainActivity.class);
                    dashboard.putExtra("email",(user.getEmail()));
                    getActivity().startActivity(dashboard);

                }else
                {
                    Snackbar.make(getView(), "Invalid email or password", Snackbar.LENGTH_LONG).show();
                }*/
            }
        });
    }

}