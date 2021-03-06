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
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.pucmm.proyectofinal.R;
import com.pucmm.proyectofinal.roomviewmodel.adapters.ProductCartAdapter;
import com.pucmm.proyectofinal.roomviewmodel.database.AppDatabase;
import com.pucmm.proyectofinal.roomviewmodel.database.AppExecutors;
import com.pucmm.proyectofinal.roomviewmodel.model.Notification;
import com.pucmm.proyectofinal.roomviewmodel.model.ProductWithCarousel;
import com.pucmm.proyectofinal.roomviewmodel.model.User;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ShoppingCartFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ShoppingCartFragment extends Fragment {


    private SharedPreferences sharedPreferences;
    private SharedPreferences quantityPreferences;
    private ProductCartAdapter cartAdapter;
    private RecyclerView cartListRecyclerView;
    private int mColumnCount = 2;
    private AppDatabase appDatabase;
    private List<ProductWithCarousel> productList = new ArrayList<>();
    private TextView txtSubTotal;
    private TextView txtTotalPrice;
    private User user;
    private TextView txtCarQuantity;
    private TextView txtNotifQuantity;
    private SharedPreferences notificationPreferences;
    private Notification notification;

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
        quantityPreferences =getActivity().getSharedPreferences("quantities_"+user.getUid(), Context.MODE_PRIVATE);

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
        txtCarQuantity = getActivity().findViewById(R.id.cart_badge);
        Button btnCheckout = view.findViewById(R.id.btnCheckout);
        //sharedPreferences.edit().clear().commit(); //limpiar shared preferences
        cartListRecyclerView = view.findViewById(R.id.cartList);
        txtNotifQuantity = getActivity().findViewById(R.id.notification_badge);
        notificationPreferences = getActivity().getSharedPreferences("notifications_" + user.getUid(), Context.MODE_PRIVATE);


        if(mColumnCount <= 1)
        {
            cartListRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext()));

        }else
        {
            cartListRecyclerView.setLayoutManager(new GridLayoutManager(getActivity().getApplicationContext(), 1));

        }
        loadCart();
        cartListRecyclerView.setAdapter(cartAdapter);


        btnCheckout.setOnClickListener(v->{
            if(productList.size() > 0){
                buyProducts();
            }else
            {
                Snackbar.make(getView(), "No hay productos a comprar en el carrito...", Snackbar.LENGTH_LONG).show();
            }

        });

        return view;

    }

    private void buyProducts(){
        AppExecutors.getInstance().diskIO().execute(() -> {
            for(ProductWithCarousel product : productList){
                product.product.setActive(true);
                appDatabase.productDao().update(product.product);
            }
            notification = new Notification("Se ha realizado la compra de " + productList.size() + " producto(s) en la fecha: " + new Date());
            productList.clear();
            sharedPreferences.edit().clear().commit();
            quantityPreferences.edit().clear().commit();

        });

        cartAdapter.setProducts(productList);
        txtSubTotal.setText("Sub total(0 items): ");
        txtTotalPrice.setText("0.0");
        txtCarQuantity.setText("0");
        Snackbar.make(getView(), "Su compra ha sido realizada con exito!", Snackbar.LENGTH_LONG).show();
        Gson notifGson = new Gson();
        String jsonNotif = notifGson.toJson(notification);
        notificationPreferences.edit().putString(String.valueOf(notificationPreferences.getAll().size()+1), jsonNotif).commit();
        txtNotifQuantity.setText(String.valueOf(notificationPreferences.getAll().size()));
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
        cartAdapter = new ProductCartAdapter(getActivity(), txtSubTotal, txtTotalPrice,txtCarQuantity, user);
        cartAdapter.setProducts(productList);
        txtCarQuantity.setText(String.valueOf(productList.size()));
    }

}