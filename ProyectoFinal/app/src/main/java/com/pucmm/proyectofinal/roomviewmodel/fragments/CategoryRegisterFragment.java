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
import com.pucmm.proyectofinal.roomviewmodel.database.AppExecutors;
import com.pucmm.proyectofinal.roomviewmodel.model.Category;
import com.pucmm.proyectofinal.roomviewmodel.model.User;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CategoryRegisterFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CategoryRegisterFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER


    // TODO: Rename and change types of parameters
    private Button btnRegister;
    private EditText editCategory;
    private AppDatabase database;

    public CategoryRegisterFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *

     * @return A new instance of fragment UserRegisterFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CategoryRegisterFragment newInstance() {
        CategoryRegisterFragment fragment = new CategoryRegisterFragment();
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

        View view = inflater.inflate(R.layout.fragment_category_register, container, false);

        btnRegister = view.findViewById(R.id.btn_registerCategory);
        editCategory = view.findViewById(R.id.editCategoryRegisterName);
        database = AppDatabase.getInstance(getActivity().getApplicationContext());


        btnRegister.setOnClickListener(v -> {
            //Validando que todos los campos esten completos antes de registrar la categoria

                Snackbar.make(getView(), "Please complete all the fields", Snackbar.LENGTH_LONG).show();

                //registerCategory();

        });

        return view;
    }

    public void registerCategory(){

        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {

                //Verificando si categoria ya existe por su nombre
                if(database.categoryDao().findCategoryByName(editCategory.getText().toString()) != null){
                    Snackbar.make(getView(), "This category is already registered", Snackbar.LENGTH_LONG).show();
                    return;
                }
                else
                {
                    //Registrando la nueva categoria
                    database.categoryDao().insert(new Category(
                            editCategory.getText().toString()
                    ));

                    //Volviendo a la lista de categorias
                    getActivity().getSupportFragmentManager().beginTransaction()
                            .setReorderingAllowed(true)
                            .replace(R.id.content_frame, CategoryListFragment.newInstance())
                            .commit();

                }


            }
        });
    }

}