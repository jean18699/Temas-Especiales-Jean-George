package com.pucmm.proyectofinal.roomviewmodel.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.gson.Gson;
import com.pucmm.proyectofinal.R;
import com.pucmm.proyectofinal.roomviewmodel.adapters.ProductCartAdapter;
import com.pucmm.proyectofinal.roomviewmodel.database.AppDatabase;
import com.pucmm.proyectofinal.roomviewmodel.database.AppExecutors;
import com.pucmm.proyectofinal.roomviewmodel.model.ProductWithCarousel;
import com.pucmm.proyectofinal.roomviewmodel.model.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ShoppingCartFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ShoppingCartFragment extends Fragment {

    private static final String TAG = "ShoppingCartFragment";
    private SharedPreferences sharedPreferences;
    private ProductCartAdapter cartAdapter;
    private RecyclerView cartListRecyclerView;
    private int mColumnCount = 2;
    private AppDatabase appDatabase;
    private List<ProductWithCarousel> productList = new ArrayList<>();
    private TextView txtSubTotal;
    private TextView txtTotalPrice;
    private User user;


    public ShoppingCartFragment() {
        // Required empty public constructor
    }

    public static ShoppingCartFragment newInstance(User user) {
        Bundle args = new Bundle();
        ShoppingCartFragment fragment = new ShoppingCartFragment();
        args.putSerializable("user", user);
        fragment.setArguments(args);
        return fragment;
    }

    // TODO: Rename and change types and number of parameters
    public static ShoppingCartFragment newInstance(ProductWithCarousel product, User user) {
        ShoppingCartFragment fragment = new ShoppingCartFragment();
        Bundle args = new Bundle();
        args.putSerializable("product", product);
        args.putSerializable("user", user);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        appDatabase = AppDatabase.getInstance(getContext());
        user = (User) getArguments().getSerializable("user");
        sharedPreferences = getActivity().getSharedPreferences("cart_"+user.getUid(), Context.MODE_PRIVATE);
        AppExecutors.getInstance().diskIO().execute(() -> { updateCart();});

        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_shopping_cart, container, false);
        txtSubTotal = view.findViewById(R.id.txtSubTotal);
        txtTotalPrice = view.findViewById(R.id.txtTotalPrice);

        //sharedPreferences.edit().clear().commit(); //limpiar shared preferences
        cartListRecyclerView = view.findViewById(R.id.cartList);


        if(mColumnCount <= 1)
        {
            cartListRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext()));

        }else
        {
            cartListRecyclerView.setLayoutManager(new GridLayoutManager(getActivity().getApplicationContext(), 1));

        }
        loadCart();
        cartListRecyclerView.setAdapter(cartAdapter);
        return view;

    }

    private void updateCart()
    {
        //Si editamos un producto en ProductManagerActivity y volvemos a la actividad principal,
        // tendremos que actualizar tambien los productos del carrito.
        Gson gson = new Gson();
        for(ProductWithCarousel product : appDatabase.productDao().getProducts()){
            String jsonProduct = gson.toJson(product);
            if(sharedPreferences.getString(product.product.getProductId(), null) != null)
            {
                sharedPreferences.edit().putString(product.product.getProductId(),jsonProduct).commit();
            }
        }
    }

    public void loadCart() {
        //Accediendo a los datos guardados localmente
        productList.clear();
        Gson gson = new Gson();
        String json;
        for (Map.Entry<String, ?> entry : sharedPreferences.getAll().entrySet()) {
            json = sharedPreferences.getString(entry.getKey(),"");
            ProductWithCarousel product = gson.fromJson(json,ProductWithCarousel.class);
            productList.add(product);
        }
        cartAdapter = new ProductCartAdapter(getActivity(), txtSubTotal, txtTotalPrice, user);
        cartAdapter.setProducts(productList);
    }

}