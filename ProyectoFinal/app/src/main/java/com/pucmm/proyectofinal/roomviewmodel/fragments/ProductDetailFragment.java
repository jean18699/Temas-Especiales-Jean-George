package com.pucmm.proyectofinal.roomviewmodel.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.gson.Gson;
import com.pucmm.proyectofinal.R;
import com.pucmm.proyectofinal.roomviewmodel.model.Product;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProductDetailFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProductDetailFragment extends Fragment {

    private Product product;
    private Integer quantity;

    public ProductDetailFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment ProductDetailFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProductDetailFragment newInstance(Product product) {
        ProductDetailFragment fragment = new ProductDetailFragment();
        Bundle args = new Bundle();
        args.putSerializable("product", product);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            product = (Product) getArguments().getSerializable("product");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        View view = inflater.inflate(R.layout.fragment_product_detail, container, false);
        TextView txtDescription = view.findViewById(R.id.txtDetailDescription);
        TextView txtPrice = view.findViewById(R.id.txtPriceDetail);
        TextView txtQuantity = view.findViewById(R.id.txtQuantity);
        Button btnRemoveQuantity = view.findViewById(R.id.btnRemoveQuantity);
        Button btnAddQuantity = view.findViewById(R.id.btnAddQuantity);
        Button btnAddToCart = view.findViewById(R.id.btnAddCar);

        //Para guardar el producto localmente en nuestro carrito
        SharedPreferences sharedPreferences =getActivity().getSharedPreferences("cart", Context.MODE_PRIVATE);
        SharedPreferences quantityPreferences =getActivity().getSharedPreferences("quantities", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        SharedPreferences.Editor editor2 = quantityPreferences.edit();

        quantity = Integer.valueOf(quantityPreferences.getString(product.getProductId()+"_quantity","1"));

       // sharedPreferences.edit().clear().commit();
        //quantityPreferences.edit().clear().commit();

        txtDescription.setText(product.getDescription());
        txtPrice.setText(product.getPrice().toString());
        txtQuantity.setText(String.valueOf(quantity));


        //Eventos
        btnRemoveQuantity.setOnClickListener(v->{
            if(quantity > 1){
                quantity--;
                txtQuantity.setText(String.valueOf(quantity));
            }

        });

        btnAddQuantity.setOnClickListener(v->{
            quantity++;
            txtQuantity.setText(String.valueOf(quantity));
        });

        btnAddToCart.setOnClickListener(v->{

            //Convertimos el producto a json para guardarlo
            Gson gson = new Gson();
            String jsonProduct = gson.toJson(product);
            editor.putString(product.getProductId(), jsonProduct);
            editor.commit();

            editor2.putString(product.getProductId()+"_quantity", txtQuantity.getText().toString());
            editor2.commit();

            getActivity().getSupportFragmentManager().beginTransaction()
                    .setReorderingAllowed(true)
                    .replace(R.id.content_frame, ShoppingCartFragment.newInstance(product))
                    .addToBackStack(null)
                    .commit();
        });

        return view;
    }
}