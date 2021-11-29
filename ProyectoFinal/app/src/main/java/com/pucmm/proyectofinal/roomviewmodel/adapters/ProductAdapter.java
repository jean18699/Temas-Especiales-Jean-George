package com.pucmm.proyectofinal.roomviewmodel.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.pucmm.proyectofinal.databinding.FragmentProductBinding;
import com.pucmm.proyectofinal.roomviewmodel.activities.ProductEditActivity;
import com.pucmm.proyectofinal.roomviewmodel.model.Product;
import com.pucmm.proyectofinal.roomviewmodel.model.ProductWithCarousel;
import com.pucmm.proyectofinal.utils.OnTouchListener;

import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.MyViewHolder> {

    private List<ProductWithCarousel> productList;
    private Context context;
    private final OnTouchListener<Product> mListener; //Agregado para que la lista responda a los eventos de click


    public ProductAdapter(Context context, OnTouchListener<Product> mListener) {
        this.context = context;
        this.mListener = mListener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        FragmentProductBinding productBinding = FragmentProductBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false);
        return new MyViewHolder(productBinding,mListener);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.product = productList.get(position);

        holder.product_id.setText(holder.product.product.getProductId());
        holder.product_price.setText(holder.product.product.getPrice().toString());
        holder.product_description.setText(holder.product.product.getDescription());
        //holder.product_image.setText(product.getImage());


    }

    @Override
    public int getItemCount() {
        if(productList == null){
            return 0;
        }
        return productList.size();
    }

    //Asignando dinamicamente a la lista
    public void setProducts(List<ProductWithCarousel> products) {
        productList = products;
        notifyDataSetChanged();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder  implements View.OnClickListener {

        public TextView product_id;
        public TextView product_price;
        public TextView product_description;
        public ImageView product_image;
        public ImageView editBtn;
        public OnTouchListener<Product> mListener;
        public ProductWithCarousel product;


        public MyViewHolder(FragmentProductBinding binding, OnTouchListener<Product> listener) {
            super(binding.getRoot());
            product_id = binding.productId;
            product_price = binding.productPrice;
            product_description = binding.productDescription;
            //product_image = binding.productImage;
            mListener = listener;
            editBtn = binding.configProduct;
            binding.getRoot().setOnClickListener(this);


            editBtn.setOnClickListener(v->{
                Intent intent = new Intent(context, ProductEditActivity.class);
                intent.putExtra("product",product);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            });

        }

        @Override
        public void onClick(View v) {
            mListener.OnClick(product.product);
        }
    }
}
