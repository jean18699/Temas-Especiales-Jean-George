package com.pucmm.proyectofinal.roomviewmodel.adapters;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.pucmm.proyectofinal.databinding.ItemProductBinding;
import com.pucmm.proyectofinal.roomviewmodel.activities.ProductEditActivity;
import com.pucmm.proyectofinal.roomviewmodel.activities.ProductManagerActivity;
import com.pucmm.proyectofinal.roomviewmodel.model.Carousel;
import com.pucmm.proyectofinal.roomviewmodel.model.Product;
import com.pucmm.proyectofinal.roomviewmodel.model.ProductWithCarousel;
import com.pucmm.proyectofinal.utils.CommonUtil;
import com.pucmm.proyectofinal.utils.OnTouchListener;
import com.pucmm.proyectofinal.utils.OptionsMenuListener;

import java.util.List;
import java.util.Optional;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.MyViewHolder> {

    private List<ProductWithCarousel> productList;
    private Context context;
    private final OnTouchListener<ProductWithCarousel> mListener; //Agregado para que la lista responda a los eventos de click
    private OptionsMenuListener optionsMenuListener;

    public ProductAdapter(Context context, OnTouchListener<ProductWithCarousel> mListener) {
        this.context = context;
        this.mListener = mListener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemProductBinding productBinding = ItemProductBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false);
        return new MyViewHolder(productBinding,mListener);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.product = productList.get(position);

        holder.product_id.setText(holder.product.product.getProductId());
        holder.product_price.setText(holder.product.product.getPrice().toString());
        holder.product_description.setText(holder.product.product.getDescription());

        if (holder.product.carousels != null && !holder.product.carousels.isEmpty()) {
            Optional<Carousel> optional = holder.product.carousels.stream()
                    .sorted((a1, a2) -> Integer.valueOf(a1.getLineNum()).compareTo(a2.getLineNum()))
                    .findFirst();

            if (optional.isPresent()) {
                CommonUtil.downloadImage(optional.get().getPhoto(), holder.product_image);
            }
        }

        holder.editBtn.setOnClickListener(v->{
            if (optionsMenuListener != null) {
                optionsMenuListener.onCreateOptionsMenu(holder.editBtn, holder.product);
            }
              /*  Intent intent = new Intent(context, ProductManagerActivity.class);
                intent.putExtra("product",product);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);*/
        });

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

    public void setOptionsMenuListener(OptionsMenuListener optionsMenuListener) {
        this.optionsMenuListener = optionsMenuListener;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder  implements View.OnClickListener {

        public TextView product_id;
        public TextView product_price;
        public TextView product_description;
        public ImageView product_image;
        public ImageView editBtn;
        public OnTouchListener<ProductWithCarousel> mListener;
        public ProductWithCarousel product;


        public MyViewHolder(ItemProductBinding binding, OnTouchListener<ProductWithCarousel> listener) {
            super(binding.getRoot());
            product_id = binding.productId;
            product_price = binding.productPrice;
            product_description = binding.productDescription;
            product_image = binding.productImage;
            mListener = listener;
            editBtn = binding.configProduct;
            binding.getRoot().setOnClickListener(this);




        }

        @Override
        public void onClick(View v) {
            mListener.OnClick(product);
        }
    }
}
