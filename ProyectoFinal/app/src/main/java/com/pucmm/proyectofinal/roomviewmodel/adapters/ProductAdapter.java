package com.pucmm.proyectofinal.roomviewmodel.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.pucmm.proyectofinal.databinding.FragmentProductBinding;
import com.pucmm.proyectofinal.roomviewmodel.model.Product;

import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.MyViewHolder> {

    private List<Product> productList;
    private Context context;
    private FragmentProductBinding productBinding;


    public ProductAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        productBinding = FragmentProductBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false);
        return new MyViewHolder(productBinding.getRoot());
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        final Product product = productList.get(position);
        holder.product_id.setText(product.getProductId());
        holder.product_price.setText(product.getPrice().toString());
        holder.product_description.setText(product.getDescription());
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
    public void setProducts(List<Product> products) {
        productList = products;
        notifyDataSetChanged();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView product_id;
        private TextView product_price;
        private TextView product_description;
        private ImageView product_image;



        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            product_id = productBinding.productId;
            product_price = productBinding.productPrice;
            product_description = productBinding.productDescription;
            product_image = productBinding.productImage;
        }
    }
}
