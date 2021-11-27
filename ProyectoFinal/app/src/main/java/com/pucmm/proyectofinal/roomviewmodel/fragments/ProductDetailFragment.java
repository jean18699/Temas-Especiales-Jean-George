package com.pucmm.proyectofinal.roomviewmodel.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

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

        AtomicInteger quantity = new AtomicInteger(1);
        View view = inflater.inflate(R.layout.fragment_product_detail, container, false);
        TextView txtDescription = view.findViewById(R.id.txtDetailDescription);
        TextView txtPrice = view.findViewById(R.id.txtPriceDetail);
        TextView txtQuantity = view.findViewById(R.id.txtQuantity);
        Button btnRemoveQuantity = view.findViewById(R.id.btnRemoveQuantity);
        Button btnAddQuantity = view.findViewById(R.id.btnAddQuantity);


        txtDescription.setText(product.getDescription());
        txtPrice.setText(product.getPrice().toString());


        btnRemoveQuantity.setOnClickListener(v->{
            if(quantity.get() > 1){
                quantity.getAndDecrement();
                txtQuantity.setText(String.valueOf(quantity.get()));
            }

        });

        btnAddQuantity.setOnClickListener(v->{
            quantity.getAndIncrement();
            txtQuantity.setText(String.valueOf(quantity.get()));
        });



        return view;
    }
}