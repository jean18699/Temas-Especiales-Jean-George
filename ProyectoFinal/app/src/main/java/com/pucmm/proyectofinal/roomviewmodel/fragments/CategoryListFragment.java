package com.pucmm.proyectofinal.roomviewmodel.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.pucmm.proyectofinal.R;
import com.pucmm.proyectofinal.databinding.FragmentCategoryListBinding;
import com.pucmm.proyectofinal.databinding.FragmentUserListBinding;
import com.pucmm.proyectofinal.roomviewmodel.adapters.CategoryAdapter;
import com.pucmm.proyectofinal.roomviewmodel.adapters.UserAdapter;
import com.pucmm.proyectofinal.roomviewmodel.database.AppDatabase;
import com.pucmm.proyectofinal.roomviewmodel.model.Category;
import com.pucmm.proyectofinal.roomviewmodel.viewmodel.CategoryViewModel;

import java.util.List;

/**
 * A fragment representing a list of Items.
 */
public class CategoryListFragment extends Fragment {

    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";
    // TODO: Customize parameters
    private int mColumnCount = 2;
    private AppDatabase appDatabase;
    private CategoryAdapter categoryAdapter;
    private RecyclerView categoryListRecyclerView;
    private @NonNull FragmentCategoryListBinding binding;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public CategoryListFragment() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static CategoryListFragment newInstance(int columnCount) {
        CategoryListFragment fragment = new CategoryListFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_category_list, container, false);
        categoryAdapter = new CategoryAdapter(getActivity().getApplicationContext());
        FloatingActionButton floatingActionButton = view.findViewById(R.id.floatBtn_addCategory);
        
        categoryListRecyclerView = view.findViewById(R.id.categoryList);

        if(mColumnCount <= 1)
        {
            categoryListRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext()));

        }else
        {
            categoryListRecyclerView.setLayoutManager(new GridLayoutManager(getActivity().getApplicationContext(), mColumnCount));

        }

        appDatabase = AppDatabase.getInstance(getActivity().getApplicationContext());
        categoryAdapter = new CategoryAdapter(getActivity().getApplicationContext());
        categoryListRecyclerView.setAdapter(categoryAdapter);

        //Pasando al fragmento de registrar categoria al clickear el boton flotante

        floatingActionButton.setOnClickListener(v ->
                        Snackbar.make(getView(), "funciona", Snackbar.LENGTH_LONG).show());
               /* getActivity().getSupportFragmentManager().beginTransaction()
                .setReorderingAllowed(true)
                .replace(R.id.content_frame, CategoryRegisterFragment.newInstance())
                .addToBackStack(null)
                .commit());
*/
        retrieveTasks();



        return view;
    }

    //Cargando los datos live del view model que utilizara la lista
    private void retrieveTasks(){
        CategoryViewModel categoryViewModel = new ViewModelProvider(this, new ViewModelProvider.Factory() {
            @NonNull
            @Override
            public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
                return (T) new CategoryViewModel(appDatabase);
            }
        }).get(CategoryViewModel.class);

        categoryViewModel.getCategoryListLiveData().observe(getActivity(), new Observer<List<Category>>() {

            @Override
            public void onChanged(List<Category> categories) {
                categoryAdapter.setCategories(categories);
            }
        });

    }

}