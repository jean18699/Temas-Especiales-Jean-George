package com.pucmm.proyectofinal.roomviewmodel.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
import com.google.android.material.snackbar.Snackbar;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.pucmm.proyectofinal.R;
import com.pucmm.proyectofinal.networksync.FirebaseNetwork;
import com.pucmm.proyectofinal.networksync.NetResponse;
import com.pucmm.proyectofinal.roomviewmodel.activities.ProductManagerActivity;
import com.pucmm.proyectofinal.roomviewmodel.adapters.ProductAdapter;
import com.pucmm.proyectofinal.roomviewmodel.database.AppDatabase;

import com.pucmm.proyectofinal.roomviewmodel.database.AppExecutors;
import com.pucmm.proyectofinal.roomviewmodel.model.Category;
import com.pucmm.proyectofinal.roomviewmodel.model.ProductWithCarousel;
import com.pucmm.proyectofinal.roomviewmodel.model.User;
import com.pucmm.proyectofinal.roomviewmodel.viewmodel.ProductViewModel;
import com.pucmm.proyectofinal.utils.CommonUtil;
import com.pucmm.proyectofinal.utils.KProgressHUDUtils;
import com.pucmm.proyectofinal.utils.OnTouchListener;
import com.pucmm.proyectofinal.utils.OptionsMenuListener;
import com.shashank.sony.fancytoastlib.FancyToast;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * A fragment representing a list of Items.
 */
public class ProductListFragment extends Fragment implements OnTouchListener<ProductWithCarousel> {

    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";
    // TODO: Customize parameters
    private int mColumnCount = 2;
    private AppDatabase appDatabase;
    private ProductAdapter productAdapter;
    private RecyclerView productListRecyclerView;
    private Category selectedCategory;
    private User user;
    private String searchQuery;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public ProductListFragment() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static ProductListFragment newInstance(int columnCount, Category category, User user, String searchQuery) {
        ProductListFragment fragment = new ProductListFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        args.putSerializable("selectedCategory", category);
        args.putSerializable("user", user);
        args.putString("query", searchQuery);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
            selectedCategory = (Category) getArguments().getSerializable("selectedCategory");
            user = (User) getArguments().getSerializable("user");
            searchQuery = getArguments().getString("query");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_product_list, container, false);
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
        productAdapter = new ProductAdapter(getActivity().getApplicationContext(), user, this);


        //Pasando al fragmento de registrar categoria al clickear el boton flotante
        floatingActionButton.setOnClickListener(v ->
                startActivity(new Intent(getActivity(), ProductManagerActivity.class))
        );

        retrieveTasks();
        productListRecyclerView.setAdapter(productAdapter);

        productAdapter.setOptionsMenuListener((OptionsMenuListener<ProductWithCarousel>) (view1, element) -> {
            CommonUtil.popupMenu(getContext(), view1, () -> {
                Intent intent = new Intent(getContext(), ProductManagerActivity.class);
                intent.putExtra("product",element);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);

                }, () -> {
                CommonUtil.alertDialog(getContext(), "Confirm dialog delete!",
                        "You are about to delete record. Do you really want to proceed?",
                        () -> delete(element));
            });
        });
        return view;
    }

    private void delete(ProductWithCarousel element) {

        if(element.product.isActive() == true){
            Snackbar.make(getView(), "Este producto ha sido comprado con anterioridad, no se puede borrar.", Snackbar.LENGTH_LONG).show();
        }else
        {
            final KProgressHUD progressDialog = new KProgressHUDUtils(getActivity()).showConnecting();
            function.apply(progressDialog).apply(true).accept(element);

            if (element.carousels != null && !element.carousels.isEmpty()) {
                FirebaseNetwork.obtain().deletes(element.carousels, new NetResponse<String>() {
                    @Override
                    public void onResponse(String response) {
                        function.apply(progressDialog).apply(true).accept(element);
                    }

                    @Override
                    public void onFailure(Throwable t) {
                        function.apply(progressDialog).apply(false).accept(element);
                        FancyToast.makeText(getContext(), t.getMessage(), FancyToast.LENGTH_LONG, FancyToast.ERROR, false).show();
                    }
                });
            } else {
                function.apply(progressDialog).apply(true).accept(element);
            }
        }


    }

    private final Function<KProgressHUD, Function<Boolean, Consumer<ProductWithCarousel>>> function = progress -> success -> element -> {
        if (success) {
            AppExecutors.getInstance().diskIO().execute(() -> {
                appDatabase.productDao().delete(element.product);
                appDatabase.productDao().deleteCarousels(element.carousels);

                SharedPreferences sharedPreferences = getActivity().getSharedPreferences("cart_"+user.getUid(), Context.MODE_PRIVATE);
                SharedPreferences quantityPreferences = getActivity().getSharedPreferences("quantities_"+user.getUid(), Context.MODE_PRIVATE);
                sharedPreferences.edit().remove(element.product.getProductId()).apply();
                quantityPreferences.edit().remove(element.product.getProductId()+"_quantity").apply();
                getActivity().runOnUiThread(() -> FancyToast.makeText(getContext(), "Successfully deleted!", FancyToast.LENGTH_LONG, FancyToast.SUCCESS, false).show());


            });
        }
        progress.dismiss();
    };

    //Cargando los datos live del view model que utilizara la lista
    private void retrieveTasks(){
        ProductViewModel productViewModel = new ViewModelProvider(this, new ViewModelProvider.Factory() {
            @NonNull
            @Override
            public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
                return (T) new ProductViewModel(appDatabase,selectedCategory, searchQuery);
            }
        }).get(ProductViewModel.class);

        productViewModel.getProductListLiveData().observe(getActivity(), products -> productAdapter.setProducts(products));


    }

    @Override
    public void OnClick(ProductWithCarousel element) {
        getActivity().getSupportFragmentManager().beginTransaction()
                .setReorderingAllowed(true)
                .replace(R.id.content_frame, ProductDetailFragment.newInstance(element, user))
                .addToBackStack(null)
                .commit();
    }
}