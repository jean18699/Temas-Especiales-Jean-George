package com.pucmm.proyectofinal.roomviewmodel.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.fragment.app.Fragment;

import com.google.android.material.snackbar.Snackbar;
import com.pucmm.proyectofinal.R;
import com.pucmm.proyectofinal.roomviewmodel.database.AppDatabase;
import com.pucmm.proyectofinal.roomviewmodel.database.AppExecutors;
import com.pucmm.proyectofinal.roomviewmodel.model.User;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link UserEditFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class UserEditFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER


    // TODO: Rename and change types of parameters
    private Button btnEdit;
    private EditText editUsername;
    private EditText editPassword;
    private EditText editEmail;
    private EditText editName;
    private AppDatabase database;
    private User user;

    public UserEditFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *

     * @return A new instance of fragment UserRegisterFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static UserEditFragment newInstance(User user) {
        UserEditFragment fragment = new UserEditFragment();
        Bundle args = new Bundle();
        args.putSerializable("user", user);
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

        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        btnEdit = view.findViewById(R.id.btn_editUser);
        editUsername = view.findViewById(R.id.editUserName);
        editEmail = view.findViewById(R.id.editEmail);
        editPassword = view.findViewById(R.id.editPassword);
        editName = view.findViewById(R.id.editName);
        database = AppDatabase.getInstance(getActivity().getApplicationContext());
        user = (User) getArguments().getSerializable("user");

        editUsername.setEnabled(false);

        editUsername.setText(user.getUsername());
        editEmail.setText(user.getEmail());
        editPassword.setText(user.getPassword());
        editName.setText(user.getName());

        editUsername.bringToFront();

        btnEdit.setOnClickListener(v -> {
            //Validando que todos los campos esten completos antes de registrarse
            if(editUsername.getText().toString().equals("") || editPassword.getText().toString().equals("") || editEmail.getText().toString().equals("") ||
                    editName.getText().toString().equals(""))
            {
                Snackbar.make(getView(), "Please complete all the fields", Snackbar.LENGTH_LONG).show();
            }else
            {
                updateUser();
            }

        });

        return view;
    }

    public void updateUser(){

        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {

                //Verificando si el usuario ya existe por su nombre de usuario
                //User user = database.userDao().findUserByUsername(editUsername.getText().toString());
                if(user != null){
                    user.setEmail(editEmail.getText().toString());
                    user.setName(editName.getText().toString());
                    user.setPassword(editPassword.getText().toString());
                    database.userDao().update(user);

                    Snackbar.make(getView(), "Update completed!", Snackbar.LENGTH_LONG).show();

                    getActivity().getSupportFragmentManager().beginTransaction()
                            .setReorderingAllowed(true)
                            .replace(R.id.content_frame, UserEditFragment.newInstance(user))
                            .commit();

                    return;
                }
            }
        });
    }

}