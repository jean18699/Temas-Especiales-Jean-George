package com.pucmm.proyectofinal.roomviewmodel.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.pucmm.proyectofinal.R;
import com.pucmm.proyectofinal.databinding.FragmentCategoryListBinding;
import com.pucmm.proyectofinal.roomviewmodel.activities.CategoryRegisterActivity;
import com.pucmm.proyectofinal.roomviewmodel.activities.ProductRegisterActivity;
import com.pucmm.proyectofinal.roomviewmodel.adapters.ProductAdapter;
import com.pucmm.proyectofinal.roomviewmodel.database.AppDatabase;
import com.pucmm.proyectofinal.roomviewmodel.model.Category;

import com.pucmm.proyectofinal.roomviewmodel.model.Product;
import com.pucmm.proyectofinal.roomviewmodel.viewmodel.ProductViewModel;

import java.util.List;

/**
 * A fragment representing a list of Items.
 */
public class ProductListFragment extends Fragment {

    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";
    // TODO: Customize parameters
    private int mColumnCount = 2;
    private AppDatabase appDatabase;
    private ProductAdapter productAdapter;
    private RecyclerView productListRecyclerView;
    private @NonNull FragmentCategoryListBinding binding;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public ProductListFragment() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static ProductListFragment newInstance(int columnCount) {
        ProductListFragment fragment = new ProductListFragment();
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
        View view = inflater.inflate(R.layout.fragment_product_list, container, false);
        productAdapter = new ProductAdapter(getActivity().getApplicationContext());
        FloatingActionButton floatingActionButton = view.findViewById(R.id.floatBtn_addProduct);
        
        productListRecyclerView = view.findViewById(R.id.productList);

        if(mColumnCount <= 1)
        {
            productListRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext()));

        }else
        {
            productListRecyclerView.setLayoutManager(new GridLayoutManager(getActivity().getApplicationContext(), mColumnCount));

        }

        appDatabase = AppDatabase.getInstance(getActivity().getApplicationContext());
        productAdapter = new ProductAdapter(getActivity().getApplicationContext());
        productListRecyclerView.setAdapter(productAdapter);

        //Pasando al fragmento de registrar categoria al clickear el boton flotante
        floatingActionButton.setOnClickListener(v ->
                startActivity(new Intent(getActivity(), ProductRegisterActivity.class))
        );

        retrieveTasks();

        return view;
    }

    //Cargando los datos live del view model que utilizara la lista
    private void retrieveTasks(){
        ProductViewModel productViewModel = new ViewModelProvider(this, new ViewModelProvider.Factory() {
            @NonNull
            @Override
            public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
                return (T) new ProductViewModel(appDatabase);
            }
        }).get(ProductViewModel.class);

        productViewModel.getProductListLiveData().observe(getActivity(), new Observer<List<Product>>() {

            @Override
            public void onChanged(List<Product> products) {
                productAdapter.setProducts(products);
            }
        });

    }

}