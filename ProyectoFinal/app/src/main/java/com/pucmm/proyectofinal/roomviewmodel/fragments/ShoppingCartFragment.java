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
import com.pucmm.proyectofinal.roomviewmodel.model.Product;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ShoppingCartFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ShoppingCartFragment extends Fragment {

    private SharedPreferences sharedPreferences;
    private ProductCartAdapter cartAdapter;
    private RecyclerView cartListRecyclerView;
    private int mColumnCount = 2;
    private List<Product> productList = new ArrayList<>();
    private TextView txtSubTotal;
    private TextView txtTotalPrice;


    public ShoppingCartFragment() {
        // Required empty public constructor
    }

    public static ShoppingCartFragment newInstance() {
        ShoppingCartFragment fragment = new ShoppingCartFragment();
        return fragment;
    }

    // TODO: Rename and change types and number of parameters
    public static ShoppingCartFragment newInstance(Product product) {
        ShoppingCartFragment fragment = new ShoppingCartFragment();
        Bundle args = new Bundle();
        args.putSerializable("product", product);
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

        View view = inflater.inflate(R.layout.fragment_shopping_cart, container, false);
        txtSubTotal = view.findViewById(R.id.txtSubTotal);
        txtTotalPrice = view.findViewById(R.id.txtTotalPrice);
        sharedPreferences = getActivity().getSharedPreferences("cart", Context.MODE_PRIVATE);
        cartListRecyclerView = view.findViewById(R.id.cartList);


        if(mColumnCount <= 1)
        {
            cartListRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext()));

        }else
        {
            cartListRecyclerView.setLayoutManager(new GridLayoutManager(getActivity().getApplicationContext(), 1));

        }
        cargarCarrito();
        cartListRecyclerView.setAdapter(cartAdapter);
        return view;

    }


    public void cargarCarrito() {
        //Accediendo a los datos guardados localmente

        Gson gson = new Gson();
        String json;
        for (Map.Entry<String, ?> entry : sharedPreferences.getAll().entrySet()) {
            json = sharedPreferences.getString(entry.getKey(),"");
            Product product = gson.fromJson(json,Product.class);
            productList.add(product);
        }
        cartAdapter = new ProductCartAdapter(getActivity().getApplicationContext(), txtSubTotal, txtTotalPrice);
        cartAdapter.setProducts(productList);
    }

}