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
    private EditText editUsername;
    private EditText editPassword;
    private EditText editEmail;
    private EditText editName;
    private EditText editLastName;
    private AppDatabase database;

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
        editUsername = view.findViewById(R.id.editUsername);
        editEmail = view.findViewById(R.id.editEmail);
        editPassword = view.findViewById(R.id.editPassword);
        editName = view.findViewById(R.id.editName);
        editLastName = view.findViewById(R.id.editLastName);
        database = AppDatabase.getInstance(getActivity().getApplicationContext());


        btnRegister.setOnClickListener(v -> {
            //Validando que todos los campos esten completos antes de registrarse
            if(editUsername.getText().toString().equals("") || editPassword.getText().toString().equals("") || editEmail.getText().toString().equals("") ||
                    editName.getText().toString().equals("") || editLastName.getText().toString().equals(""))
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

                //Verificando si el usuario ya existe por su nombre de usuario
                if(database.userDao().findUserByUsername(editUsername.getText().toString()) != null){
                    Snackbar.make(getView(), "This username is already taken", Snackbar.LENGTH_LONG).show();
                    return;
                }
                else if(database.userDao().findUserByEmail(editEmail.getText().toString()) != null)
                {
                    Snackbar.make(getView(), "This email is already registered", Snackbar.LENGTH_LONG).show();
                    return;
                }
                else
                {
                    //Registrando el nuevo usuario
                    database.userDao().insert(new User(
                            editUsername.getText().toString(),
                            editPassword.getText().toString(),
                            editEmail.getText().toString(),
                            editName.getText().toString(),
                            editLastName.getText().toString()
                    ));

                    //Volviendo al Login
                    getActivity().getSupportFragmentManager().beginTransaction()
                            .setReorderingAllowed(true)
                            .replace(R.id.main, LoginFragment.newInstance())
                            .commit();

                }


            }
        });
    }

}