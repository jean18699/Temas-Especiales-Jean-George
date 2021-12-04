package com.pucmm.proyectofinal.roomviewmodel.fragments;

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
import com.pucmm.proyectofinal.roomviewmodel.model.User;
import com.pucmm.proyectofinal.roomviewmodel.services.UserApiService;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link UserRecoveryFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class UserRecoveryFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER


    // TODO: Rename and change types of parameters
    private Button btnRegister;
    private Button btnLogin;
    private Button btnRecovery;
    private EditText editEmail;
    private List<User> users;

    public UserRecoveryFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment UserRegisterFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static UserRecoveryFragment newInstance() {
        UserRecoveryFragment fragment = new UserRecoveryFragment();
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

        View view = inflater.inflate(R.layout.fragment_forgot_password, container, false);

        btnRecovery = view.findViewById(R.id.btnGetPassword);
        btnLogin = view.findViewById(R.id.btnLoginNow);
        btnRegister = view.findViewById(R.id.btnRegisterNow);
        editEmail = view.findViewById(R.id.editEmailRecovery);



        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://137.184.110.89:7002/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        Call<List<User>> userCall = retrofit.create(UserApiService.class).getAll();
        userCall.enqueue(new Callback<List<User>>() {

            @Override
            public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                users = response.body();

            }

            @Override
            public void onFailure(Call<List<User>> call, Throwable t) {
                Snackbar.make(getView(),"An error has ocurried retrieving the accounts", Snackbar.LENGTH_LONG);
            }
        });

        btnRecovery.setOnClickListener(v -> {
            //Validando que todos los campos esten completos antes de registrarse
            if (editEmail.getText().toString().equals("")) {
                Snackbar.make(getView(), "Please provide an email address", Snackbar.LENGTH_LONG).show();
            } else {
                showPassword();
            }
        });

        btnLogin.setOnClickListener(v -> {
            getActivity().getSupportFragmentManager().beginTransaction()
                    .setReorderingAllowed(true)
                    .replace(R.id.main, LoginFragment.newInstance())
                    .addToBackStack(null)
                    .commit();
        });

        btnRegister.setOnClickListener(v -> {
            getActivity().getSupportFragmentManager().beginTransaction()
                    .setReorderingAllowed(true)
                    .replace(R.id.main, UserManagerFragment.newInstance(null))
                    .addToBackStack(null)
                    .commit();
        });



        return view;
    }

    public void showPassword() {

            for(User user : users){
                System.out.println(user.getEmail());
                if(user.getEmail().equalsIgnoreCase(editEmail.getText().toString())){
                    Snackbar.make(getView(), "Your password is: " + user.getPassword(), Snackbar.LENGTH_LONG).show();
                    return;
                }else
                {
                    Snackbar.make(getView(), "This email is not registered", Snackbar.LENGTH_LONG).show();
                }
                /*
                if(user.getEmail().equals(editEmail.getText())){
                    System.out.println("mirame size: "+ users.size());
                    Snackbar.make(getView(), "Your password is: " + user.getPassword(), Snackbar.LENGTH_LONG).show();
                }*/

          //  User user = database.userDao().findUserByEmail(editEmail.getText().toString());
          /*  if (user != null) {
                Snackbar.make(getView(), "Your password is: " + user.getPassword(), Snackbar.LENGTH_LONG).show();
                return;
            } else {
                Snackbar.make(getView(), "This email is not registered", Snackbar.LENGTH_LONG).show();
            }*/

        }

    }

}