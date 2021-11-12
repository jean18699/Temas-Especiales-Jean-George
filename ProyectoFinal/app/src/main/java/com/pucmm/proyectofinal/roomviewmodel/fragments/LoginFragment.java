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
    private EditText editUserName;
    private EditText editPassword;

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
      //  args.putString(ARG_PARAM1, param1);
       // args.putString(ARG_PARAM2, param2);
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


        //Uso las referencias directas en lugar del binding por que este ultimo no me respondia los eventos
        Button btnLogin = view.findViewById(R.id.btn_login);
        Button btnUserRegister = view.findViewById(R.id.btn_userRegister);
        editUserName = view.findViewById(R.id.editCategoryName);
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

        return view;

    }

    public void Login(){
        //App executors nos ayuda a ejecutar consultas de manera segura
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {

                if(appDatabase.userDao().findUserByCredentials(editUserName.getText().toString(), editPassword.getText().toString()) == null){
                    Snackbar.make(getView(), "Invalid username or password", Snackbar.LENGTH_LONG).show();
                }else
                {
                    Snackbar.make(getView(), "Logged succesfully!", Snackbar.LENGTH_LONG).show();

                    //Iniciando el dashboard
                    Intent dashboard = new Intent(getActivity(), MainActivity.class);
                    getActivity().startActivity(dashboard);
                }
                //AppDatabase.getInstance(getApplicationContext()).databaseDao().eraseUsers();
            }
        });
    }

}