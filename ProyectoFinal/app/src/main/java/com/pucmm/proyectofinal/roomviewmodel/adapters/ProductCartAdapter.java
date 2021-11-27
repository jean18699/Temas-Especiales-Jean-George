package com.pucmm.proyectofinal.roomviewmodel.adapters;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.pucmm.proyectofinal.databinding.FragmentProductCartBinding;
import com.pucmm.proyectofinal.roomviewmodel.model.Product;

import org.w3c.dom.Text;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class ProductCartAdapter extends RecyclerView.Adapter<ProductCartAdapter.MyViewHolder> {

    private List<Product> cartList;
    private List<Integer> quantities;
    private Context context;
    private TextView txtSubTotal, txtTotalPrice;
    private SharedPreferences sharedPreferences;
    private SharedPreferences quantityPreferences;
    private SharedPreferences.Editor editor;

    public ProductCartAdapter(Context context, TextView txtSubTotal, TextView txtTotalPrice) {
        this.context = context;
        this.txtSubTotal = txtSubTotal;
        this.txtTotalPrice = txtTotalPrice;
        sharedPreferences = context.getSharedPreferences("cart", Context.MODE_PRIVATE);
        quantityPreferences =  context.getSharedPreferences("quantities", Context.MODE_PRIVATE);
        editor = quantityPreferences.edit();
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        FragmentProductCartBinding productBinding = FragmentProductCartBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false);
        return new MyViewHolder(productBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.product = cartList.get(position);
        holder.product_price.setText(holder.product.getPrice().toString());
        holder.product_description.setText(holder.product.getDescription());
        holder.txtQuantity.setText(quantityPreferences.getString(holder.product.getProductId()+"_quantity", "1"));

        float total = 0;
        for(Product item : cartList){
            total += item.getPrice() * Integer.valueOf(quantityPreferences.getString(item.getProductId()+"_quantity","1"));
        }

        txtSubTotal.setText("Sub total ("+ cartList.size() +" items): " );
        txtTotalPrice.setText(String.valueOf(total));

        holder.quantityValue = Integer.valueOf(quantityPreferences.getString(holder.product.getProductId()+"_quantity","1"));
        //holder.product_image.setText(product.getImage());


    }

    @Override
    public int getItemCount() {
        if(cartList == null){
            return 0;
        }
        return cartList.size();
    }


    public void setProducts(List<Product> products) {
        cartList = products;
        notifyDataSetChanged();
    }

    /*public void setQuantities(List<Integer> quantities) {
        cartList = products;
        notifyDataSetChanged();
    }*/

    public class MyViewHolder extends RecyclerView.ViewHolder{

        public TextView product_price;
        public TextView product_description;
        public TextView txtQuantity;
        public ImageView product_image;
        public Product product;
        public Button btnDelete;
        public Button btnAddQuantity;
        public Button btnRemoveQuantity;
        public Integer quantityValue;



        public MyViewHolder(FragmentProductCartBinding binding) {
            super(binding.getRoot());
            product_price = binding.productPrice;
            product_description = binding.productDescription;
            btnDelete = binding.btnDeleteProduct;
            btnAddQuantity = binding.btnAddQuantity;
            btnRemoveQuantity = binding.btnRemoveQuantity;
            txtQuantity = binding.txtQuantity;
            //product_image = binding.productImage;




            btnDelete.setOnClickListener(v->{

                int currentPosition = cartList.indexOf(product); //Tomo el indice del item que borrare para notificarle al fragmento que este index fue borrado
                sharedPreferences.edit().remove(product.getProductId()).apply();
                quantityPreferences.edit().remove(product.getProductId()+"_quantity").apply();
                cartList.remove(currentPosition);
                notifyItemRemoved(currentPosition);
                txtSubTotal.setText(String.valueOf(cartList.size()));

                float total = 0;
                for(Product item : cartList){
                   total += item.getPrice() * Integer.valueOf(quantityPreferences.getString(item.getProductId()+"_quantity","1"));
                }
                txtSubTotal.setText("Sub total ("+ cartList.size() +" items): " );
                txtTotalPrice.setText(String.valueOf(total));
            });

            btnRemoveQuantity.setOnClickListener(v->{
               if(quantityValue > 1){
                   quantityValue--;
               }
               txtQuantity.setText(String.valueOf(quantityValue));
               editor.putString(product.getProductId()+"_quantity", String.valueOf(quantityValue)).commit();
                float total = 0;
                for(Product item : cartList){
                    total += item.getPrice() * Integer.valueOf(quantityPreferences.getString(item.getProductId()+"_quantity","1"));
                }
                txtSubTotal.setText("Sub total ("+ cartList.size() +" items): " );
                txtTotalPrice.setText(String.valueOf(total));
            });

            btnAddQuantity.setOnClickListener(v->{
                quantityValue++;
                txtQuantity.setText(String.valueOf(quantityValue));
                editor.putString(product.getProductId()+"_quantity", String.valueOf(quantityValue)).commit();
                float total = 0;
                for(Product item : cartList){
                    total += item.getPrice() * Integer.valueOf(quantityPreferences.getString(item.getProductId()+"_quantity","1"));
                }
                txtSubTotal.setText("Sub total ("+ cartList.size() +" items): " );
                txtTotalPrice.setText(String.valueOf(total));
            });

        }

    }
}
