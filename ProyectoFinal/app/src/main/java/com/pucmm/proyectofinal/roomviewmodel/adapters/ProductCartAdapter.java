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

import com.pucmm.proyectofinal.R;
import com.pucmm.proyectofinal.databinding.FragmentProductCartBinding;
import com.pucmm.proyectofinal.databinding.FragmentProductDetailBinding;
import com.pucmm.proyectofinal.roomviewmodel.activities.MainActivity;
import com.pucmm.proyectofinal.roomviewmodel.fragments.ProductDetailFragment;
import com.pucmm.proyectofinal.roomviewmodel.fragments.ShoppingCartFragment;
import com.pucmm.proyectofinal.roomviewmodel.model.Carousel;
import com.pucmm.proyectofinal.roomviewmodel.model.Product;
import com.pucmm.proyectofinal.roomviewmodel.model.ProductWithCarousel;
import com.pucmm.proyectofinal.utils.CommonUtil;

import org.w3c.dom.Text;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

public class ProductCartAdapter extends RecyclerView.Adapter<ProductCartAdapter.MyViewHolder> {

    private List<ProductWithCarousel> cartList;
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
        holder.product_price.setText(holder.product.product.getPrice().toString());
        holder.product_description.setText(holder.product.product.getDescription());
        holder.txtQuantity.setText(quantityPreferences.getString(holder.product.product.getProductId()+"_quantity", "1"));

        float total = 0;
        for(ProductWithCarousel item : cartList){
            total += item.product.getPrice() * Integer.valueOf(quantityPreferences.getString(item.product.getProductId()+"_quantity","1"));
        }

        txtSubTotal.setText("Sub total ("+ cartList.size() +" items): " );
        txtTotalPrice.setText(String.valueOf(total));

        holder.quantityValue = Integer.valueOf(quantityPreferences.getString(holder.product.product.getProductId()+"_quantity","1"));


        if (holder.product.carousels != null && !holder.product.carousels.isEmpty()) {
            Optional<Carousel> optional = holder.product.carousels.stream()
                    .sorted((a1, a2) -> Integer.valueOf(a1.getLineNum()).compareTo(a2.getLineNum()))
                    .findFirst();

            if (optional.isPresent()) {
                CommonUtil.downloadImage(optional.get().getPhoto(), holder.product_image);
            }
        }


    }

    @Override
    public int getItemCount() {
        if(cartList == null){
            return 0;
        }
        return cartList.size();
    }


    public void setProducts(List<ProductWithCarousel> products) {
        cartList = products;
        notifyDataSetChanged();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        public TextView product_price;
        public TextView product_description;
        public TextView txtQuantity;
        public ImageView product_image;
        public ProductWithCarousel product;
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
            product_image = binding.productImage;

            btnDelete.setOnClickListener(v->{

                int currentPosition = cartList.indexOf(product); //Tomo el indice del item que borrare para notificarle al fragmento que este index fue borrado
                sharedPreferences.edit().remove(product.product.getProductId()).apply();
                quantityPreferences.edit().remove(product.product.getProductId()+"_quantity").apply();
                cartList.remove(currentPosition);
                notifyItemRemoved(currentPosition);
                txtSubTotal.setText(String.valueOf(cartList.size()));

                float total = 0;
                for(ProductWithCarousel item : cartList){
                   total += item.product.getPrice() * Integer.valueOf(quantityPreferences.getString(item.product.getProductId()+"_quantity","1"));
                }
                txtSubTotal.setText("Sub total ("+ cartList.size() +" items): " );
                txtTotalPrice.setText(String.valueOf(total));
            });

            btnRemoveQuantity.setOnClickListener(v->{
               if(quantityValue > 1){
                   quantityValue--;
               }
               txtQuantity.setText(String.valueOf(quantityValue));
               editor.putString(product.product.getProductId()+"_quantity", String.valueOf(quantityValue)).commit();
                float total = 0;
                for(ProductWithCarousel item : cartList){
                    total += item.product.getPrice() * Integer.valueOf(quantityPreferences.getString(item.product.getProductId()+"_quantity","1"));
                }
                txtSubTotal.setText("Sub total ("+ cartList.size() +" items): " );
                txtTotalPrice.setText(String.valueOf(total));
            });

            btnAddQuantity.setOnClickListener(v->{
                quantityValue++;
                txtQuantity.setText(String.valueOf(quantityValue));
                editor.putString(product.product.getProductId()+"_quantity", String.valueOf(quantityValue)).commit();
                float total = 0;
                for(ProductWithCarousel item : cartList){
                    total += item.product.getPrice() * Integer.valueOf(quantityPreferences.getString(item.product.getProductId()+"_quantity","1"));
                }
                txtSubTotal.setText("Sub total ("+ cartList.size() +" items): " );
                txtTotalPrice.setText(String.valueOf(total));
            });

            product_image.setOnClickListener(v->{
                MainActivity activity = (MainActivity) context;
                cartList.clear(); //Para solucionar un bug de que la lista se duplicaba al volver a este fragmento desde detalle del producto
                activity.getSupportFragmentManager().beginTransaction()
                        .setReorderingAllowed(true)
                        .replace(R.id.content_frame, ProductDetailFragment.newInstance(product))
                        .addToBackStack(null)
                        .commit();
            });

        }

    }
}
